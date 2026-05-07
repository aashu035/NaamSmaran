/**
 * notifications.js — Local notification service for Naam Smaran.
 * 
 * 5 reminder types per AGENTS.md §9:
 *   1. Morning    — 04:00 AM  — Today's target + "शुभ साधना हो 🙏"
 *   2. Evening    — 20:00 PM  — Progress update + remaining count
 *   3. Day-end    — 22:00 PM  — "दिन का विश्लेषण तैयार है"
 *   4. Progress   — 14:00 PM  — "X नाम जप शेष हैं" if >5000 remaining
 *   5. Encourage  — 06:00 AM  — Random pad/quote from the library
 * 
 * Uses @capacitor/local-notifications.
 * Gracefully degrades to no-op in browser environment.
 */

import { LocalNotifications } from '@capacitor/local-notifications';
import { Capacitor } from '@capacitor/core';
import { formatNumber } from '../logic/numberFormat';

/** Check if running on native platform */
const isNative = () => Capacitor.isNativePlatform();

/** Notification channel IDs (Android 8+) */
const CHANNELS = {
  SADHANA: 'sadhana-reminders',
  DAILY:   'daily-analysis',
  INSPIRE: 'inspiration',
};

/**
 * Request notification permissions.
 * Call once on first app launch or from Settings.
 * @returns {boolean} Whether permission was granted
 */
export async function requestNotificationPermission() {
  if (!isNative()) {
    console.log('[Notifications] Browser mode — skipping permission request');
    return false;
  }

  try {
    const result = await LocalNotifications.requestPermissions();
    return result.display === 'granted';
  } catch (err) {
    console.warn('[Notifications] Permission request failed:', err);
    return false;
  }
}

/**
 * Create Android notification channels.
 * Required for Android 8.0+ (API 26+). Call once on app init.
 */
export async function createNotificationChannels() {
  if (!isNative()) return;

  try {
    await LocalNotifications.createChannel({
      id: CHANNELS.SADHANA,
      name: 'साधना स्मारक',
      description: 'Daily sadhana reminders',
      importance: 4, // HIGH
      visibility: 1, // PUBLIC
      vibration: true,
    });

    await LocalNotifications.createChannel({
      id: CHANNELS.DAILY,
      name: 'दैनिक विश्लेषण',
      description: 'End of day analysis notification',
      importance: 3, // DEFAULT
      visibility: 1,
      vibration: true,
    });

    await LocalNotifications.createChannel({
      id: CHANNELS.INSPIRE,
      name: 'प्रेरणा',
      description: 'Inspirational quotes and pads',
      importance: 2, // LOW
      visibility: 1,
      vibration: false,
    });
  } catch (err) {
    console.warn('[Notifications] Channel creation failed:', err);
  }
}

/**
 * Cancel all pending notifications.
 * Call before re-scheduling (to avoid duplicates).
 */
export async function cancelAllNotifications() {
  if (!isNative()) return;

  try {
    const pending = await LocalNotifications.getPending();
    if (pending.notifications.length > 0) {
      await LocalNotifications.cancel(pending);
    }
  } catch (err) {
    console.warn('[Notifications] Cancel failed:', err);
  }
}

/**
 * Schedule a single local notification at a specific time today or tomorrow.
 */
async function scheduleAt({ id, title, body, hour, minute = 0, channelId, extra = {} }) {
  const now = new Date();
  const scheduledDate = new Date();
  scheduledDate.setHours(hour, minute, 0, 0);

  // If the time has already passed today, schedule for tomorrow
  if (scheduledDate <= now) {
    scheduledDate.setDate(scheduledDate.getDate() + 1);
  }

  try {
    await LocalNotifications.schedule({
      notifications: [{
        id,
        title,
        body,
        schedule: {
          at: scheduledDate,
          allowWhileIdle: true, // Ensure delivery in Doze mode
        },
        channelId,
        extra,
        smallIcon: 'ic_stat_naam',
        iconColor: '#E8A0BF',
      }],
    });
  } catch (err) {
    console.warn(`[Notifications] Schedule failed for id=${id}:`, err);
  }
}

/**
 * Schedule all 5 daily reminders based on current settings and today's record.
 * 
 * @param {Object} settings - AppSettings from the store
 * @param {Object} todayRecord - Today's DailyRecord
 * @param {string} [quoteText] - Optional quote for encouragement notification
 */
export async function scheduleAllReminders(settings, todayRecord, quoteText) {
  if (!isNative()) return;

  // Cancel existing to avoid duplicates
  await cancelAllNotifications();

  const target = todayRecord?.target || settings.initialTarget;
  const did = todayRecord?.did || 0;
  const remaining = Math.max(0, target - did);

  // ─── 1. Morning Reminder ───
  if (settings.reminderMorningEnabled) {
    const [h, m] = settings.reminderMorningTime.split(':').map(Number);
    await scheduleAt({
      id: 1001,
      title: 'शुभ साधना हो 🙏',
      body: `आज का लक्ष्य: ${formatNumber(target)} नाम जप`,
      hour: h,
      minute: m,
      channelId: CHANNELS.SADHANA,
    });
  }

  // ─── 2. Evening Reminder ───
  if (settings.reminderEveningEnabled) {
    const [h, m] = settings.reminderEveningTime.split(':').map(Number);
    await scheduleAt({
      id: 1002,
      title: 'सायं साधना स्मारक 🌸',
      body: remaining > 0
        ? `${formatNumber(did)} जप हुए, ${formatNumber(remaining)} शेष हैं`
        : `🙏 लक्ष्य पूर्ण! ${formatNumber(did)} नाम जप`,
      hour: h,
      minute: m,
      channelId: CHANNELS.SADHANA,
    });
  }

  // ─── 3. Day-End Reminder ───
  if (settings.reminderDayEndEnabled) {
    const [h, m] = settings.reminderDayEndTime.split(':').map(Number);
    await scheduleAt({
      id: 1003,
      title: 'दिन का विश्लेषण तैयार है',
      body: 'आज की साधना का सारांश देखें',
      hour: h,
      minute: m,
      channelId: CHANNELS.DAILY,
      extra: { action: 'open_day_end' },
    });
  }

  // ─── 4. Progress Reminder (only if > 5000 remaining) ───
  if (settings.reminderProgressEnabled && remaining > 5000) {
    const [h, m] = settings.reminderProgressTime.split(':').map(Number);
    await scheduleAt({
      id: 1004,
      title: 'साधना प्रगति 📿',
      body: `${formatNumber(remaining)} नाम जप शेष हैं`,
      hour: h,
      minute: m,
      channelId: CHANNELS.SADHANA,
    });
  }

  // ─── 5. Encouragement (quote of the day) ───
  if (settings.reminderEncouragementEnabled) {
    await scheduleAt({
      id: 1005,
      title: 'गुरु कृपा केवलं 🙏',
      body: quoteText || 'श्री राधावल्लभ लाल जु की जय',
      hour: 6,
      minute: 0,
      channelId: CHANNELS.INSPIRE,
    });
  }
}

/**
 * Listen for notification tap actions.
 * Returns a cleanup function.
 */
export function onNotificationTap(callback) {
  if (!isNative()) return () => {};

  const listener = LocalNotifications.addListener(
    'localNotificationActionPerformed',
    (event) => {
      callback(event.notification.extra || {});
    }
  );

  return () => listener.remove();
}
