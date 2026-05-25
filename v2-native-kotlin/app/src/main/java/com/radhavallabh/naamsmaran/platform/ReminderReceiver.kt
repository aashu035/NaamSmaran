package com.radhavallabh.naamsmaran.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.settingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val appContext = context.applicationContext
        val settingsStore = AppSettingsStore(appContext.settingsDataStore)
        val scheduler = AppAlarmScheduler.fromContext(appContext)

        runBlocking {
            val enabled = settingsStore.dailyReminderEnabled.first()
            val hour = settingsStore.dailyReminderHour.first()
            val minute = settingsStore.dailyReminderMinute.first()

            if (!enabled) {
                scheduler.cancelDailyReminder()
                return@runBlocking
            }

            ensureChannel(appContext)

            if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED ||
                android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU
            ) {
                NotificationManagerCompat.from(appContext).notify(
                    201,
                    NotificationCompat.Builder(appContext, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("दैनिक स्मरण")
                        .setContentText("आज का नाम जप स्मरण करें। शुभ साधना हो 🙏")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build()
                )
            }

            scheduler.scheduleDailyReminder(hour, minute)
        }
    }

    private fun ensureChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Naam Smaran Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "दैनिक स्मरण और बैकअप सूचनाएँ"
        }
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "naam_smaran_daily"
    }
}
