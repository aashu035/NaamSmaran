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
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
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
        prefs[Keys.GREETING_NAME] ?: "राधे राधे"
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

    suspend fun setBackgroundMode(mode: String) {
        dataStore.edit { it[Keys.BACKGROUND_MODE] = mode }
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
}
