package com.radhavallabh.naamsmaran.ui.screens.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.data.repository.JapRepository
import com.radhavallabh.naamsmaran.domain.engine.DayBoundaryEngine
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.domain.model.didFor
import com.radhavallabh.naamsmaran.domain.model.targetFor
import com.radhavallabh.naamsmaran.domain.util.IndianNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: JapRepository,
    private val settingsStore: AppSettingsStore
) : ViewModel() {

    // Today's record for quick updates and verse display
    val todayRecord: StateFlow<DailyRecord?> = repository.getTodayRecord()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val todaySpiritual = DayBoundaryEngine.getSpiritualLocalDate()

    // ── Zone 1: Weekly Glance (सप्ताह की झलक) ───────────────────────────────────
    val weeklyOverview: StateFlow<WeeklyOverview> = repository.getRecordsBetween(
        startDate = todaySpiritual.with(java.time.DayOfWeek.MONDAY).toString(),
        endDate = todaySpiritual.with(java.time.DayOfWeek.SUNDAY).toString()
    ).map { records ->
        val recordMap = records.associateBy { it.date }
        val monday = todaySpiritual.with(java.time.DayOfWeek.MONDAY)
        val todayStr = DayBoundaryEngine.getSpiritualDate()

        val days = (0..6).map { offset ->
            val localDate = monday.plusDays(offset.toLong())
            val dateStr = localDate.toString()
            val record = recordMap[dateStr]

            val dayHindi = when (localDate.dayOfWeek) {
                java.time.DayOfWeek.MONDAY -> "सोम"
                java.time.DayOfWeek.TUESDAY -> "मंगल"
                java.time.DayOfWeek.WEDNESDAY -> "बुध"
                java.time.DayOfWeek.THURSDAY -> "गुरु"
                java.time.DayOfWeek.FRIDAY -> "शुक्र"
                java.time.DayOfWeek.SATURDAY -> "शनि"
                java.time.DayOfWeek.SUNDAY -> "रवि"
            }

            val state = when {
                record == null -> DayState.EMPTY
                record.did >= record.target && record.target > 0L -> DayState.DONE
                record.did > 0L -> DayState.PARTIAL
                else -> DayState.EMPTY
            }

            val label = if (dateStr == todayStr) "आज" else "${localDate.dayOfMonth} ${getHindiMonthName(localDate.monthValue)}"

            DayStatus(dayHindi, label, state)
        }

        val completedCount = days.count { it.state == DayState.DONE }

        WeeklyOverview(days, completedCount, 7)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeeklyOverview(emptyList(), 0, 7)
    )

    // ── Zone 2 & 3: 30-Day Trends (लक्ष्य रुझान & डोनट चार्ट) ──────────────────────
    private val last30DaysRecords = repository.getRecordsBetween(
        startDate = todaySpiritual.minusDays(29).toString(),
        endDate = todaySpiritual.toString()
    )

    val trendData: StateFlow<List<Long>> = last30DaysRecords.map { records ->
        val recordMap = records.associateBy { it.date }
        (0..29).map { offset ->
            val dateStr = todaySpiritual.minusDays(29L - offset).toString()
            recordMap[dateStr]?.did ?: 0L
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = List(30) { 0L }
    )

    val achievementDonut: StateFlow<AchievementDonut> = last30DaysRecords.map { records ->
        val recordMap = records.associateBy { it.date }
        var completed = 0
        var partial = 0
        var missed = 0

        (0..29).forEach { offset ->
            val dateStr = todaySpiritual.minusDays(29L - offset).toString()
            val record = recordMap[dateStr]
            when {
                record == null -> missed++
                record.did >= record.target && record.target > 0L -> completed++
                record.did > 0L -> partial++
                else -> missed++
            }
        }

        AchievementDonut(completed, partial, missed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AchievementDonut(0, 0, 30)
    )

    // ── Zone 4: 7-Sadhana Progress List ──────────────────────────────────────────
    val sadhanaOverview: StateFlow<List<SadhanaCardData>> = repository.getTodayRecord()
        .map { record ->
            val safeRecord = record ?: DailyRecord(date = DayBoundaryEngine.getSpiritualDate())
            
            listOf(
                // 1. नाम जप (Naam Jap — Track B + Track A)
                SadhanaCardData(
                    id = 1,
                    name = "नाम जप (राधा | हरिवंश)",
                    didLabel = "${IndianNumberFormat.format(safeRecord.did)} जप | ${safeRecord.mala_did} माला",
                    percentage = if (safeRecord.target > 0) safeRecord.did.toFloat() / safeRecord.target.toFloat() else 0f,
                    accentColor = Color(0xFFE8A0BF), // Rose pink
                    statusText = if (safeRecord.did >= safeRecord.target && safeRecord.mala_did >= safeRecord.mala_target) {
                        "नाम जप संपन्न! 📿"
                    } else {
                        "लक्ष्य: ${IndianNumberFormat.format(safeRecord.target)} जप | ${safeRecord.mala_target} माला"
                    },
                    streak = safeRecord.streakCount
                ),
                // 2. श्री हित चतुरसी जी
                SadhanaCardData(
                    id = 2,
                    name = "चतुरसी जी",
                    didLabel = "${safeRecord.chaturasi_did} / ${safeRecord.chaturasi_target} पद",
                    percentage = if (safeRecord.chaturasi_target > 0) safeRecord.chaturasi_did.toFloat() / safeRecord.chaturasi_target.toFloat() else 0f,
                    accentColor = Color(0xFFF5CBA7), // Soft gold
                    statusText = if (safeRecord.chaturasi_did >= safeRecord.chaturasi_target) "पूर्ण" else "${safeRecord.chaturasi_target - safeRecord.chaturasi_did} पद शेष",
                    streak = null
                ),
                // 3. श्री हित राधा सुधानिधी जी
                SadhanaCardData(
                    id = 3,
                    name = "राधा सुधानिधी जी",
                    didLabel = "${safeRecord.sudhanidhi_did} / ${safeRecord.sudhanidhi_target} श्लोक",
                    percentage = if (safeRecord.sudhanidhi_target > 0) safeRecord.sudhanidhi_did.toFloat() / safeRecord.sudhanidhi_target.toFloat() else 0f,
                    accentColor = Color(0xFFE8A0BF), // Rose pink
                    statusText = if (safeRecord.sudhanidhi_did >= safeRecord.sudhanidhi_target) "पूर्ण" else "${safeRecord.sudhanidhi_target - safeRecord.sudhanidhi_did} श्लोक शेष",
                    streak = null
                ),
                // 4. श्री हित सेवक वाणी
                SadhanaCardData(
                    id = 4,
                    name = "सेवक वाणी",
                    didLabel = "${safeRecord.sevakVani_did} / ${safeRecord.sevakVani_target} छंद",
                    percentage = if (safeRecord.sevakVani_target > 0) safeRecord.sevakVani_did.toFloat() / safeRecord.sevakVani_target.toFloat() else 0f,
                    accentColor = Color(0xFFB8C8FF), // Moonlight blue
                    statusText = if (safeRecord.sevakVani_did >= safeRecord.sevakVani_target) "पूर्ण" else "${safeRecord.sevakVani_target - safeRecord.sevakVani_did} छंद शेष",
                    streak = null
                ),
                // 5. अष्टयाम सेवा पद्धति (Checklist, 1/1 complete if checked)
                SadhanaCardData(
                    id = 5,
                    name = "अष्टयाम सेवा",
                    didLabel = if (safeRecord.checkAshtayamSeva) "पूर्ण" else "अपूर्ण",
                    percentage = if (safeRecord.checkAshtayamSeva) 1f else 0f,
                    accentColor = Color(0xFFF5CBA7), // Soft gold
                    statusText = if (safeRecord.checkAshtayamSeva) "सेवा संपन्न 🙏" else "३ सेवा पहर शेष",
                    streak = null
                ),
                // 6. नित्य पाठ रसोपासना
                SadhanaCardData(
                    id = 6,
                    name = "नित्य पाठ",
                    didLabel = if (safeRecord.checkNityaPath) "पूर्ण" else "अपूर्ण",
                    percentage = if (safeRecord.checkNityaPath) 1f else 0f,
                    accentColor = Color(0xFFB8C8FF), // Moonlight blue
                    statusText = if (safeRecord.checkNityaPath) "पाठ पूर्ण 🪷" else "पाठ अपूर्ण",
                    streak = null
                ),
                // 7. श्री वृंदावन शत लीला
                SadhanaCardData(
                    id = 7,
                    name = "वृंदावन शत लीला",
                    didLabel = "${safeRecord.vrindavan_did} / ${safeRecord.vrindavan_target} छंद",
                    percentage = if (safeRecord.vrindavan_target > 0) safeRecord.vrindavan_did.toFloat() / safeRecord.vrindavan_target.toFloat() else 0f,
                    accentColor = Color(0xFFD6C7FF), // Purple
                    statusText = if (safeRecord.vrindavan_did >= safeRecord.vrindavan_target) "पूर्ण" else "${safeRecord.vrindavan_target - safeRecord.vrindavan_did} छंद शेष",
                    streak = null
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ── Quick updates directly from Dashboard screen ─────────────────────────────
    fun quickIncrementSadhana(sectionId: Int) {
        viewModelScope.launch {
            when (sectionId) {
                1 -> repository.addJapCount(108) // add 1 mala count mentally
                2 -> repository.addCarryOverProgress(CarryOverSection.CHATURASI, 1)
                3 -> repository.addCarryOverProgress(CarryOverSection.SUDHANIDHI, 1)
                4 -> repository.addCarryOverProgress(CarryOverSection.SEVAK_VANI, 1)
                5 -> {
                    val record = repository.ensureTodayRecord()
                    repository.setAshtayamDoneToday(!record.checkAshtayamSeva)
                }
                6 -> {
                    val record = repository.ensureTodayRecord()
                    repository.setNityaPathDoneToday(!record.checkNityaPath)
                }
                7 -> repository.addCarryOverProgress(CarryOverSection.VRINDAVAN_LILA, 1)
            }
        }
    }

    private fun getHindiMonthName(monthValue: Int): String {
        return when (monthValue) {
            1 -> "जनवरी"
            2 -> "फरवरी"
            3 -> "मार्च"
            4 -> "अप्रैल"
            5 -> "मई"
            6 -> "जून"
            7 -> "जुलाई"
            8 -> "अगस्त"
            9 -> "सितंबर"
            10 -> "अक्टूबर"
            11 -> "नवंबर"
            12 -> "दिसंबर"
            else -> ""
        }
    }
}

// ── Dashboard State Model classes ──────────────────────────────────────────────

data class WeeklyOverview(
    val days: List<DayStatus>,
    val completedCount: Int,
    val totalDays: Int
)

data class DayStatus(
    val dayName: String,
    val dateLabel: String,
    val state: DayState
)

enum class DayState { DONE, PARTIAL, EMPTY }

data class AchievementDonut(
    val completed: Int,
    val partial: Int,
    val missed: Int
)

data class SadhanaCardData(
    val id: Int,
    val name: String,
    val didLabel: String,
    val percentage: Float,
    val accentColor: Color,
    val statusText: String,
    val streak: Int?
)
