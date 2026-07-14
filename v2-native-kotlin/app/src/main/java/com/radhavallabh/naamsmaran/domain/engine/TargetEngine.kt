package com.radhavallabh.naamsmaran.domain.engine

/**
 * TargetEngine — THE FORMULA. Core of the app.
 *
 * T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
 *
 * VERIFICATION TABLE (from agents/02-formula.md — INCREMENT=5,000):
 * | Day | Target    | Did       | Case     | Next Target |
 * |-----|-----------|-----------|----------|-------------|
 * | 1   | 21,600    | 25,000    | exceeded | 30,000      |
 * | 2   | 30,000    | 30,000    | exact    | 35,000      |
 * | 3   | 35,000    | 20,000    | deficit  | 50,000      |
 * | 4   | 50,000    | 0         | missed   | 1,00,000    |
 * | 5   | 1,00,000  | 0         | missed   | 2,00,000    |
 * | 6   | 2,00,000  | 2,10,000  | exceeded | 2,15,000    |
 *
 * No cap on maximum target — the formula runs pure, no ceiling.
 *
 * Ported from: src/logic/targetEngine.js
 */
object TargetEngine {

    /**
     * Calculate tomorrow's target based on today's performance.
     *
     * @param targetToday T(n) — today's target
     * @param countToday D(n) — actual count today
     * @param increment The daily increment (default 5000)
     * @return T(n+1) — tomorrow's target
     */
    fun calculateNextTarget(
        targetToday: Long,
        countToday: Long,
        increment: Int = 5000
    ): Long {
        require(countToday >= 0) { "Did count cannot be negative" }
        
        val nextTarget = if (countToday >= targetToday) {
            countToday + increment
        } else {
            targetToday + (targetToday - countToday)
        }
        
        require(nextTarget >= targetToday) { "Target overflow detected" }
        return nextTarget
    }

    /**
     * Recompute the full target chain from a starting record forward.
     * Used when a past day's D(n) is edited.
     *
     * @param records List of DailyRecords sorted by date ascending
     * @param increment The increment value
     * @return Updated list with recomputed targets and streaks
     */
    fun recomputeChain(
        records: List<com.radhavallabh.naamsmaran.data.local.entity.DailyRecord>,
        increment: Int = 5000,
        initialTarget: Long = 21600L
    ): List<com.radhavallabh.naamsmaran.data.local.entity.DailyRecord> {
        if (records.isEmpty()) return records

        val result = mutableListOf<com.radhavallabh.naamsmaran.data.local.entity.DailyRecord>()

        for ((index, record) in records.withIndex()) {
            val target = if (index == 0) {
                initialTarget
            } else {
                val prev = result[index - 1]
                calculateNextTarget(prev.target, prev.did, increment)
            }

            val prevStreak = if (index == 0) 0 else result[index - 1].streakCount
            val streak = StreakEngine.calculateStreak(prevStreak, target, record.did)

            result.add(record.copy(
                target = target,
                checkNaamJap = record.did >= target,
                streakCount = streak,
                updatedAt = System.currentTimeMillis()
            ))
        }

        return result
    }
}
