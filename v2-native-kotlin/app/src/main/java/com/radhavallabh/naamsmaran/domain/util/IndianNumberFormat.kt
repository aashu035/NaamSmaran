package com.radhavallabh.naamsmaran.domain.util

/**
 * Indian number formatting — 1,000 / 10,000 / 1,00,000 / 9,99,999
 * Per AGENTS.md §1: Indian system everywhere.
 */
object IndianNumberFormat {

    /**
     * Format a number into Indian numbering system.
     *
     * Examples:
     *   1000       → "1,000"
     *   10000      → "10,000"
     *   100000     → "1,00,000"
     *   999999     → "9,99,999"
     *   21600      → "21,600"
     *   136000     → "1,36,000"
     */
    fun format(number: Long): String {
        if (number < 0) return "-${format(-number)}"
        if (number < 1000) return number.toString()

        val str = number.toString()
        val len = str.length

        // Last 3 digits get first comma
        val lastThree = str.substring(len - 3)
        val remaining = str.substring(0, len - 3)

        // Remaining digits get grouped in pairs from right
        val formatted = buildString {
            for ((i, ch) in remaining.withIndex()) {
                if (i > 0 && (remaining.length - i) % 2 == 0) {
                    append(',')
                }
                append(ch)
            }
        }

        return "$formatted,$lastThree"
    }

    /**
     * Short format for very large numbers.
     * 1,50,000 → "1.5L"
     * 10,00,000 → "10L"
     */
    fun formatShort(number: Long): String {
        return when {
            number >= 10_000_000 -> "${number / 10_000_000}.${(number % 10_000_000) / 1_000_000}Cr"
            number >= 100_000 -> "${number / 100_000}.${(number % 100_000) / 10_000}L"
            else -> format(number)
        }
    }
}
