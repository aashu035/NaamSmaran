package com.radhavallabh.naamsmaran.platform.santsmaran

import com.radhavallabh.naamsmaran.data.santsmaran.SantSmaranTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class SantAlarmTimeTest {

    private val kolkata = ZoneId.of("Asia/Kolkata")
    private val newYork = ZoneId.of("America/New_York")

    private fun at(zone: ZoneId, y: Int, mo: Int, d: Int, h: Int, mi: Int, s: Int = 0) =
        ZonedDateTime.of(LocalDateTime.of(y, mo, d, h, mi, s), zone)

    @Test
    fun beforeAlarmTime_firesToday() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 3, 59, 59), 4, 0)
        assertEquals(at(kolkata, 2026, 10, 5, 4, 0), next)
    }

    @Test
    fun afterAlarmTime_firesTomorrow() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 4, 0, 1), 4, 0)
        assertEquals(at(kolkata, 2026, 10, 6, 4, 0), next)
    }

    @Test
    fun exactlyAtAlarmTime_firesTomorrow() {
        // The alarm that just rang must not be scheduled again for "now".
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 4, 0, 0), 4, 0)
        assertEquals(at(kolkata, 2026, 10, 6, 4, 0), next)
    }

    @Test
    fun lateEvening_firesNextMorning() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 23, 30), 4, 0)
        assertEquals(at(kolkata, 2026, 10, 6, 4, 0), next)
    }

    @Test
    fun monthAndYearRollover() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 12, 31, 5, 0), 4, 0)
        assertEquals(at(kolkata, 2027, 1, 1, 4, 0), next)
    }

    @Test
    fun customTime_isRespected() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 4, 30), 5, 15)
        assertEquals(at(kolkata, 2026, 10, 5, 5, 15), next)
    }

    @Test
    fun resultIsAlwaysStrictlyInTheFuture() {
        var now = at(kolkata, 2026, 1, 1, 0, 0)
        repeat(200) {
            val next = SantAlarmTime.nextTriggerAt(now, 4, 0)
            assertTrue("now=$now next=$next", next.isAfter(now))
            assertEquals(4, next.hour)
            assertEquals(0, next.minute)
            now = now.plusHours(7).plusMinutes(13)
        }
    }

    @Test
    fun dstSpringForward_gapTimeStillFiresThatDay() {
        // New York 2026-03-08: 02:00 → 03:00, so 02:30 does not exist. Must still ring that morning.
        val now = at(newYork, 2026, 3, 8, 0, 30)
        val next = SantAlarmTime.nextTriggerAt(now, 2, 30)
        assertEquals(8, next.dayOfMonth)
        assertTrue(next.isAfter(now))
        assertTrue("fires within the same day", next.hour in 2..3)
    }

    @Test
    fun dstFallBack_overlapUsesEarlierInstantAndStillFiresOncePerDay() {
        // New York 2026-11-01: 01:00–02:00 happens twice. 01:30 → first (EDT) occurrence.
        val now = at(newYork, 2026, 11, 1, 0, 0)
        val next = SantAlarmTime.nextTriggerAt(now, 1, 30)
        assertEquals(1, next.hour)
        assertEquals(30, next.minute)
        assertEquals(1, next.dayOfMonth)
        // After that first occurrence rang, the next trigger is tomorrow, not the repeated 01:30.
        val afterFirstRing = next.plusSeconds(1)
        val tomorrow = SantAlarmTime.nextTriggerAt(afterFirstRing, 1, 30)
        assertEquals(2, tomorrow.dayOfMonth)
    }

    @Test
    fun timeZoneChange_recomputesInNewZone() {
        // Same instant, different zone → 04:00 means 04:00 on the wall clock of the *current* zone.
        val instant = at(kolkata, 2026, 10, 5, 12, 0).toInstant()
        val inKolkata = SantAlarmTime.nextTriggerAt(instant.atZone(kolkata), 4, 0)
        val inNewYork = SantAlarmTime.nextTriggerAt(instant.atZone(newYork), 4, 0)
        assertEquals(kolkata, inKolkata.zone)
        assertEquals(newYork, inNewYork.zone)
        assertEquals(4, inKolkata.hour)
        assertEquals(4, inNewYork.hour)
    }

    @Test
    fun outOfRangeInput_isClamped() {
        val next = SantAlarmTime.nextTriggerAt(at(kolkata, 2026, 10, 5, 0, 0), 99, 99)
        assertEquals(23, next.hour)
        assertEquals(59, next.minute)
    }

    @Test
    fun defaultAlarmConstants_matchBundledJson() {
        val content = SantSmaranTestData.content()
        assertEquals(SantAlarmDefaults.HOUR, content.alarmHour)
        assertEquals(SantAlarmDefaults.MINUTE, content.alarmMinute)
    }
}
