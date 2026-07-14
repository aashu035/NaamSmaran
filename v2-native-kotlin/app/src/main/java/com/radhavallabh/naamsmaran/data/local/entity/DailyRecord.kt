package com.radhavallabh.naamsmaran.data.local.entity

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * DailyRecord — Room entity for daily sadhana tracking.
 * Schema v2: columns aligned to the canonical 7-section structure.
 *
 * Primary key: date string "YYYY-MM-DD" (spiritual date, respecting day boundary).
 *
 * Section columns map to DevotionalSectionId:
 *   1. naamJap*    — Track B count (राधा counter)
 *   2. chaturasi*  — पद count (carry-over)
 *   3. sudhanidhi* — श्लोक count (carry-over)
 *   4. sevakVani*  — छंद count (carry-over)
 *   5. ashtayam*   — seva checklist (boolean done)
 *   6. nityaPath*  — daily toggle (boolean done)
 *   7. vrindavan*  — छंद count (carry-over)
 *
 * Each section has: target (Long), did (Long), check (Boolean).
 * Section 5 & 6 have boolean-only tracking (no numeric target/did).
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Stable
@Entity(tableName = "daily_records")
data class DailyRecord(
    @PrimaryKey
    val date: String,                      // "YYYY-MM-DD" — spiritual effective date

    val dayOfWeek: String = "",            // "शुक्रवार" etc.

    // ═══════════════════════════════════════════════════════════
    // Section 1: नाम जप — Track B (राधा counter, doubling formula)
    // ═══════════════════════════════════════════════════════════
    val target: Long = 21600L,             // T(n) — today's Radha naam jap target
    val did: Long = 0L,                    // D(n) — actual Radha naam jap count
    val nextDayTarget: Long = 0L,          // Computed on day close
    val checkNaamJap: Boolean = false,     // auto-true when did > 0

    // Track A — हरिवंश (mala counter, carry-over + 5 baseline formula)
    val mala_target: Long = 11L,
    val mala_did: Long = 0L,
    val checkMala: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 2: श्री हित चतुरसी जी (carry-over, +6 पद)
    // ═══════════════════════════════════════════════════════════
    val chaturasi_target: Long = 12L,
    val chaturasi_did: Long = 0L,
    val checkChaturasi: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 3: श्री हित राधा सुधानिधी जी (carry-over, +5 श्लोक)
    // ═══════════════════════════════════════════════════════════
    val sudhanidhi_target: Long = 10L,
    val sudhanidhi_did: Long = 0L,
    val checkSudhanidhi: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 4: श्री हित सेवक वाणी (carry-over, +2 छंद)
    // ═══════════════════════════════════════════════════════════
    val sevakVani_target: Long = 5L,
    val sevakVani_did: Long = 0L,
    val checkSevakVani: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 5: अष्टयाम सेवा पद्धति (checklist, boolean only)
    // ═══════════════════════════════════════════════════════════
    val checkAshtayamSeva: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 6: नित्य पाठ रसोपासना (daily toggle, boolean only)
    // ═══════════════════════════════════════════════════════════
    val checkNityaPath: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Section 7: श्री वृंदावन शत लीला (carry-over, +5 छंद)
    // ═══════════════════════════════════════════════════════════
    val vrindavan_target: Long = 10L,
    val vrindavan_did: Long = 0L,
    val checkVrindavan: Boolean = false,

    // ═══════════════════════════════════════════════════════════
    // Common fields
    // ═══════════════════════════════════════════════════════════
    val streakCount: Int = 0,              // S(n) — stored for fast reads
    val quoteShown: String = "",           // Quote ID shown in day-end analysis
    val notes: String = "",                // Max 500 chars

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
