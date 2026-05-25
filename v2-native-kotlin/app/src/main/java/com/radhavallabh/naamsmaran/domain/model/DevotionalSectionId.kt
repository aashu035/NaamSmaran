package com.radhavallabh.naamsmaran.domain.model

/**
 * DevotionalSectionId — Single source of truth for all 7 devotional sections.
 *
 * Used for:
 * - Navigation route mapping (Screen.kt)
 * - HomeScreen grid tile rendering
 * - DailyRecord column referencing
 * - Section-specific engine dispatch
 *
 * ⚠️ There are exactly 7 sections, not 6. Never remove any.
 *
 * | # | Key             | Hindi Name                           |
 * |---|:----------------|:-------------------------------------|
 * | 1 | NAAM_JAP        | नाम जप (dual-track)                 |
 * | 2 | CHATURASI       | श्री हित चतुरसी जी                   |
 * | 3 | SUDHANIDHI      | श्री हित राधा सुधानिधी जी             |
 * | 4 | SEVAK_VANI      | श्री हित सेवक वाणी                   |
 * | 5 | ASHTAYAM_SEVA   | अष्टयाम सेवा पद्धति                 |
 * | 6 | NITYA_PATH      | नित्य पाठ रसोपासना                  |
 * | 7 | VRINDAVAN_LILA  | श्री वृंदावन शत लीला                 |
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
enum class DevotionalSectionId(
    val sectionNumber: Int,
    val hindiName: String,
    val shortLabel: String,
    val emoji: String,
    val route: String
) {
    /** Section 1 — राधा नाम जप (Track A: Harivansh mala + Track B: Radha counter) */
    NAAM_JAP(
        sectionNumber = 1,
        hindiName = "नाम जप",
        shortLabel = "राधा\nनाम जप",
        emoji = "📿",
        route = "section/naamjap"
    ),

    /** Section 2 — श्री हित चतुरसी जी (84 पद reading, carry-over) */
    CHATURASI(
        sectionNumber = 2,
        hindiName = "श्री हित चतुरसी जी",
        shortLabel = "चतुरसी\nजी",
        emoji = "📖",
        route = "section/chaturasi"
    ),

    /** Section 3 — श्री हित राधा सुधानिधी जी (reading with meaning, carry-over) */
    SUDHANIDHI(
        sectionNumber = 3,
        hindiName = "श्री हित राधा सुधानिधी जी स्तोत्र",
        shortLabel = "सुधानिधी\nजी",
        emoji = "🌸",
        route = "section/sudhanidhi"
    ),

    /** Section 4 — श्री हित सेवक वाणी (छंद reading with meaning, carry-over) */
    SEVAK_VANI(
        sectionNumber = 4,
        hindiName = "श्री हित सेवक वाणी",
        shortLabel = "सेवक\nवाणी",
        emoji = "🌙",
        route = "section/sevakvani"
    ),

    /** Section 5 — अष्टयाम सेवा पद्धति (daily seva checklist, no escalation) */
    ASHTAYAM_SEVA(
        sectionNumber = 5,
        hindiName = "अष्टयाम सेवा पद्धति",
        shortLabel = "अष्टयाम\nसेवा",
        emoji = "",
        route = "section/ashtayamseva"
    ),

    /** Section 6 — नित्य पाठ रसोपासना (daily toggle + calendar, no escalation) */
    NITYA_PATH(
        sectionNumber = 6,
        hindiName = "नित्य पाठ रसोपासना",
        shortLabel = "नित्य\nपाठ",
        emoji = "",
        route = "section/nityapath"
    ),

    /** Section 7 — श्री वृंदावन शत लीला (छंद reading, carry-over — NOT doubling) */
    VRINDAVAN_LILA(
        sectionNumber = 7,
        hindiName = "श्री वृंदावन शत लीला",
        shortLabel = "वृंदावन\nशत लीला",
        emoji = "",
        route = "section/vrindavanlila"
    );

    companion object {
        /** Get section by its 1-based number. Returns null for invalid numbers. */
        fun fromNumber(number: Int): DevotionalSectionId? =
            entries.find { it.sectionNumber == number }

        /** Get section by its route string. Returns null if not found. */
        fun fromRoute(route: String): DevotionalSectionId? =
            entries.find { it.route == route }
    }
}
