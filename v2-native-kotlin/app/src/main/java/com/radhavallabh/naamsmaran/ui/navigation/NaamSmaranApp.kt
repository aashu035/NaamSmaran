package com.radhavallabh.naamsmaran.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.radhavallabh.naamsmaran.ui.screens.ashtayamseva.AshtayamSevaScreen
import com.radhavallabh.naamsmaran.ui.screens.chaturdas.ChaturdasScreen
import com.radhavallabh.naamsmaran.ui.screens.home.HomeScreen
import com.radhavallabh.naamsmaran.ui.screens.kirtan.KirtanScreen
import com.radhavallabh.naamsmaran.ui.screens.lalita.LalitaScreen
import com.radhavallabh.naamsmaran.ui.screens.maharas.MaharasScreen
import com.radhavallabh.naamsmaran.ui.screens.naamjap.NaamJapScreen
import com.radhavallabh.naamsmaran.ui.screens.settings.SettingsScreen
import com.radhavallabh.naamsmaran.ui.screens.vrindavanlila.VrindavanLilaScreen

/**
 * NaamSmaranApp — Root navigation composable.
 *
 * Zero-UI design: HomeScreen is the full-screen darshan experience.
 * All section navigation is triggered from the GlassBottomSheet grid.
 * Back-press from any section returns to Home — never closes the app.
 *
 * Route mapping (7 sections + Settings):
 *   0 = NaamJap       → NaamJapScreen (dual-track: Harivansh mala + Radha counter)
 *   1 = Chaturasi     → KirtanScreen (84 पद reading)
 *   2 = Sudhanidhi    → MaharasScreen (श्लोक with meaning)
 *   3 = SevakVani     → LalitaScreen (छंद reading)
 *   4 = AshtayamSeva  → AshtayamSevaScreen (seva checklist)
 *   5 = NityaPath     → NityaPathScreen (daily toggle)
 *   6 = VrindavanLila → ChaturdasScreen (छंद reading)
 *   7 = Settings      → SettingsScreen
 *
 * Note: Screen files (KirtanScreen, MaharasScreen, etc.) retain their old filenames
 * for git history continuity. Routes use canonical DevotionalSectionId names.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun NaamSmaranApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // ── Home ──────────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSection = { index ->
                    val screen = Screen.fromSectionIndex(index)
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // ── Section 1: नाम जप (dual-track: Harivansh mala + Radha counter) ──
        composable(Screen.NaamJap.route) {
            NaamJapScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 2: श्री हित चतुरसी जी (84 पद reading) ──────────────
        composable(Screen.Chaturasi.route) {
            KirtanScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 3: श्री हित राधा सुधानिधी जी (श्लोक with meaning) ──
        composable(Screen.Sudhanidhi.route) {
            MaharasScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 4: श्री हित सेवक वाणी (छंद reading) ────────────────
        composable(Screen.SevakVani.route) {
            LalitaScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 5: अष्टयाम सेवा पद्धति (seva checklist) ────────────
        composable(Screen.AshtayamSeva.route) {
            AshtayamSevaScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 6: नित्य पाठ रसोपासना (daily toggle + calendar) ───────────
        // Note: ChaturdasScreen.kt has the full-featured daily toggle + streak + calendar UI
        // that matches Section 6's spec. The filename is a legacy artifact.
        composable(Screen.NityaPath.route) {
            ChaturdasScreen(onBack = { navController.popBackStack() })
        }

        // ── Section 7: श्री वृंदावन शत लीला (छंद reading) ──────────────
        composable(Screen.VrindavanLila.route) {
            VrindavanLilaScreen(onBack = { navController.popBackStack() })
        }

        // ── Settings ──────────────────────────────────────────────────────
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
