package com.radhavallabh.naamsmaran.domain.engine

/**
 * CarryOverEngine — reading-section target progression.
 *
 * For reading sections, missed work carries forward without doubling.
 * If the target is met, tomorrow grows by the section's met increment.
 * If the target is missed, tomorrow carries the unread remainder and adds
 * the section's fresh daily base.
 */
object CarryOverEngine {

    fun calculateNextTarget(
        targetToday: Long,
        countToday: Long,
        dailyBase: Long,
        metIncrement: Long
    ): Long {
        return if (countToday >= targetToday) {
            targetToday + metIncrement
        } else {
            (targetToday - countToday).coerceAtLeast(0L) + dailyBase
        }
    }
}
