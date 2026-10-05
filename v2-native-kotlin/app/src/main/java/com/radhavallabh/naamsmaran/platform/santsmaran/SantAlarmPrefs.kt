package com.radhavallabh.naamsmaran.platform.santsmaran

import android.content.Context
import android.content.SharedPreferences

/**
 * Defaults for the "प्रातः संत नाम स्मरण" alarm.
 * Must equal `alarm.hour/minute` in assets/sant_smaran.json (enforced by SantAlarmTimeTest);
 * duplicated here so receivers never have to parse the JSON at boot.
 */
object SantAlarmDefaults {
    const val HOUR = 4
    const val MINUTE = 0
    const val ENABLED = true
}

/**
 * Alarm configuration: enabled flag + time of day.
 *
 * Deliberately NOT in [com.radhavallabh.naamsmaran.data.local.AppSettingsStore]:
 * that store is EncryptedSharedPreferences (AndroidKeystore, credential-encrypted), which cannot
 * be opened before the first unlock after a reboot. This alarm must be rescheduled on
 * LOCKED_BOOT_COMPLETED, so its tiny, non-sensitive config lives in **device-protected** storage.
 *
 * Not part of the app backup payload (it is a per-device wake-up setting).
 */
class SantAlarmPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext
            .createDeviceProtectedStorageContext()
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var enabled: Boolean
        get() = prefs.getBoolean(KEY_ENABLED, SantAlarmDefaults.ENABLED)
        set(value) = prefs.edit().putBoolean(KEY_ENABLED, value).apply()

    val hour: Int
        get() = prefs.getInt(KEY_HOUR, SantAlarmDefaults.HOUR).coerceIn(0, 23)

    val minute: Int
        get() = prefs.getInt(KEY_MINUTE, SantAlarmDefaults.MINUTE).coerceIn(0, 59)

    fun setTime(hour: Int, minute: Int) {
        prefs.edit()
            .putInt(KEY_HOUR, hour.coerceIn(0, 23))
            .putInt(KEY_MINUTE, minute.coerceIn(0, 59))
            .apply()
    }

    private companion object {
        const val FILE_NAME = "sant_smaran_alarm"
        const val KEY_ENABLED = "enabled"
        const val KEY_HOUR = "hour"
        const val KEY_MINUTE = "minute"
    }
}
