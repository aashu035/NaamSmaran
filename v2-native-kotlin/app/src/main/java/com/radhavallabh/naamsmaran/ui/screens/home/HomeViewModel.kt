package com.radhavallabh.naamsmaran.ui.screens.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.data.repository.JapRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.radhavallabh.naamsmaran.debug.AgentDebugLog
import javax.inject.Inject

/**
 * HomeViewModel — manages the Zero-UI Darshan home screen state.
 *
 * Counter visibility logic (Merge A+C):
 * - Counter is VISIBLE after any jap count action.
 * - Counter AUTO-HIDES after [COUNTER_VISIBLE_MS] ms of inactivity.
 * - Counter re-appears for [COUNTER_VISIBLE_MS] ms after the bottom sheet closes.
 *
 * Swipe hint logic:
 * - Shows for [HINT_VISIBLE_MS] ms on first app launch, then fades out.
 * - Resets each session (cold start).
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: JapRepository,
    private val settingsStore: AppSettingsStore,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    companion object {
        /** How long the counter stays visible after an interaction (ms). */
        private const val COUNTER_VISIBLE_MS = 3_500L
        /** How long the swipe-up hint stays visible on cold start (ms). */
        private const val HINT_VISIBLE_MS = 5_000L
    }

    // ── Today's record (from Room, live) ────────────────────────────────────
    val todayRecord: StateFlow<DailyRecord?> = repository.getTodayRecord()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // ── Counter visibility ───────────────────────────────────────────────────
    private val _counterVisible = MutableStateFlow(false)
    val counterVisible: StateFlow<Boolean> = _counterVisible.asStateFlow()

    private var hideJob: Job? = null

    // ── Chanting active (for sliding to corner) ──────────────────────────────
    private val _chantingActive = MutableStateFlow(false)
    val chantingActive: StateFlow<Boolean> = _chantingActive.asStateFlow()

    private var chantingJob: Job? = null

    // ── Swipe-up hint visibility ─────────────────────────────────────────────
    private val _hintVisible = MutableStateFlow(true)
    val hintVisible: StateFlow<Boolean> = _hintVisible.asStateFlow()

    private var hintDismissJob: Job? = null

    val greetingName: StateFlow<String> = settingsStore.greetingName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "राधे राधे"
        )

    val hapticEnabled: StateFlow<Boolean> = settingsStore.hapticEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    // ── Gallery image URIs (reactive, from DataStore) ────────────────────────
    val galleryState: StateFlow<GalleryUiState> = settingsStore.galleryImageUris
        .mapLatest { uriStrings ->
            // C1 fix: isReadableUri opens ContentResolver streams — must run on IO thread.
            withContext(Dispatchers.IO) {
                val parsed = uriStrings.mapNotNull { raw ->
                    runCatching { Uri.parse(raw) }.getOrNull()
                }
                val readableUris = parsed.filter(::isReadableUri)
                GalleryUiState(
                    uris = readableUris,
                    isLoaded = true
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GalleryUiState()
        )

    init {
        // Ensure today's record is seeded from the engine
        viewModelScope.launch {
            repository.ensureTodayRecord()
        }

        // C1 fix: URI validation (isReadableUri) does IO — launch on Dispatchers.IO.
        viewModelScope.launch(Dispatchers.IO) {
            settingsStore.galleryImageUris.collect { uriStrings ->
                val valid = uriStrings.mapNotNull { raw ->
                    runCatching { Uri.parse(raw) }.getOrNull()?.takeIf(::isReadableUri)
                }
                if (valid.size != uriStrings.size) {
                    settingsStore.setGalleryImageUris(valid.map(Uri::toString))
                }
            }
        }

        // Auto-dismiss swipe hint after 5 seconds
        hintDismissJob = viewModelScope.launch {
            delay(HINT_VISIBLE_MS)
            _hintVisible.value = false
        }
    }

    /** Add [count] naam-jap repetitions and flash the counter. */
    fun addJap(count: Long, isMainScreenTap: Boolean = false) {
        // #region agent log
        AgentDebugLog.log(
            location = "HomeViewModel.kt:addJap",
            message = "addJap_called",
            hypothesisId = "H2",
            data = mapOf("count" to count, "counterVisibleBefore" to _counterVisible.value)
        )
        // #endregion
        viewModelScope.launch {
            repository.addJapCount(count)
            flashCounter()
        }
        // Dismiss hint immediately on any interaction
        dismissHint()

        if (isMainScreenTap) {
            _chantingActive.value = true
            chantingJob?.cancel()
            chantingJob = viewModelScope.launch {
                delay(5000L) // 5 seconds of inactivity
                _chantingActive.value = false
            }
        }
    }

    /** Add [count] mala to today's total (Track A). */
    fun addMala(count: Long) {
        viewModelScope.launch {
            repository.addMalaCount(count)
        }
        // Dismiss hint immediately on any interaction
        dismissHint()
    }

    /**
     * Called when the bottom sheet is dismissed.
     * Re-shows the counter for [COUNTER_VISIBLE_MS] ms.
     */
    fun onBottomSheetDismissed() {
        // #region agent log
        AgentDebugLog.log(
            location = "HomeViewModel.kt:onBottomSheetDismissed",
            message = "sheet_dismissed_vm",
            hypothesisId = "H1",
            data = mapOf("counterVisibleBefore" to _counterVisible.value)
        )
        // #endregion
        flashCounter()
    }

    /** Called when the user starts swiping up — dismiss hint immediately. */
    fun dismissHint() {
        hintDismissJob?.cancel()
        _hintVisible.value = false
    }

    /** Add a gallery image URI to the showreel. */
    fun addGalleryImage(uri: Uri) {
        viewModelScope.launch {
            settingsStore.addGalleryImageUri(uri.toString())
        }
    }

    /** Remove a gallery image URI from the showreel. */
    fun removeGalleryImage(uri: Uri) {
        viewModelScope.launch {
            settingsStore.removeGalleryImageUri(uri.toString())
        }
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    /**
     * Show the counter and schedule its auto-hide.
     * Cancels any pending hide before re-scheduling.
     */
    private fun flashCounter() {
        _counterVisible.value = true
        hideJob?.cancel()
        hideJob = viewModelScope.launch {
            delay(COUNTER_VISIBLE_MS)
            _counterVisible.value = false
        }
    }

    private fun isReadableUri(uri: Uri): Boolean = runCatching {
        appContext.contentResolver.openInputStream(uri)?.use { input -> input.read() }
        true
    }.getOrDefault(false)
}

data class GalleryUiState(
    val uris: List<Uri> = emptyList(),
    val isLoaded: Boolean = false
)
