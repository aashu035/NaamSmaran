package com.radhavallabh.naamsmaran.domain.engine

/**
 * StreakEngine — Streak calculation.
 *
 * S(n) = D(n) >= T(n) ? S(n-1) + 1 : 0
 *
 * Streak requires target MET or exceeded. Partial completion breaks streak.
 *
 * Ported from: src/logic/streakEngine.js
 */
object StreakEngine {

    /**
     * Calculate today's streak.
     *
     * @param currentStreak S(n-1) — yesterday's streak
     * @param targetToday T(n) — today's target
     * @param countToday D(n) — actual count
     * @return S(n) — today's streak
     */
    fun calculateStreak(
        currentStreak: Int,
        targetToday: Long,
        countToday: Long
    ): Int {
        return if (countToday >= targetToday) {
            currentStreak + 1
        } else {
            0
        }
    }
}
