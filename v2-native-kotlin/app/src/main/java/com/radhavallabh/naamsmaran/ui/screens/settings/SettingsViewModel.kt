package com.radhavallabh.naamsmaran.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radhavallabh.naamsmaran.data.backup.AppBackupManager
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.platform.AppAlarmScheduler
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val activeTheme: NaamSmaranThemeId = NaamSmaranThemeId.SHARAD_MOON,
    val dailyReminderEnabled: Boolean = true,
    val dailyReminderHour: Int = 6,
    val dailyReminderMinute: Int = 0,
    val hapticEnabled: Boolean = true,
    val autoBackupEnabled: Boolean = false,
    val initialTarget: Long = 21_600L,
    val targetIncrement: Int = 5_000,
    val lastBackupAt: Long? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStore: AppSettingsStore,
    private val scheduler: AppAlarmScheduler,
    private val backupManager: AppBackupManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        combine(
            settingsStore.activeTheme,
            settingsStore.dailyReminderEnabled,
            settingsStore.dailyReminderHour,
            settingsStore.dailyReminderMinute,
            settingsStore.hapticEnabled
        ) { activeTheme, dailyReminderEnabled, dailyReminderHour, dailyReminderMinute, hapticEnabled ->
            SettingsUiState(
                activeTheme = activeTheme,
                dailyReminderEnabled = dailyReminderEnabled,
                dailyReminderHour = dailyReminderHour,
                dailyReminderMinute = dailyReminderMinute,
                hapticEnabled = hapticEnabled
            )
        },
        settingsStore.autoBackupEnabled,
        settingsStore.initialTarget,
        settingsStore.targetIncrement,
        settingsStore.lastBackupAt
    ) { baseState, autoBackupEnabled, initialTarget, targetIncrement, lastBackupAt ->
        baseState.copy(
            autoBackupEnabled = autoBackupEnabled,
            initialTarget = initialTarget,
            targetIncrement = targetIncrement,
            lastBackupAt = lastBackupAt
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    init {
        viewModelScope.launch {
            syncScheduledTasks()
        }
    }

    fun setTheme(themeId: NaamSmaranThemeId) {
        viewModelScope.launch {
            settingsStore.setTheme(themeId)
        }
    }

    fun setDailyReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsStore.setDailyReminderEnabled(enabled)
            if (enabled) {
                scheduler.scheduleDailyReminder(
                    settingsStore.dailyReminderHour.first(),
                    settingsStore.dailyReminderMinute.first()
                )
            } else {
                scheduler.cancelDailyReminder()
            }
        }
    }

    fun setDailyReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsStore.setDailyReminderTime(hour, minute)
            if (settingsStore.dailyReminderEnabled.first()) {
                scheduler.scheduleDailyReminder(hour, minute)
            }
        }
    }

    fun setHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsStore.setHapticEnabled(enabled)
        }
    }

    fun setAutoBackupEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsStore.setAutoBackupEnabled(enabled)
            if (enabled) {
                scheduler.scheduleAutoBackup()
            } else {
                scheduler.cancelAutoBackup()
            }
        }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            backupManager.exportToUri(uri)
        }
    }

    fun restoreBackup(uri: Uri) {
        viewModelScope.launch {
            backupManager.restoreFromUri(uri)
            syncScheduledTasks()
        }
    }

    private suspend fun syncScheduledTasks() {
        if (settingsStore.dailyReminderEnabled.first()) {
            scheduler.scheduleDailyReminder(
                settingsStore.dailyReminderHour.first(),
                settingsStore.dailyReminderMinute.first()
            )
        } else {
            scheduler.cancelDailyReminder()
        }

        if (settingsStore.autoBackupEnabled.first()) {
            scheduler.scheduleAutoBackup()
        } else {
            scheduler.cancelAutoBackup()
        }
    }
}
