package com.radhavallabh.naamsmaran.domain.engine

/**
 * MalaTargetEngine — Target recomputation for Track A: "हरिवंश" Naam Jap in माला.
 *
 * Daily baseline: 11 Mala
 * If met (did >= target): next target = target + 5 (or 6 alternating, standard increment is 5)
 * If missed (did < target): next target = (target - did) + 11 (deficit carries over + baseline)
 *
 * श्री राधावल्लभ श्री हरिवंश 🙏
 */
object MalaTargetEngine {

    fun calculateNextTarget(
        targetToday: Long,
        didToday: Long,
        base: Long = 11L,
        increment: Long = 5L
    ): Long {
        return if (didToday >= targetToday) {
            targetToday + increment
        } else {
            (targetToday - didToday) + base
        }
    }
}
