package com.radhavallabh.naamsmaran.platform

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleDailyReminder(hour: Int, minute: Int) {
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextTriggerAt(hour, minute),
            reminderPendingIntent()
        )
    }

    fun cancelDailyReminder() {
        alarmManager.cancel(reminderPendingIntent())
    }

    fun scheduleAutoBackup(hour: Int = 22, minute: Int = 0) {
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextTriggerAt(hour, minute),
            backupPendingIntent()
        )
    }

    fun cancelAutoBackup() {
        alarmManager.cancel(backupPendingIntent())
    }

    private fun reminderPendingIntent(): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            REQUEST_REMINDER,
            Intent(context, ReminderReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun backupPendingIntent(): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            REQUEST_BACKUP,
            Intent(context, AutoBackupReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun nextTriggerAt(hour: Int, minute: Int): Long {
        val now = LocalDateTime.now()
        var next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }
        return next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    companion object {
        private const val REQUEST_REMINDER = 101
        private const val REQUEST_BACKUP = 102

        fun fromContext(context: Context): AppAlarmScheduler =
            AppAlarmScheduler(context.applicationContext)
    }
}
