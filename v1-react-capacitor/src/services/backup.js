/**
 * backup.js — Local file backup/restore for Naam Smaran.
 * 
 * Simplified from AGENTS.md §10 (Google Drive deferred).
 * Instead: exports/imports a single backup.json to device storage.
 * 
 * Backup format:
 * {
 *   version: 1,
 *   exportedAt: ISO timestamp,
 *   appName: "NaamSmaran",
 *   data: {
 *     dailyRecords: [...],
 *     appSettings: {...},
 *     quotes: [...]
 *   }
 * }
 * 
 * Uses @capacitor/filesystem for native file I/O.
 * Falls back to browser download/upload for web testing.
 */

import { Capacitor } from '@capacitor/core';
import { Filesystem, Directory, Encoding } from '@capacitor/filesystem';
import { getAllRecords, getRecord, putRecord, clearStore } from '../store/db';

const BACKUP_FILENAME = 'naam_smaran_backup.json';
const BACKUP_DIR = 'NaamSmaran';
const BACKUP_VERSION = 1;

/**
 * Create a full backup of all app data.
 * @returns {Object} The backup data object
 */
export async function createBackupData() {
  const dailyRecords = await getAllRecords('DailyRecord');
  const settings = await getRecord('AppSettings', 1);
  const quotes = await getAllRecords('Quotes');

  return {
    version: BACKUP_VERSION,
    exportedAt: new Date().toISOString(),
    appName: 'NaamSmaran',
    data: {
      dailyRecords: dailyRecords || [],
      appSettings: settings || null,
      quotes: quotes || [],
    },
  };
}

/**
 * Export backup to device storage.
 * On native: writes to Documents/NaamSmaran/naam_smaran_backup.json
 * On web: triggers browser download
 * 
 * @returns {{ success: boolean, path?: string, error?: string }}
 */
export async function exportBackup() {
  try {
    const backupData = await createBackupData();
    const jsonStr = JSON.stringify(backupData, null, 2);

    if (Capacitor.isNativePlatform()) {
      // Ensure directory exists
      try {
        await Filesystem.mkdir({
          path: BACKUP_DIR,
          directory: Directory.Documents,
          recursive: true,
        });
      } catch {
        // Directory may already exist — that's fine
      }

      const result = await Filesystem.writeFile({
        path: `${BACKUP_DIR}/${BACKUP_FILENAME}`,
        data: jsonStr,
        directory: Directory.Documents,
        encoding: Encoding.UTF8,
      });

      // Update last backup timestamp in settings
      const settings = await getRecord('AppSettings', 1);
      if (settings) {
        settings.lastBackupTimestamp = Date.now();
        await putRecord('AppSettings', settings);
      }

      return {
        success: true,
        path: result.uri,
      };
    } else {
      // Browser fallback: trigger download
      const blob = new Blob([jsonStr], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = BACKUP_FILENAME;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);

      return { success: true, path: 'download' };
    }
  } catch (err) {
    console.error('[Backup] Export failed:', err);
    return { success: false, error: err.message };
  }
}

/**
 * Import backup from device storage.
 * On native: reads from Documents/NaamSmaran/naam_smaran_backup.json
 * On web: opens file picker
 * 
 * @returns {{ success: boolean, recordCount?: number, error?: string }}
 */
export async function importBackup() {
  try {
    let jsonStr;

    if (Capacitor.isNativePlatform()) {
      const result = await Filesystem.readFile({
        path: `${BACKUP_DIR}/${BACKUP_FILENAME}`,
        directory: Directory.Documents,
        encoding: Encoding.UTF8,
      });
      jsonStr = result.data;
    } else {
      // Browser fallback: file picker
      jsonStr = await browserFilePickerRead();
    }

    if (!jsonStr) {
      return { success: false, error: 'No data read' };
    }

    return await restoreFromJson(jsonStr);
  } catch (err) {
    console.error('[Backup] Import failed:', err);
    return { success: false, error: err.message };
  }
}

/**
 * Restore data from a JSON string.
 * Clears existing data and imports the backup.
 * 
 * @param {string} jsonStr - The backup JSON string
 * @returns {{ success: boolean, recordCount?: number, error?: string }}
 */
export async function restoreFromJson(jsonStr) {
  try {
    const backup = JSON.parse(jsonStr);

    // Validate structure
    if (!backup.appName || backup.appName !== 'NaamSmaran') {
      return { success: false, error: 'Invalid backup file — not a Naam Smaran backup' };
    }

    if (!backup.data) {
      return { success: false, error: 'Backup contains no data' };
    }

    const { dailyRecords, appSettings, quotes } = backup.data;

    // Clear existing data
    await clearStore('DailyRecord');
    await clearStore('Quotes');
    // NOTE: We don't clear AppSettings — we merge instead

    // Restore DailyRecords
    let recordCount = 0;
    if (dailyRecords && Array.isArray(dailyRecords)) {
      for (const record of dailyRecords) {
        await putRecord('DailyRecord', record);
        recordCount++;
      }
    }

    // Restore AppSettings (merge with existing to preserve any new fields)
    if (appSettings) {
      const existingSettings = await getRecord('AppSettings', 1);
      const mergedSettings = {
        ...existingSettings,
        ...appSettings,
        id: 1, // Ensure ID stays 1
        lastBackupTimestamp: Date.now(), // Update timestamp
      };
      await putRecord('AppSettings', mergedSettings);
    }

    // Restore Quotes
    if (quotes && Array.isArray(quotes)) {
      for (const quote of quotes) {
        await putRecord('Quotes', quote);
      }
    }

    return {
      success: true,
      recordCount,
    };
  } catch (err) {
    console.error('[Backup] Restore failed:', err);
    return { success: false, error: `Parse error: ${err.message}` };
  }
}

/**
 * Browser file picker fallback.
 * Opens an invisible <input type="file"> to select a JSON file.
 * @returns {Promise<string>} The file contents as a string
 */
function browserFilePickerRead() {
  return new Promise((resolve) => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json,application/json';

    input.onchange = (e) => {
      const file = e.target.files?.[0];
      if (!file) {
        resolve(null);
        return;
      }
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = () => resolve(null);
      reader.readAsText(file);
    };

    // Cancel = resolve null
    input.oncancel = () => resolve(null);

    input.click();
  });
}

/**
 * Check if a backup file exists on device.
 * @returns {{ exists: boolean, lastModified?: string }}
 */
export async function checkBackupExists() {
  if (!Capacitor.isNativePlatform()) {
    return { exists: false };
  }

  try {
    const stat = await Filesystem.stat({
      path: `${BACKUP_DIR}/${BACKUP_FILENAME}`,
      directory: Directory.Documents,
    });

    return {
      exists: true,
      lastModified: stat.mtime ? new Date(stat.mtime).toISOString() : undefined,
    };
  } catch {
    return { exists: false };
  }
}
