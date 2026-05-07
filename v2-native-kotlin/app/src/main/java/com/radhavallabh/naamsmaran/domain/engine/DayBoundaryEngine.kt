package com.radhavallabh.naamsmaran.domain.engine

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * DayBoundaryEngine — Spiritual date logic.
 *
 * effectiveDate = (currentHour < DAY_BOUNDARY_HOUR) ? yesterday : today
 *
 * DAY_BOUNDARY_HOUR = 3 (configurable 1–5 AM in settings).
 * At 2:30 AM, the app still treats it as the previous day for data entry.
 *
 * Ported from: src/logic/dayBoundary.js
 */
object DayBoundaryEngine {

    private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Hindi day names for display
    private val HINDI_DAY_NAMES = mapOf(
        "MONDAY" to "सोमवार",
        "TUESDAY" to "मंगलवार",
        "WEDNESDAY" to "बुधवार",
        "THURSDAY" to "गुरुवार",
        "FRIDAY" to "शुक्रवार",
        "SATURDAY" to "शनिवार",
        "SUNDAY" to "रविवार"
    )

    /**
     * Get the spiritual effective date.
     * Before [boundaryHour] AM, it's still "yesterday" in spiritual terms.
     */
    fun getSpiritualDate(
        now: LocalDateTime = LocalDateTime.now(),
        boundaryHour: Int = 3
    ): String {
        val effectiveDate = if (now.hour < boundaryHour) {
            now.toLocalDate().minusDays(1)
        } else {
            now.toLocalDate()
        }
        return effectiveDate.format(DATE_FORMAT)
    }

    /**
     * Get yesterday's spiritual date (for looking up previous day's record).
     */
    fun getYesterdaySpiritualDate(
        now: LocalDateTime = LocalDateTime.now(),
        boundaryHour: Int = 3
    ): String {
        val effectiveDate = if (now.hour < boundaryHour) {
            now.toLocalDate().minusDays(2)
        } else {
            now.toLocalDate().minusDays(1)
        }
        return effectiveDate.format(DATE_FORMAT)
    }

    /**
     * Get the Hindi name of the current spiritual day.
     */
    fun getDayOfWeekHindi(
        now: LocalDateTime = LocalDateTime.now(),
        boundaryHour: Int = 3
    ): String {
        val effectiveDate = if (now.hour < boundaryHour) {
            now.toLocalDate().minusDays(1)
        } else {
            now.toLocalDate()
        }
        val englishDay = effectiveDate.dayOfWeek.name
        return HINDI_DAY_NAMES[englishDay] ?: englishDay
    }

    /**
     * Get the spiritual date as LocalDate for calendar operations.
     */
    fun getSpiritualLocalDate(
        now: LocalDateTime = LocalDateTime.now(),
        boundaryHour: Int = 3
    ): LocalDate {
        return if (now.hour < boundaryHour) {
            now.toLocalDate().minusDays(1)
        } else {
            now.toLocalDate()
        }
    }
}
