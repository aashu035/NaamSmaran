package com.radhavallabh.naamsmaran.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.radhavallabh.naamsmaran.data.backup.AppSettingsSnapshot
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AppSettingsStore — EncryptedSharedPreferences-based preferences.
 * Schema from AGENTS.md §6 — AppSettings.
 * Adheres to ECC Standards by encrypting sensitive settings.
 */

@Singleton
class AppSettingsStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "naam_smaran_settings_encrypted",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // ═══════════════════════════════════════════════════════════
    // Keys
    // ═══════════════════════════════════════════════════════════
    private object Keys {
        const val ACTIVE_THEME = "active_theme"
        const val CHOSEN_MANTRA = "chosen_mantra_text"
        const val GREETING_NAME = "deity_display_name"
        const val INITIAL_TARGET = "initial_target"
        const val TARGET_INCREMENT = "target_increment"
        const val DAY_BOUNDARY_HOUR = "day_boundary_hour"
        const val USE_INDIAN_NUMBERING = "use_indian_numbering"
        const val DAILY_REMINDER_ENABLED = "daily_reminder_enabled"
        const val DAILY_REMINDER_HOUR = "daily_reminder_hour"
        const val DAILY_REMINDER_MINUTE = "daily_reminder_minute"
        const val HAPTIC_ENABLED = "haptic_enabled"
        const val AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        const val LAST_BACKUP_AT = "last_backup_at"
        const val BACKGROUND_MODE = "background_mode"
        const val GALLERY_IMAGE_URIS = "gallery_image_uris"
    }

    private fun <T> getFlow(key: String, defaultValue: T, getter: () -> T): Flow<T> = callbackFlow {
        // Emit initial value
        trySend(getter())

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(getter())
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // ═══════════════════════════════════════════════════════════
    // Readers (Flow-based for reactive UI)
    // ═══════════════════════════════════════════════════════════
    val activeTheme: Flow<NaamSmaranThemeId> = getFlow(Keys.ACTIVE_THEME, NaamSmaranThemeId.SHARAD_MOON) {
        val name = prefs.getString(Keys.ACTIVE_THEME, NaamSmaranThemeId.SHARAD_MOON.name) ?: NaamSmaranThemeId.SHARAD_MOON.name
        try { NaamSmaranThemeId.valueOf(name) } catch (_: Exception) { NaamSmaranThemeId.SHARAD_MOON }
    }

    val chosenMantra: Flow<String> = getFlow(Keys.CHOSEN_MANTRA, "राधा") {
        prefs.getString(Keys.CHOSEN_MANTRA, "राधा") ?: "राधा"
    }

    val greetingName: Flow<String> = getFlow(Keys.GREETING_NAME, "जय जय श्री हित हरिवंश") {
        prefs.getString(Keys.GREETING_NAME, "जय जय श्री हित हरिवंश") ?: "जय जय श्री हित हरिवंश"
    }

    val initialTarget: Flow<Long> = getFlow(Keys.INITIAL_TARGET, 21600L) {
        prefs.getLong(Keys.INITIAL_TARGET, 21600L)
    }

    val targetIncrement: Flow<Int> = getFlow(Keys.TARGET_INCREMENT, 5000) {
        val raw = prefs.getInt(Keys.TARGET_INCREMENT, 5000)
        if (raw == 1000) 5000 else raw
    }

    val dayBoundaryHour: Flow<Int> = getFlow(Keys.DAY_BOUNDARY_HOUR, 3) {
        prefs.getInt(Keys.DAY_BOUNDARY_HOUR, 3)
    }

    val useIndianNumbering: Flow<Boolean> = getFlow(Keys.USE_INDIAN_NUMBERING, true) {
        prefs.getBoolean(Keys.USE_INDIAN_NUMBERING, true)
    }

    val dailyReminderEnabled: Flow<Boolean> = getFlow(Keys.DAILY_REMINDER_ENABLED, true) {
        prefs.getBoolean(Keys.DAILY_REMINDER_ENABLED, true)
    }

    val dailyReminderHour: Flow<Int> = getFlow(Keys.DAILY_REMINDER_HOUR, 6) {
        prefs.getInt(Keys.DAILY_REMINDER_HOUR, 6)
    }

    val dailyReminderMinute: Flow<Int> = getFlow(Keys.DAILY_REMINDER_MINUTE, 0) {
        prefs.getInt(Keys.DAILY_REMINDER_MINUTE, 0)
    }

    val hapticEnabled: Flow<Boolean> = getFlow(Keys.HAPTIC_ENABLED, true) {
        prefs.getBoolean(Keys.HAPTIC_ENABLED, true)
    }

    val autoBackupEnabled: Flow<Boolean> = getFlow(Keys.AUTO_BACKUP_ENABLED, false) {
        prefs.getBoolean(Keys.AUTO_BACKUP_ENABLED, false)
    }

    val lastBackupAt: Flow<Long?> = getFlow(Keys.LAST_BACKUP_AT, null as Long?) {
        if (prefs.contains(Keys.LAST_BACKUP_AT)) prefs.getLong(Keys.LAST_BACKUP_AT, 0L) else null
    }

    val backgroundMode: Flow<String> = getFlow(Keys.BACKGROUND_MODE, "images") {
        prefs.getString(Keys.BACKGROUND_MODE, "images") ?: "images"
    }

    val galleryImageUris: Flow<List<String>> = getFlow(Keys.GALLERY_IMAGE_URIS, emptyList()) {
        val raw = prefs.getString(Keys.GALLERY_IMAGE_URIS, "") ?: ""
        if (raw.isBlank()) emptyList() else raw.split("|")
    }

    // ═══════════════════════════════════════════════════════════
    // Writers
    // ═══════════════════════════════════════════════════════════
    suspend fun setTheme(themeId: NaamSmaranThemeId) {
        prefs.edit().putString(Keys.ACTIVE_THEME, themeId.name).apply()
    }

    suspend fun setDayBoundaryHour(hour: Int) {
        prefs.edit().putInt(Keys.DAY_BOUNDARY_HOUR, hour.coerceIn(1, 5)).apply()
    }

    suspend fun setTargetIncrement(increment: Int) {
        prefs.edit().putInt(Keys.TARGET_INCREMENT, increment).apply()
    }

    suspend fun setDailyReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.DAILY_REMINDER_ENABLED, enabled).apply()
    }

    suspend fun setDailyReminderTime(hour: Int, minute: Int) {
        prefs.edit()
            .putInt(Keys.DAILY_REMINDER_HOUR, hour.coerceIn(0, 23))
            .putInt(Keys.DAILY_REMINDER_MINUTE, minute.coerceIn(0, 59))
            .apply()
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.HAPTIC_ENABLED, enabled).apply()
    }

    suspend fun setAutoBackupEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.AUTO_BACKUP_ENABLED, enabled).apply()
    }

    suspend fun setLastBackupAt(timestamp: Long?) {
        if (timestamp == null) {
            prefs.edit().remove(Keys.LAST_BACKUP_AT).apply()
        } else {
            prefs.edit().putLong(Keys.LAST_BACKUP_AT, timestamp).apply()
        }
    }

    suspend fun setBackgroundMode(mode: String) {
        prefs.edit().putString(Keys.BACKGROUND_MODE, mode).apply()
    }

    suspend fun setGalleryImageUris(uris: List<String>) {
        prefs.edit().putString(Keys.GALLERY_IMAGE_URIS, uris.distinct().joinToString("|")).apply()
    }

    suspend fun addGalleryImageUri(uri: String) {
        val existing = prefs.getString(Keys.GALLERY_IMAGE_URIS, "") ?: ""
        val uris = if (existing.isBlank()) mutableListOf() else existing.split("|").toMutableList()
        if (!uris.contains(uri)) {
            uris.add(uri)
            prefs.edit().putString(Keys.GALLERY_IMAGE_URIS, uris.joinToString("|")).apply()
        }
    }

    suspend fun removeGalleryImageUri(uri: String) {
        val existing = prefs.getString(Keys.GALLERY_IMAGE_URIS, "") ?: ""
        val uris = existing.split("|").toMutableList()
        uris.remove(uri)
        prefs.edit().putString(Keys.GALLERY_IMAGE_URIS, uris.joinToString("|")).apply()
    }

    suspend fun snapshot(): AppSettingsSnapshot {
        return AppSettingsSnapshot(
            activeTheme = prefs.getString(Keys.ACTIVE_THEME, NaamSmaranThemeId.SHARAD_MOON.name) ?: NaamSmaranThemeId.SHARAD_MOON.name,
            initialTarget = prefs.getLong(Keys.INITIAL_TARGET, 21_600L),
            targetIncrement = prefs.getInt(Keys.TARGET_INCREMENT, 5_000),
            dayBoundaryHour = prefs.getInt(Keys.DAY_BOUNDARY_HOUR, 3),
            dailyReminderEnabled = prefs.getBoolean(Keys.DAILY_REMINDER_ENABLED, true),
            dailyReminderHour = prefs.getInt(Keys.DAILY_REMINDER_HOUR, 6),
            dailyReminderMinute = prefs.getInt(Keys.DAILY_REMINDER_MINUTE, 0),
            hapticEnabled = prefs.getBoolean(Keys.HAPTIC_ENABLED, true),
            autoBackupEnabled = prefs.getBoolean(Keys.AUTO_BACKUP_ENABLED, false),
            lastBackupAt = if (prefs.contains(Keys.LAST_BACKUP_AT)) prefs.getLong(Keys.LAST_BACKUP_AT, 0L) else null,
            galleryImageUris = (prefs.getString(Keys.GALLERY_IMAGE_URIS, "") ?: "")
                .split("|")
                .filter { it.isNotBlank() }
        )
    }

    suspend fun restoreFromSnapshot(snapshot: AppSettingsSnapshot) {
        prefs.edit()
            .putString(Keys.ACTIVE_THEME, snapshot.activeTheme)
            .putLong(Keys.INITIAL_TARGET, snapshot.initialTarget)
            .putInt(Keys.TARGET_INCREMENT, snapshot.targetIncrement)
            .putInt(Keys.DAY_BOUNDARY_HOUR, snapshot.dayBoundaryHour.coerceIn(1, 5))
            .putBoolean(Keys.DAILY_REMINDER_ENABLED, snapshot.dailyReminderEnabled)
            .putInt(Keys.DAILY_REMINDER_HOUR, snapshot.dailyReminderHour.coerceIn(0, 23))
            .putInt(Keys.DAILY_REMINDER_MINUTE, snapshot.dailyReminderMinute.coerceIn(0, 59))
            .putBoolean(Keys.HAPTIC_ENABLED, snapshot.hapticEnabled)
            .putBoolean(Keys.AUTO_BACKUP_ENABLED, snapshot.autoBackupEnabled)
            .apply {
                if (snapshot.lastBackupAt == null) {
                    remove(Keys.LAST_BACKUP_AT)
                } else {
                    putLong(Keys.LAST_BACKUP_AT, snapshot.lastBackupAt)
                }
            }
        prefs.edit().putString(Keys.GALLERY_IMAGE_URIS, snapshot.galleryImageUris.distinct().joinToString("|")).apply()
    }
}
