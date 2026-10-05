package com.radhavallabh.naamsmaran.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.radhavallabh.naamsmaran.platform.santsmaran.SantAlarmPermissions
import com.radhavallabh.naamsmaran.platform.santsmaran.SantAlarmReadiness
import com.radhavallabh.naamsmaran.platform.santsmaran.SantAlarmScheduler
import com.radhavallabh.naamsmaran.platform.santsmaran.SantScheduleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SantAlarmUiState(
    val enabled: Boolean,
    val hour: Int,
    val minute: Int,
    /** Epoch millis of the next daily ring, null when the alarm is off. */
    val nextAlarmMillis: Long?,
    val readiness: SantAlarmReadiness,
    /** Result of the most recent scheduling attempt (null = nothing attempted yet). */
    val lastResult: SantScheduleResult?,
    /** True right after the test alarm was armed, to tell the user to lock the phone. */
    val testArmed: Boolean = false
)

/**
 * State for the "प्रातः संत नाम स्मरण" card in Settings: alarm on/off + time, the live
 * permission checklist (re-read on every resume), and the 10-second test alarm.
 */
@HiltViewModel
class SantAlarmSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scheduler: SantAlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(snapshot(lastResult = null))
    val state: StateFlow<SantAlarmUiState> = _state.asStateFlow()

    /** Re-reads permissions and the schedule; call when the screen resumes. */
    fun refresh() {
        _state.value = snapshot(_state.value.lastResult)
    }

    fun setEnabled(enabled: Boolean) {
        _state.value = snapshot(scheduler.setEnabled(enabled))
    }

    fun setTime(hour: Int, minute: Int) {
        _state.value = snapshot(scheduler.setTime(hour, minute))
    }

    /** Arms a one-off alarm ~10 s from now so the whole ring flow can be tried right away. */
    fun scheduleTest() {
        val result = scheduler.scheduleTest()
        _state.value = snapshot(result).copy(testArmed = result == SantScheduleResult.SCHEDULED)
    }

    private fun snapshot(lastResult: SantScheduleResult?): SantAlarmUiState =
        SantAlarmUiState(
            enabled = scheduler.isEnabled,
            hour = scheduler.hour,
            minute = scheduler.minute,
            nextAlarmMillis = scheduler.nextDailyTriggerMillis(),
            readiness = SantAlarmPermissions.read(context),
            lastResult = lastResult
        )
}
