package com.radhavallabh.naamsmaran.platform.santsmaran

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.radhavallabh.naamsmaran.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

/** Outcome of an attempt to (re)schedule the alarm. */
enum class SantScheduleResult {
    SCHEDULED,
    /** Alarm is switched off in settings (any pending alarm was cancelled). */
    DISABLED,
    /** The system refused exact alarms (Android 12/12L permission revoked) — show the checklist. */
    EXACT_ALARM_DENIED
}

/**
 * Schedules the daily "प्रातः संत नाम स्मरण" alarm.
 *
 * Uses [AlarmManager.setAlarmClock]: it fires on time even in Doze, is shown as the system's
 * "next alarm", and is what real clock apps use. (`setExactAndAllowWhileIdle` is throttled in
 * Doze and WorkManager gives no exact-time guarantee — neither is used here.)
 *
 * Stateless apart from [SantAlarmPrefs]; receivers use [fromContext] like the existing
 * AppAlarmScheduler, while screens get it from Hilt.
 */
@Singleton
class SantAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val prefs = SantAlarmPrefs(context)

    val isEnabled: Boolean get() = prefs.enabled
    val hour: Int get() = prefs.hour
    val minute: Int get() = prefs.minute

    /** Android 12/12L can revoke the exact-alarm permission; 13+ grants USE_EXACT_ALARM. */
    fun canScheduleExactAlarms(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    fun setEnabled(enabled: Boolean): SantScheduleResult {
        prefs.enabled = enabled
        return syncWithPrefs()
    }

    fun setTime(hour: Int, minute: Int): SantScheduleResult {
        prefs.setTime(hour, minute)
        return syncWithPrefs()
    }

    /**
     * Makes the system alarm match the saved config. Idempotent: re-scheduling replaces the
     * existing alarm (same PendingIntent). Called on app start, boot, time/zone change,
     * package update, permission change, and right after each ring (tomorrow's alarm).
     */
    fun syncWithPrefs(now: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())): SantScheduleResult {
        if (!prefs.enabled) {
            cancelDaily()
            return SantScheduleResult.DISABLED
        }
        val next = SantAlarmTime.nextTriggerAt(now, prefs.hour, prefs.minute)
        return schedule(SantAlarmContract.RC_DAILY, SantAlarmContract.KIND_DAILY, next.toInstant().toEpochMilli())
    }

    /** Epoch millis of the next daily ring, or null when disabled. For the settings card. */
    fun nextDailyTriggerMillis(now: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())): Long? =
        if (prefs.enabled) SantAlarmTime.nextTriggerAt(now, prefs.hour, prefs.minute).toInstant().toEpochMilli() else null

    fun cancelDaily() {
        alarmManager.cancel(firePendingIntent(SantAlarmContract.RC_DAILY, SantAlarmContract.KIND_DAILY))
    }

    fun scheduleSnooze(minutes: Int = SantAlarmContract.SNOOZE_MINUTES): SantScheduleResult =
        schedule(
            SantAlarmContract.RC_SNOOZE,
            SantAlarmContract.KIND_SNOOZE,
            System.currentTimeMillis() + minutes * 60_000L
        )

    fun cancelSnooze() {
        alarmManager.cancel(firePendingIntent(SantAlarmContract.RC_SNOOZE, SantAlarmContract.KIND_SNOOZE))
    }

    /** One-off alarm a few seconds from now, to try the whole ring flow (lock the phone first). */
    fun scheduleTest(seconds: Int = SantAlarmContract.TEST_DELAY_SECONDS): SantScheduleResult =
        schedule(
            SantAlarmContract.RC_TEST,
            SantAlarmContract.KIND_TEST,
            System.currentTimeMillis() + seconds * 1_000L
        )

    private fun schedule(requestCode: Int, kind: String, triggerAtMillis: Long): SantScheduleResult =
        try {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(triggerAtMillis, showAppPendingIntent()),
                firePendingIntent(requestCode, kind)
            )
            SantScheduleResult.SCHEDULED
        } catch (e: SecurityException) {
            // Exact-alarm permission revoked (Android 12/12L). Surfaced in the Settings checklist.
            SantScheduleResult.EXACT_ALARM_DENIED
        }

    private fun firePendingIntent(requestCode: Int, kind: String): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            SantAlarmIntents.fire(context, kind),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    /** What the system shows when the user taps the "next alarm" indicator. */
    private fun showAppPendingIntent(): PendingIntent =
        PendingIntent.getActivity(
            context,
            SantAlarmContract.RC_SHOW_APP,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    companion object {
        fun fromContext(context: Context): SantAlarmScheduler =
            SantAlarmScheduler(context.applicationContext)
    }
}
