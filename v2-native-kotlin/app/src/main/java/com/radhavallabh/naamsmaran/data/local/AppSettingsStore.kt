package com.radhavallabh.naamsmaran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.radhavallabh.naamsmaran.data.backup.AppSettingsSnapshot
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AppSettingsStore — DataStore-based preferences.
 * Schema from AGENTS.md §6 — AppSettings.
 */

// Extension property for DataStore
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "naam_smaran_settings")

@Singleton
class AppSettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // ═══════════════════════════════════════════════════════════
    // Keys
    // ═══════════════════════════════════════════════════════════
    private object Keys {
        val ACTIVE_THEME = stringPreferencesKey("active_theme")
        val CHOSEN_MANTRA = stringPreferencesKey("chosen_mantra_text")
        val GREETING_NAME = stringPreferencesKey("deity_display_name")
        val INITIAL_TARGET = longPreferencesKey("initial_target")
        val TARGET_INCREMENT = intPreferencesKey("target_increment")
        val DAY_BOUNDARY_HOUR = intPreferencesKey("day_boundary_hour")
        val USE_INDIAN_NUMBERING = booleanPreferencesKey("use_indian_numbering")
        val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        val DAILY_REMINDER_HOUR = intPreferencesKey("daily_reminder_hour")
        val DAILY_REMINDER_MINUTE = intPreferencesKey("daily_reminder_minute")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        val LAST_BACKUP_AT = longPreferencesKey("last_backup_at")
        val AUDIO_FILENAME = stringPreferencesKey("audio_filename")
        val AUDIO_VOLUME = intPreferencesKey("audio_volume_percent")
        val AUDIO_LOOP = booleanPreferencesKey("audio_loop")
        val BACKGROUND_MODE = stringPreferencesKey("background_mode") // "frames" | "images"
        val GALLERY_IMAGE_URIS = stringPreferencesKey("gallery_image_uris") // pipe-delimited URIs
    }

    // ═══════════════════════════════════════════════════════════
    // Readers (Flow-based for reactive UI)
    // ═══════════════════════════════════════════════════════════
    val activeTheme: Flow<NaamSmaranThemeId> = dataStore.data.map { prefs ->
        val name = prefs[Keys.ACTIVE_THEME] ?: NaamSmaranThemeId.SHARAD_MOON.name
        try { NaamSmaranThemeId.valueOf(name) } catch (_: Exception) { NaamSmaranThemeId.SHARAD_MOON }
    }

    val chosenMantra: Flow<String> = dataStore.data.map { prefs ->
        prefs[Keys.CHOSEN_MANTRA] ?: "राधा"
    }

    val greetingName: Flow<String> = dataStore.data.map { prefs ->
        prefs[Keys.GREETING_NAME] ?: "जय जय श्री हित हरिवंश"
    }

    val initialTarget: Flow<Long> = dataStore.data.map { prefs ->
        prefs[Keys.INITIAL_TARGET] ?: 21600L
    }

    val targetIncrement: Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.TARGET_INCREMENT] ?: 5000
    }

    val dayBoundaryHour: Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.DAY_BOUNDARY_HOUR] ?: 3
    }

    val useIndianNumbering: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.USE_INDIAN_NUMBERING] ?: true
    }

    val dailyReminderEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.DAILY_REMINDER_ENABLED] ?: true
    }

    val dailyReminderHour: Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.DAILY_REMINDER_HOUR] ?: 6
    }

    val dailyReminderMinute: Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.DAILY_REMINDER_MINUTE] ?: 0
    }

    val hapticEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.HAPTIC_ENABLED] ?: true
    }

    val autoBackupEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.AUTO_BACKUP_ENABLED] ?: false
    }

    val lastBackupAt: Flow<Long?> = dataStore.data.map { prefs ->
        prefs[Keys.LAST_BACKUP_AT]
    }

    val backgroundMode: Flow<String> = dataStore.data.map { prefs ->
        prefs[Keys.BACKGROUND_MODE] ?: "images"
    }

    /** User gallery image URIs for the showreel background. */
    val galleryImageUris: Flow<List<String>> = dataStore.data.map { prefs ->
        val raw = prefs[Keys.GALLERY_IMAGE_URIS] ?: ""
        if (raw.isBlank()) emptyList() else raw.split("|")
    }

    // ═══════════════════════════════════════════════════════════
    // Writers
    // ═══════════════════════════════════════════════════════════
    suspend fun setTheme(themeId: NaamSmaranThemeId) {
        dataStore.edit { it[Keys.ACTIVE_THEME] = themeId.name }
    }

    suspend fun setDayBoundaryHour(hour: Int) {
        dataStore.edit { it[Keys.DAY_BOUNDARY_HOUR] = hour.coerceIn(1, 5) }
    }

    suspend fun setTargetIncrement(increment: Int) {
        dataStore.edit { it[Keys.TARGET_INCREMENT] = increment }
    }

    suspend fun setDailyReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.DAILY_REMINDER_ENABLED] = enabled }
    }

    suspend fun setDailyReminderTime(hour: Int, minute: Int) {
        dataStore.edit {
            it[Keys.DAILY_REMINDER_HOUR] = hour.coerceIn(0, 23)
            it[Keys.DAILY_REMINDER_MINUTE] = minute.coerceIn(0, 59)
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.HAPTIC_ENABLED] = enabled }
    }

    suspend fun setAutoBackupEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.AUTO_BACKUP_ENABLED] = enabled }
    }

    suspend fun setLastBackupAt(timestamp: Long?) {
        dataStore.edit {
            if (timestamp == null) {
                it.remove(Keys.LAST_BACKUP_AT)
            } else {
                it[Keys.LAST_BACKUP_AT] = timestamp
            }
        }
    }

    suspend fun setBackgroundMode(mode: String) {
        dataStore.edit { it[Keys.BACKGROUND_MODE] = mode }
    }

    suspend fun setGalleryImageUris(uris: List<String>) {
        dataStore.edit { prefs ->
            prefs[Keys.GALLERY_IMAGE_URIS] = uris.distinct().joinToString("|")
        }
    }

    /** Add a gallery image URI to the showreel. */
    suspend fun addGalleryImageUri(uri: String) {
        dataStore.edit { prefs ->
            val existing = prefs[Keys.GALLERY_IMAGE_URIS] ?: ""
            val uris = if (existing.isBlank()) mutableListOf() else existing.split("|").toMutableList()
            if (!uris.contains(uri)) {
                uris.add(uri)
                prefs[Keys.GALLERY_IMAGE_URIS] = uris.joinToString("|")
            }
        }
    }

    /** Remove a gallery image URI from the showreel. */
    suspend fun removeGalleryImageUri(uri: String) {
        dataStore.edit { prefs ->
            val existing = prefs[Keys.GALLERY_IMAGE_URIS] ?: ""
            val uris = existing.split("|").toMutableList()
            uris.remove(uri)
            prefs[Keys.GALLERY_IMAGE_URIS] = uris.joinToString("|")
        }
    }

    suspend fun snapshot(): AppSettingsSnapshot {
        val prefs = dataStore.data.first()
        return AppSettingsSnapshot(
            activeTheme = prefs[Keys.ACTIVE_THEME] ?: NaamSmaranThemeId.SHARAD_MOON.name,
            initialTarget = prefs[Keys.INITIAL_TARGET] ?: 21_600L,
            targetIncrement = prefs[Keys.TARGET_INCREMENT] ?: 5_000,
            dayBoundaryHour = prefs[Keys.DAY_BOUNDARY_HOUR] ?: 3,
            dailyReminderEnabled = prefs[Keys.DAILY_REMINDER_ENABLED] ?: true,
            dailyReminderHour = prefs[Keys.DAILY_REMINDER_HOUR] ?: 6,
            dailyReminderMinute = prefs[Keys.DAILY_REMINDER_MINUTE] ?: 0,
            hapticEnabled = prefs[Keys.HAPTIC_ENABLED] ?: true,
            autoBackupEnabled = prefs[Keys.AUTO_BACKUP_ENABLED] ?: false,
            lastBackupAt = prefs[Keys.LAST_BACKUP_AT],
            galleryImageUris = (prefs[Keys.GALLERY_IMAGE_URIS] ?: "")
                .split("|")
                .filter { it.isNotBlank() }
        )
    }

    suspend fun restoreFromSnapshot(snapshot: AppSettingsSnapshot) {
        dataStore.edit { prefs ->
            prefs[Keys.ACTIVE_THEME] = snapshot.activeTheme
            prefs[Keys.INITIAL_TARGET] = snapshot.initialTarget
            prefs[Keys.TARGET_INCREMENT] = snapshot.targetIncrement
            prefs[Keys.DAY_BOUNDARY_HOUR] = snapshot.dayBoundaryHour.coerceIn(1, 5)
            prefs[Keys.DAILY_REMINDER_ENABLED] = snapshot.dailyReminderEnabled
            prefs[Keys.DAILY_REMINDER_HOUR] = snapshot.dailyReminderHour.coerceIn(0, 23)
            prefs[Keys.DAILY_REMINDER_MINUTE] = snapshot.dailyReminderMinute.coerceIn(0, 59)
            prefs[Keys.HAPTIC_ENABLED] = snapshot.hapticEnabled
            prefs[Keys.AUTO_BACKUP_ENABLED] = snapshot.autoBackupEnabled
            if (snapshot.lastBackupAt == null) {
                prefs.remove(Keys.LAST_BACKUP_AT)
            } else {
                prefs[Keys.LAST_BACKUP_AT] = snapshot.lastBackupAt
            }
            prefs[Keys.GALLERY_IMAGE_URIS] = snapshot.galleryImageUris.distinct().joinToString("|")
        }
    }
}
