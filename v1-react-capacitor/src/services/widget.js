/**
 * widget.js — Android home screen widget bridge for Naam Smaran.
 * 
 * This service provides the data layer that an Android widget reads from.
 * The actual widget UI is implemented in native Android XML/Kotlin
 * (generated when `npx cap add android` runs in Phase B).
 * 
 * Strategy:
 * - Write widget data to SharedPreferences via Capacitor Preferences
 * - The native Android widget reads from the same SharedPreferences
 * - Data is updated whenever the jap count changes
 * 
 * Widget displays:
 * - Today's jap count (did)
 * - Today's target
 * - Progress percentage
 * - Streak count
 * 
 * STATUS: Bridge ready. Native widget XML will be added in Phase B.
 */

import { Capacitor } from '@capacitor/core';
import { formatNumber } from '../logic/numberFormat';

/** SharedPreferences key prefix for widget data */
const WIDGET_PREFIX = 'widget_';

/**
 * Update widget data in SharedPreferences.
 * Called from the store whenever todayRecord changes.
 * 
 * @param {Object} todayRecord - Today's DailyRecord
 * @param {Object} settings - AppSettings
 */
export async function updateWidgetData(todayRecord, settings) {
  if (!Capacitor.isNativePlatform()) return;

  // Capacitor Preferences plugin isn't installed yet — using localStorage bridge
  // In Phase B, this will be replaced with @capacitor/preferences
  try {
    const target = todayRecord?.target || settings?.initialTarget || 21600;
    const did = todayRecord?.did || 0;
    const progress = target > 0 ? Math.round((did / target) * 100) : 0;
    const streak = todayRecord?.streakCount || 0;

    const widgetData = {
      did: formatNumber(did),
      target: formatNumber(target),
      progress,
      streak,
      date: todayRecord?.date || '',
      dayOfWeek: todayRecord?.dayOfWeek || '',
      updatedAt: Date.now(),
    };

    // Store as JSON string — native widget will parse this
    localStorage.setItem(
      `${WIDGET_PREFIX}data`,
      JSON.stringify(widgetData)
    );

    console.log('[Widget] Data updated:', widgetData);
  } catch (err) {
    console.warn('[Widget] Update failed:', err);
  }
}

/**
 * Get current widget data (for debugging / settings preview).
 * @returns {Object|null}
 */
export function getWidgetData() {
  try {
    const raw = localStorage.getItem(`${WIDGET_PREFIX}data`);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

/**
 * Clear widget data (on data reset / logout).
 */
export function clearWidgetData() {
  try {
    localStorage.removeItem(`${WIDGET_PREFIX}data`);
  } catch {
    // Silently fail
  }
}
