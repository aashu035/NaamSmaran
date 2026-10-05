package com.radhavallabh.naamsmaran.platform.santsmaran

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * Pure time maths for the daily "प्रातः संत नाम स्मरण" alarm (no Android dependencies).
 *
 * Wall-clock semantics: the alarm means "04:00 local time, every day". After a time-zone
 * change or a clock change the receiver recomputes from scratch, so no offset is cached.
 */
object SantAlarmTime {

    /**
     * Next occurrence of [hour]:[minute] that is strictly after [now], in [now]'s zone.
     *
     * - Exactly at the alarm time → tomorrow (today's has just fired).
     * - DST gap (local time doesn't exist, e.g. 02:30 on spring-forward day) → java.time shifts
     *   forward by the gap length, so the alarm still rings that day.
     * - DST overlap (local time occurs twice) → the earlier instant is used.
     */
    fun nextTriggerAt(now: ZonedDateTime, hour: Int, minute: Int): ZonedDateTime {
        val time = LocalTime.of(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
        val zone = now.zone

        val today = LocalDateTime.of(now.toLocalDate(), time).atZone(zone)
        if (today.isAfter(now)) return today

        return LocalDateTime.of(now.toLocalDate().plusDays(1), time).atZone(zone)
    }
}
