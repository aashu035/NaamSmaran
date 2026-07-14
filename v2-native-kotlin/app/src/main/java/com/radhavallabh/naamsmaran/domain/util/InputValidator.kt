package com.radhavallabh.naamsmaran.domain.util

/**
 * InputValidator — Enforces strict sanitization and validation on all custom inputs.
 * Adheres to ECC Standards to prevent SQL injection and unsafe patterns.
 */
object InputValidator {
    private val sqlInjectionPattern = Regex(
        "['|--|;|/\\*|\\*/|\\bUNION\\b|\\bDROP\\b|\\bSELECT\\b|\\bINSERT\\b|\\bDELETE\\b]",
        RegexOption.IGNORE_CASE
    )

    /**
     * Validates [input] against SQL injection patterns.
     * Returns true if safe, false if potential injection detected.
     */
    fun isSafe(input: String): Boolean {
        if (input.isBlank()) return true
        return !sqlInjectionPattern.containsMatchIn(input)
    }

    /**
     * Sanitizes [input] by removing any matched SQL injection patterns.
     * Use cautiously, as it might alter the string unexpectedly.
     */
    fun sanitize(input: String): String {
        return input.replace(sqlInjectionPattern, "")
    }
}
