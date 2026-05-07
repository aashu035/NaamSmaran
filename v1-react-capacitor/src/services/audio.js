/**
 * audio.js — Background audio player for Naam Smaran.
 * 
 * Per AGENTS.md §11:
 * - User places their mp3 in the app's documents directory manually
 * - Filename is set in AppSettings.audioFileName
 * - No file picker needed, no complexity
 * 
 * Uses HTML5 Audio API (works in both browser and Capacitor WebView).
 * Capacitor Filesystem is used only to verify file existence on native.
 * 
 * STATUS: Service skeleton ready. Audio feature deferred — no files bundled.
 * Audio will activate when user provides mp3 and sets filename in Settings.
 */

import { Capacitor } from '@capacitor/core';
import { Filesystem, Directory } from '@capacitor/filesystem';

let audioInstance = null;
let isPlaying = false;

/**
 * Get the audio file URI based on platform.
 * On native: reads from Documents/audio/{filename}
 * On web: reads from /audio/{filename} in public folder
 */
async function getAudioUri(fileName) {
  if (!fileName) return null;

  if (Capacitor.isNativePlatform()) {
    try {
      // Check if file exists in Documents/audio/
      const result = await Filesystem.stat({
        path: `audio/${fileName}`,
        directory: Directory.Documents,
      });

      if (result) {
        const uri = await Filesystem.getUri({
          path: `audio/${fileName}`,
          directory: Directory.Documents,
        });
        return Capacitor.convertFileSrc(uri.uri);
      }
    } catch {
      console.warn(`[Audio] File not found: Documents/audio/${fileName}`);
      return null;
    }
  } else {
    // Browser fallback — serve from public/audio/
    return `/audio/${fileName}`;
  }

  return null;
}

/**
 * Initialize audio with the given settings.
 * Does NOT auto-play — call play() separately.
 * 
 * @param {Object} settings - AppSettings from the store
 */
export async function initAudio(settings) {
  if (!settings.audioFileName) {
    console.log('[Audio] No audio file configured');
    return;
  }

  const uri = await getAudioUri(settings.audioFileName);
  if (!uri) {
    console.log('[Audio] Audio file not accessible');
    return;
  }

  // Clean up previous instance
  if (audioInstance) {
    audioInstance.pause();
    audioInstance.src = '';
    audioInstance = null;
  }

  audioInstance = new Audio(uri);
  audioInstance.volume = settings.audioVolume ?? 0.7;
  audioInstance.loop = settings.audioLoop ?? true;
  audioInstance.preload = 'auto';

  // Handle errors gracefully
  audioInstance.addEventListener('error', (e) => {
    console.warn('[Audio] Playback error:', e);
    isPlaying = false;
  });

  audioInstance.addEventListener('ended', () => {
    if (!audioInstance?.loop) {
      isPlaying = false;
    }
  });

  console.log(`[Audio] Initialized: ${settings.audioFileName}`);
}

/**
 * Start or resume playback.
 */
export async function play() {
  if (!audioInstance) {
    console.log('[Audio] No audio initialized');
    return;
  }

  try {
    await audioInstance.play();
    isPlaying = true;
  } catch (err) {
    // Auto-play may be blocked until user interaction
    console.warn('[Audio] Play blocked (needs user gesture):', err);
    isPlaying = false;
  }
}

/**
 * Pause playback.
 */
export function pause() {
  if (audioInstance && isPlaying) {
    audioInstance.pause();
    isPlaying = false;
  }
}

/**
 * Toggle play/pause.
 * @returns {boolean} New playing state
 */
export async function toggle() {
  if (isPlaying) {
    pause();
    return false;
  } else {
    await play();
    return true;
  }
}

/**
 * Set volume (0.0 to 1.0).
 */
export function setVolume(vol) {
  if (audioInstance) {
    audioInstance.volume = Math.max(0, Math.min(1, vol));
  }
}

/**
 * Get current playback state.
 */
export function getState() {
  return {
    isPlaying,
    hasAudio: !!audioInstance,
    currentTime: audioInstance?.currentTime || 0,
    duration: audioInstance?.duration || 0,
  };
}

/**
 * Dispose audio resources.
 * Call on app background/destroy.
 */
export function dispose() {
  if (audioInstance) {
    audioInstance.pause();
    audioInstance.src = '';
    audioInstance = null;
  }
  isPlaying = false;
}
