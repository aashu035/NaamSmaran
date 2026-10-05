package com.radhavallabh.naamsmaran.platform.santsmaran

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Notification channels + builders for the "प्रातः संत नाम स्मरण" alarm.
 * UI strings are hardcoded Hindi, consistent with the rest of the app (see ReminderReceiver).
 */
object SantAlarmNotifications {

    private const val TITLE = "प्रातः संत नाम स्मरण"
    private const val TEXT_RINGING = "उठने का समय हो गया — संत नाम स्मरण आरंभ करें 🙏"
    private const val TEXT_MISSED = "आज का संत नाम स्मरण अभी बाकी है — खोलने के लिए छुएँ 🙏"
    private const val ACTION_SNOOZE_LABEL = "10 मिनट बाद"
    private const val ACTION_START_LABEL = "स्मरण आरंभ करें"

    private val immutableUpdate = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Primary channel: the service plays sound + vibration itself, so the channel is silent.
        val alarm = NotificationChannel(
            SantAlarmContract.CHANNEL_ALARM,
            "प्रातः संत नाम स्मरण अलार्म",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "सुबह का संत नाम स्मरण अलार्म"
            setSound(null, null)
            enableVibration(false)
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        // Fallback channel: only used if the foreground service could not start. Rings by itself.
        val fallback = NotificationChannel(
            SantAlarmContract.CHANNEL_ALARM_FALLBACK,
            "प्रातः संत नाम स्मरण अलार्म (वैकल्पिक)",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "सेवा शुरू न हो पाने पर अलार्म की आवाज़"
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 800, 600, 800, 600)
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val missed = NotificationChannel(
            SantAlarmContract.CHANNEL_MISSED,
            "छूटा हुआ संत नाम स्मरण",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "अलार्म बंद हुए बिना बीत जाने पर सूचना"
        }

        manager.createNotificationChannels(listOf(alarm, fallback, missed))
    }

    /** Full-screen "ringing" notification. [channelId] is CHANNEL_ALARM, or the fallback channel. */
    fun ringing(context: Context, channelId: String = SantAlarmContract.CHANNEL_ALARM): Notification {
        val fullScreen = PendingIntent.getActivity(
            context, SantAlarmContract.RC_FULL_SCREEN, SantAlarmIntents.alarmScreen(context), immutableUpdate
        )
        val snooze = PendingIntent.getBroadcast(
            context, SantAlarmContract.RC_NOTIF_SNOOZE, SantAlarmIntents.snooze(context), immutableUpdate
        )
        // Android 12+ blocks starting activities from a broadcast receiver triggered by a
        // notification action, so this action is an Activity PendingIntent.
        val openReading = PendingIntent.getActivity(
            context,
            SantAlarmContract.RC_NOTIF_OPEN_READING,
            SantAlarmIntents.alarmScreenOpenReading(context),
            immutableUpdate
        )

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(TITLE)
            .setContentText(TEXT_RINGING)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setShowWhen(true)
            .setContentIntent(fullScreen)
            .setFullScreenIntent(fullScreen, true)
            .addAction(0, ACTION_SNOOZE_LABEL, snooze)
            .addAction(0, ACTION_START_LABEL, openReading)
            .build()
    }

    /**
     * Last-resort ringing when the foreground service could not start: a full-screen notification
     * on the fallback channel, which plays the alarm sound by itself and repeats until cancelled.
     */
    fun postFallback(context: Context) {
        ensureChannels(context)
        val notification = ringing(context, SantAlarmContract.CHANNEL_ALARM_FALLBACK).apply {
            flags = flags or Notification.FLAG_INSISTENT
        }
        notifySafely(context, SantAlarmContract.NOTIFICATION_ALARM, notification)
    }

    fun postMissed(context: Context) {
        ensureChannels(context)
        notifySafely(context, SantAlarmContract.NOTIFICATION_MISSED, missed(context))
    }

    private fun notifySafely(context: Context, id: Int, notification: Notification) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return // POST_NOTIFICATIONS denied → checklist shows it
        try {
            manager.notify(id, notification)
        } catch (_: SecurityException) {
            // Permission revoked between the check and the call; nothing more can be done here.
        }
    }

    /** Shown when the alarm rang its full timeout without being dismissed. */
    fun missed(context: Context): Notification {
        val open = PendingIntent.getActivity(
            context, SantAlarmContract.RC_MISSED_OPEN, SantAlarmIntents.openReading(context), immutableUpdate
        )
        return NotificationCompat.Builder(context, SantAlarmContract.CHANNEL_MISSED)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(TITLE)
            .setContentText(TEXT_MISSED)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(open)
            .build()
    }
}
