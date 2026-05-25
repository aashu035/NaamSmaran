package com.radhavallabh.naamsmaran.domain.engine

import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import java.time.LocalDate

data class NityaPathProgressSnapshot(
    val isDoneToday: Boolean,
    val currentStreak: Int,
    val completedDaysInMonth: Set<Int>
)

object NityaPathProgressCalculator {

    fun calculate(
        records: List<DailyRecord>,
        today: LocalDate
    ): NityaPathProgressSnapshot {
        val recordsByDate = records.associateBy { it.date }
        val todayKey = today.toString()
        val isDoneToday = recordsByDate[todayKey]?.checkNityaPath == true

        var streak = 0
        var cursor = today
        while (recordsByDate[cursor.toString()]?.checkNityaPath == true) {
            streak += 1
            cursor = cursor.minusDays(1)
        }

        val completedDaysInMonth = records
            .filter { it.checkNityaPath }
            .mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }
            .filter { it.year == today.year && it.month == today.month }
            .map { it.dayOfMonth }
            .toSet()

        return NityaPathProgressSnapshot(
            isDoneToday = isDoneToday,
            currentStreak = streak,
            completedDaysInMonth = completedDaysInMonth
        )
    }
}
