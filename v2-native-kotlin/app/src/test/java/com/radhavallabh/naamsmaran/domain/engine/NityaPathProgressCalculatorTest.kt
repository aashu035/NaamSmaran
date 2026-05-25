package com.radhavallabh.naamsmaran.domain.engine

import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class NityaPathProgressCalculatorTest {

    @Test
    fun `empty record list yields empty first launch state`() {
        val snapshot = NityaPathProgressCalculator.calculate(
            records = emptyList(),
            today = LocalDate.of(2026, 5, 15)
        )

        assertFalse(snapshot.isDoneToday)
        assertEquals(0, snapshot.currentStreak)
        assertTrue(snapshot.completedDaysInMonth.isEmpty())
    }

    @Test
    fun `streak counts only contiguous completed spiritual days`() {
        val snapshot = NityaPathProgressCalculator.calculate(
            records = listOf(
                DailyRecord(date = "2026-05-12", checkNityaPath = true),
                DailyRecord(date = "2026-05-13", checkNityaPath = true),
                DailyRecord(date = "2026-05-14", checkNityaPath = true),
                DailyRecord(date = "2026-05-15", checkNityaPath = true)
            ),
            today = LocalDate.of(2026, 5, 15)
        )

        assertTrue(snapshot.isDoneToday)
        assertEquals(4, snapshot.currentStreak)
        assertEquals(setOf(12, 13, 14, 15), snapshot.completedDaysInMonth)
    }
}
