package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Naam Smaran — Spacing & Dimension Tokens
 * Direct translation of tokens.css Category C (spacing) + Category I (visualization).
 *
 * Base-8 grid. Mobile-only app.
 */
object Dimens {
    // ═══════════════════════════════════════════════════════════
    // SPACING — Base-8 grid
    // ═══════════════════════════════════════════════════════════
    val Space1   = 4.dp
    val Space2   = 8.dp
    val Space3   = 12.dp
    val Space4   = 16.dp
    val Space5   = 20.dp
    val Space6   = 24.dp
    val Space7   = 28.dp
    val Space8   = 32.dp
    val Space10  = 40.dp
    val Space12  = 48.dp
    val Space16  = 64.dp
    val Space20  = 80.dp

    // Semantic spacing aliases
    val PaddingCard      = Space6      // 24dp inside glass cards
    val PaddingSection   = Space4      // 16dp horizontal screen padding
    val GapStack         = Space4      // vertical card stack gap
    val GapInline        = Space2      // horizontal element gap
    val BottomNavHeight  = 72.dp       // fixed bottom nav

    // ═══════════════════════════════════════════════════════════
    // PROGRESS / VISUALIZATION
    // ═══════════════════════════════════════════════════════════
    val RingSizeLarge    = 140.dp      // main Naam Jap progress ring
    val RingSizeMedium   = 80.dp       // section summary rings
    val RingStrokeLarge  = 8.dp
    val RingStrokeMedium = 5.dp
    val BarHeight        = 6.dp        // standard progress bar
    val BarHeightThick   = 10.dp       // Chaturasi pad progress

    // ═══════════════════════════════════════════════════════════
    // GLASS CARD — blur radius
    // ═══════════════════════════════════════════════════════════
    val GlassBlurRadius  = 20.dp       // backdrop-filter: blur(20px)

    // ═══════════════════════════════════════════════════════════
    // ANIMATION — Spring parameters
    // ═══════════════════════════════════════════════════════════
    const val SpringDampingDefault   = 0.7f
    const val SpringStiffnessLow    = 200f
    const val SpringStiffnessMedium = 400f
    const val SpringStiffnessHigh   = 800f

    // Tap feedback
    const val ScaleTap  = 0.96f
    const val ScaleHover = 1.02f
}
