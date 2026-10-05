package com.radhavallabh.naamsmaran.platform.santsmaran

import android.content.Context
import android.content.Intent
import com.radhavallabh.naamsmaran.MainActivity

/**
 * Shared constants + intent factories for the "प्रातः संत नाम स्मरण" alarm.
 *
 * Flow: AlarmManager.setAlarmClock → [SantAlarmReceiver] → [SantAlarmService] (sound + vibration +
 * full-screen notification) → [SantAlarmActivity] (Snooze / Dismiss) → MainActivity reading screen.
 */
object SantAlarmContract {

    private const val PKG = "com.radhavallabh.naamsmaran.santsmaran"

    // ── Broadcast / activity actions ────────────────────────────────────────
    const val ACTION_FIRE = "$PKG.ACTION_FIRE"
    const val ACTION_SNOOZE = "$PKG.ACTION_SNOOZE"
    const val ACTION_STOP = "$PKG.ACTION_STOP"
    /** Handled by SantAlarmActivity: silence the alarm, then open the reading screen. */
    const val ACTION_OPEN_READING = "$PKG.ACTION_OPEN_READING"

    // ── Extras ──────────────────────────────────────────────────────────────
    const val EXTRA_KIND = "$PKG.EXTRA_KIND"
    const val KIND_DAILY = "daily"
    const val KIND_SNOOZE = "snooze"
    const val KIND_TEST = "test"

    /** MainActivity extra: navigate straight to the reading screen. */
    const val EXTRA_OPEN_SANT_SMARAN = "$PKG.EXTRA_OPEN_SANT_SMARAN"

    // ── PendingIntent request codes ─────────────────────────────────────────
    const val RC_DAILY = 4001
    const val RC_SNOOZE = 4002
    const val RC_TEST = 4003
    const val RC_SHOW_APP = 4004
    const val RC_FULL_SCREEN = 4005
    const val RC_NOTIF_SNOOZE = 4006
    const val RC_NOTIF_OPEN_READING = 4007
    const val RC_MISSED_OPEN = 4008

    // ── Notifications ───────────────────────────────────────────────────────
    const val CHANNEL_ALARM = "sant_smaran_alarm"
    /** Used only if the foreground service cannot start: this channel plays the alarm sound itself. */
    const val CHANNEL_ALARM_FALLBACK = "sant_smaran_alarm_fallback"
    const val CHANNEL_MISSED = "sant_smaran_missed"
    const val NOTIFICATION_ALARM = 4101
    const val NOTIFICATION_MISSED = 4102

    // ── Behaviour ───────────────────────────────────────────────────────────
    const val SNOOZE_MINUTES = 10
    const val TEST_DELAY_SECONDS = 10
    /** Ring for at most this long, then fall back to a "missed" notification. */
    const val RING_TIMEOUT_MS = 10 * 60 * 1000L
}

/** Intent factories used by the alarm components. */
object SantAlarmIntents {

    /** Opens the app straight on the reading screen (single instance, reuses the task). */
    fun openReading(context: Context): Intent =
        Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra(SantAlarmContract.EXTRA_OPEN_SANT_SMARAN, true)

    fun fire(context: Context, kind: String): Intent =
        Intent(context, SantAlarmReceiver::class.java)
            .setAction(SantAlarmContract.ACTION_FIRE)
            .putExtra(SantAlarmContract.EXTRA_KIND, kind)

    fun snooze(context: Context): Intent =
        Intent(context, SantAlarmReceiver::class.java).setAction(SantAlarmContract.ACTION_SNOOZE)

    fun stop(context: Context): Intent =
        Intent(context, SantAlarmReceiver::class.java).setAction(SantAlarmContract.ACTION_STOP)

    /** Alarm screen (shown over the lock screen while ringing). */
    fun alarmScreen(context: Context): Intent =
        Intent(context, SantAlarmActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

    /** Alarm screen in "silence + open reading" mode (used by the notification's action button). */
    fun alarmScreenOpenReading(context: Context): Intent =
        alarmScreen(context).setAction(SantAlarmContract.ACTION_OPEN_READING)
}
