package com.radhavallabh.naamsmaran.domain.model

import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord

enum class CarryOverSection(
    val devotionalSectionId: DevotionalSectionId,
    val dailyBase: Long,
    val metIncrement: Long,
    val totalUnits: Long,
    val unitLabel: String,
    val quickAdds: List<Long>,
    val subtitle: String
) {
    CHATURASI(
        devotionalSectionId = DevotionalSectionId.CHATURASI,
        dailyBase = 12L,
        metIncrement = 6L,
        totalUnits = 84L,
        unitLabel = "पद",
        quickAdds = listOf(1L, 3L, 6L),
        subtitle = "कुल ८४ पद"
    ),
    SUDHANIDHI(
        devotionalSectionId = DevotionalSectionId.SUDHANIDHI,
        dailyBase = 10L,
        metIncrement = 5L,
        totalUnits = 0L,
        unitLabel = "श्लोक",
        quickAdds = listOf(1L, 2L, 5L),
        subtitle = "अर्थ सहित पाठ"
    ),
    SEVAK_VANI(
        devotionalSectionId = DevotionalSectionId.SEVAK_VANI,
        dailyBase = 5L,
        metIncrement = 2L,
        totalUnits = 0L,
        unitLabel = "छंद",
        quickAdds = listOf(1L, 2L, 5L),
        subtitle = "अर्थ सहित पाठ"
    ),
    VRINDAVAN_LILA(
        devotionalSectionId = DevotionalSectionId.VRINDAVAN_LILA,
        dailyBase = 10L,
        metIncrement = 5L,
        totalUnits = 100L,
        unitLabel = "छंद",
        quickAdds = listOf(1L, 2L, 5L),
        subtitle = "कुल १०० छंद"
    )
}

data class CarryOverSectionState(
    val spec: CarryOverSection,
    val todayTarget: Long,
    val todayDone: Long,
    val overallRead: Long
) {
    val remaining: Long get() = (todayTarget - todayDone).coerceAtLeast(0L)
    val isComplete: Boolean get() = todayDone >= todayTarget
    val todayProgress: Float get() = if (todayTarget <= 0) 0f else (todayDone.toFloat() / todayTarget.toFloat()).coerceIn(0f, 1f)
    val overallProgress: Float get() = if (spec.totalUnits <= 0) 0f else (overallRead.toFloat() / spec.totalUnits.toFloat()).coerceIn(0f, 1f)
}

fun DailyRecord.targetFor(section: CarryOverSection): Long = when (section) {
    CarryOverSection.CHATURASI -> chaturasi_target
    CarryOverSection.SUDHANIDHI -> sudhanidhi_target
    CarryOverSection.SEVAK_VANI -> sevakVani_target
    CarryOverSection.VRINDAVAN_LILA -> vrindavan_target
}

fun DailyRecord.didFor(section: CarryOverSection): Long = when (section) {
    CarryOverSection.CHATURASI -> chaturasi_did
    CarryOverSection.SUDHANIDHI -> sudhanidhi_did
    CarryOverSection.SEVAK_VANI -> sevakVani_did
    CarryOverSection.VRINDAVAN_LILA -> vrindavan_did
}

fun DailyRecord.checkFor(section: CarryOverSection): Boolean = when (section) {
    CarryOverSection.CHATURASI -> checkChaturasi
    CarryOverSection.SUDHANIDHI -> checkSudhanidhi
    CarryOverSection.SEVAK_VANI -> checkSevakVani
    CarryOverSection.VRINDAVAN_LILA -> checkVrindavan
}

fun DailyRecord.withCarryOverProgress(
    section: CarryOverSection,
    target: Long = targetFor(section),
    did: Long = didFor(section),
    checked: Boolean = checkFor(section),
    updatedAt: Long = System.currentTimeMillis()
): DailyRecord = when (section) {
    CarryOverSection.CHATURASI -> copy(
        chaturasi_target = target,
        chaturasi_did = did,
        checkChaturasi = checked,
        updatedAt = updatedAt
    )
    CarryOverSection.SUDHANIDHI -> copy(
        sudhanidhi_target = target,
        sudhanidhi_did = did,
        checkSudhanidhi = checked,
        updatedAt = updatedAt
    )
    CarryOverSection.SEVAK_VANI -> copy(
        sevakVani_target = target,
        sevakVani_did = did,
        checkSevakVani = checked,
        updatedAt = updatedAt
    )
    CarryOverSection.VRINDAVAN_LILA -> copy(
        vrindavan_target = target,
        vrindavan_did = did,
        checkVrindavan = checked,
        updatedAt = updatedAt
    )
}
