package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.animation.core.spring
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
    val RingSizeLarge    = 180.dp      // main Naam Jap progress ring
    val RingSizeMedium   = 80.dp       // section summary rings
    val RingStrokeLarge  = 10.dp
    val RingStrokeMedium = 5.dp
    val BarHeight        = 6.dp        // standard progress bar
    val BarHeightThick   = 10.dp       // Chaturasi pad progress

    // Home dashboard
    val SheetSwipeThreshold = 120.dp
    val SwipeHintBottomPadding = Space8
    val SheetCornerRadius = Space7
    val SheetHandleWidth = Space8 + Space1

    // Sheet offset fractions (fraction of screen height from top where sheet top edge sits).
    // Hidden:      0.90 → sheet mostly off-screen, only the drag handle peeks (~10% from bottom)
    // QuickActions: 0.45 → sheet covers ~55% of screen (shows header + summary + quick-add)
    // FullGrid:    0.04 → sheet almost full-screen (4% gap at top for visual breathing room)
    const val SheetHiddenOffsetFraction = 0.90f
    const val SheetQuickActionsOffsetFraction = 0.45f
    const val SheetFullGridOffsetFraction = 0.04f
    const val SheetDragThresholdFraction = 0.12f

    // ═══════════════════════════════════════════════════════════
    // SANT SMARAN — reading screen
    // ═══════════════════════════════════════════════════════════
    val SantCardCorner      = Space7          // 28dp — never below 12dp
    // Saint photos are tall portraits (aspect 0.41–0.67), shown whole (Fit), never cropped.
    // Frame height = fraction of the page height, clamped so it works on small and large phones.
    const val SantPhotoHeightFraction    = 0.50f
    const val SantNameCardHeightFraction = 0.24f
    val SantPhotoMinHeight  = 220.dp
    val SantPhotoMaxHeight  = 460.dp
    val SantNameCardMinHeight = 150.dp
    val SantNameCardMaxHeight = 240.dp
    val SantIndicatorHeight = Space1
    const val SantAutoAdvanceMillis = 6_000L  // optional auto-advance interval

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

/**
 * Pre-built animation specs for consistent motion across the app.
 * References [Dimens] constants — Rule 7 compliance.
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
object NaamSmaranMotion {
    /** Default spring: used for tap-scale feedback, card reveals. */
    val DefaultSpring = spring<Float>(
        dampingRatio = Dimens.SpringDampingDefault,
        stiffness = Dimens.SpringStiffnessMedium
    )

    /** Gentle spring: used for counter roll-up in Day-End Analysis. */
    val CounterSpring = spring<Float>(
        dampingRatio = Dimens.SpringDampingDefault,
        stiffness = Dimens.SpringStiffnessLow
    )

    /** Tap scale factor (Rule 6) — subtle press feedback. */
    const val TapScale = Dimens.ScaleTap

    /** Max animation duration (Rule 7) — hard cap. */
    const val MaxAnimationDurationMs = 400
}
