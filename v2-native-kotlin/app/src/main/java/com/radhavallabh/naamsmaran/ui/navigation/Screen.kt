package com.radhavallabh.naamsmaran.ui.navigation

import com.radhavallabh.naamsmaran.domain.model.DevotionalSectionId

/**
 * Screen — sealed class defining all navigation routes.
 *
 * All 7 devotional sections plus HomeScreen + Settings.
 * Navigation is zero-UI: everything flows from the GlassBottomSheet.
 *
 * Route names now match DevotionalSectionId.route exactly.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
sealed class Screen(val route: String) {

    /** Home — full-screen Darshan experience, entry point */
    data object Home : Screen("home")

    /** Section 1 — नाम जप (Track A: Harivansh mala + Track B: Radha counter) */
    data object NaamJap : Screen(DevotionalSectionId.NAAM_JAP.route)

    /** Section 2 — श्री हित चतुरसी जी (84 पद reading, carry-over) */
    data object Chaturasi : Screen(DevotionalSectionId.CHATURASI.route)

    /** Section 3 — श्री हित राधा सुधानिधी जी (reading with meaning, carry-over) */
    data object Sudhanidhi : Screen(DevotionalSectionId.SUDHANIDHI.route)

    /** Section 4 — श्री हित सेवक वाणी (छंद reading, carry-over) */
    data object SevakVani : Screen(DevotionalSectionId.SEVAK_VANI.route)

    /** Section 5 — अष्टयाम सेवा पद्धति (daily seva checklist) */
    data object AshtayamSeva : Screen(DevotionalSectionId.ASHTAYAM_SEVA.route)

    /** Section 6 — नित्य पाठ रसोपासना (daily toggle + calendar) */
    data object NityaPath : Screen(DevotionalSectionId.NITYA_PATH.route)

    /** Section 7 — श्री वृंदावन शत लीला (छंद reading, carry-over) */
    data object VrindavanLila : Screen(DevotionalSectionId.VRINDAVAN_LILA.route)

    /** Settings — app configuration */
    data object Settings : Screen("settings")

    /** Dashboard — sadhana overview analytics */
    data object Dashboard : Screen("dashboard")

    /**
     * प्रातः संत नाम स्मरण — morning saint-name recitation, swipe-through reading screen.
     * Opened from Settings and from the 4 AM alarm. Not one of the 7 devotional sections
     * (no counters, no carry-over) so it has no Home-grid tile.
     */
    data object SantSmaran : Screen("sant_smaran")

    companion object {
        /**
         * Maps section index from HomeScreen grid → Screen route.
         * Index 0–6 = 7 devotional sections, Index 7 = Settings.
         */
        fun fromSectionIndex(index: Int): Screen = when (index) {
            0 -> NaamJap
            1 -> Chaturasi
            2 -> Sudhanidhi
            3 -> SevakVani
            4 -> AshtayamSeva
            5 -> NityaPath
            6 -> VrindavanLila
            7 -> Settings
            else -> Home
        }
    }
}
