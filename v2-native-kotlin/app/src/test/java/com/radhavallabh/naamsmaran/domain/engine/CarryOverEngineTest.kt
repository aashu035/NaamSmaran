package com.radhavallabh.naamsmaran.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class CarryOverEngineTest {

    @Test
    fun `met target adds met increment`() {
        val result = CarryOverEngine.calculateNextTarget(
            targetToday = 12L,
            countToday = 12L,
            dailyBase = 12L,
            metIncrement = 6L
        )

        assertEquals(18L, result)
    }

    @Test
    fun `missed target carries remaining plus daily base`() {
        val result = CarryOverEngine.calculateNextTarget(
            targetToday = 12L,
            countToday = 7L,
            dailyBase = 12L,
            metIncrement = 6L
        )

        assertEquals(17L, result)
    }

    @Test
    fun `full miss keeps debt and adds fresh daily base`() {
        val result = CarryOverEngine.calculateNextTarget(
            targetToday = 10L,
            countToday = 0L,
            dailyBase = 10L,
            metIncrement = 5L
        )

        assertEquals(20L, result)
    }
}
