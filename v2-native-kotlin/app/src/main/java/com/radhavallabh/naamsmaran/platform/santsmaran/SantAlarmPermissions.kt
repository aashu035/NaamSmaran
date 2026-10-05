package com.radhavallabh.naamsmaran.platform.santsmaran

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

/** Which of the system conditions the alarm needs are currently satisfied. */
data class SantAlarmReadiness(
    val notificationsAllowed: Boolean,
    val exactAlarmAllowed: Boolean,
    val fullScreenIntentAllowed: Boolean,
    val batteryUnrestricted: Boolean
) {
    /** The three conditions without which the alarm cannot ring/show properly. */
    val requiredOk: Boolean get() = notificationsAllowed && exactAlarmAllowed && fullScreenIntentAllowed
}

/**
 * Reads and deep-links the Android settings the "प्रातः संत नाम स्मरण" alarm depends on.
 * All checks are cheap and are re-read each time the Settings screen resumes.
 */
object SantAlarmPermissions {

    fun read(context: Context): SantAlarmReadiness {
        val app = context.applicationContext
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val powerManager = app.getSystemService(Context.POWER_SERVICE) as PowerManager

        return SantAlarmReadiness(
            notificationsAllowed = NotificationManagerCompat.from(app).areNotificationsEnabled(),
            exactAlarmAllowed =
                Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms(),
            fullScreenIntentAllowed =
                Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE ||
                    notificationManager.canUseFullScreenIntent(),
            batteryUnrestricted = powerManager.isIgnoringBatteryOptimizations(app.packageName)
        )
    }

    private fun packageUri(context: Context): Uri = Uri.parse("package:${context.packageName}")

    /** Android 12/12L only matters in practice (13+ grants USE_EXACT_ALARM); harmless elsewhere. */
    fun exactAlarmSettings(context: Context): Intent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, packageUri(context))
        } else {
            appDetailsSettings(context)
        }

    fun fullScreenIntentSettings(context: Context): Intent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT, packageUri(context))
        } else {
            appDetailsSettings(context)
        }

    fun notificationSettings(context: Context): Intent =
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)

    /** The system battery-optimisation list (no extra permission needed, unlike the direct prompt). */
    fun batteryOptimizationSettings(): Intent =
        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)

    fun appDetailsSettings(context: Context): Intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri(context))

    /** Per-OEM "autostart / background kill" guide (Xiaomi, Realme, Oppo, Vivo …). */
    fun oemGuideUrl(): Uri = Uri.parse("https://dontkillmyapp.com")
}
