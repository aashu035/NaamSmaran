package com.radhavallabh.naamsmaran.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radhavallabh.naamsmaran.data.repository.JapRepository
import com.radhavallabh.naamsmaran.domain.engine.NityaPathProgressSnapshot
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.domain.model.CarryOverSectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SectionsViewModel @Inject constructor(
    private val repository: JapRepository
) : ViewModel() {

    val ashtayamDoneToday: StateFlow<Boolean> = repository.isAshtayamDoneToday()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val nityaPathProgress: StateFlow<NityaPathProgressSnapshot> = repository.getNityaPathProgress(LocalDate.now())
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NityaPathProgressSnapshot(
                isDoneToday = false,
                currentStreak = 0,
                completedDaysInMonth = emptySet()
            )
        )

    fun carryOverSectionState(section: CarryOverSection): StateFlow<CarryOverSectionState> =
        repository.getCarryOverSectionState(section)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CarryOverSectionState(
                    spec = section,
                    todayTarget = section.dailyBase,
                    todayDone = 0L,
                    overallRead = 0L
                )
            )

    fun addCarryOverProgress(section: CarryOverSection, amount: Long) {
        viewModelScope.launch {
            repository.addCarryOverProgress(section, amount)
        }
    }

    fun resetCarryOverProgress(section: CarryOverSection) {
        viewModelScope.launch {
            repository.resetCarryOverProgress(section)
        }
    }

    fun setAshtayamDoneToday(done: Boolean) {
        viewModelScope.launch {
            repository.setAshtayamDoneToday(done)
        }
    }

    fun setNityaPathDoneToday(done: Boolean) {
        viewModelScope.launch {
            repository.setNityaPathDoneToday(done)
        }
    }
}
