package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Naam Smaran — Color Palettes
 * Direct translation of tokens.css into Compose Color objects.
 * 5 Radhavallabh Sampraday themes — never generic, always sacred.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// ═══════════════════════════════════════════════════════════════
// THEME 1: SHARAD MOON (DEFAULT)
// Deep indigo, pink lotus, moonlight blue
// ═══════════════════════════════════════════════════════════════
object SharadMoonColors {
    val bgPrimary        = Color(0xFF070010)
    val accentPrimary    = Color(0xFFE8A0BF) // rose-pink lotus
    val accentSecondary  = Color(0xFFB8C8FF) // moonlight blue
    val accentGold       = Color(0xFFF5CBA7) // soft champak gold
    val accentGlow       = Color(0x26E8A0BF) // 15% alpha
    val malaBead         = Color(0xFF6B4D7A)
}

// ═══════════════════════════════════════════════════════════════
// THEME 2: VRINDAVAN DAWN
// Warm rose-gold, pearl light
// ═══════════════════════════════════════════════════════════════
object VrindavanDawnColors {
    val bgPrimary        = Color(0xFF0A0408)
    val accentPrimary    = Color(0xFFFFD4A3) // champak dawn gold
    val accentSecondary  = Color(0xFFFFAAB5) // rose-pearl
    val accentGold       = Color(0xFFFFD700)
    val accentGlow       = Color(0x1FFFD4A3) // 12% alpha
    val malaBead         = Color(0xFF7A5D4D)
}

// ═══════════════════════════════════════════════════════════════
// THEME 3: NIKUNJ
// Emerald bower, golden-green
// ═══════════════════════════════════════════════════════════════
object NikunjColors {
    val bgPrimary        = Color(0xFF030A04)
    val accentPrimary    = Color(0xFFA8E6CF) // bower green
    val accentSecondary  = Color(0xFFFFD4A3) // golden light through leaves
    val accentGold       = Color(0xFFC8E6A0)
    val accentGlow       = Color(0x1AA8E6CF) // 10% alpha
    val malaBead         = Color(0xFF4D6B5A)
}

// ═══════════════════════════════════════════════════════════════
// THEME 4: VAN VIHAAR
// Forest amber, kadamba gold
// ═══════════════════════════════════════════════════════════════
object VanVihaarColors {
    val bgPrimary        = Color(0xFF040806)
    val accentPrimary    = Color(0xFFE8C547) // kadamba amber
    val accentSecondary  = Color(0xFF89C4A0) // forest green
    val accentGold       = Color(0xFFF5D76E)
    val accentGlow       = Color(0x1AE8C547) // 10% alpha
    val malaBead         = Color(0xFF6B6A4D)
}

// ═══════════════════════════════════════════════════════════════
// THEME 5: SHAYAN
// Midnight blue, diya-glow gold
// ═══════════════════════════════════════════════════════════════
object ShayanColors {
    val bgPrimary        = Color(0xFF020209)
    val accentPrimary    = Color(0xFFC8D8F0) // moonlight silver-blue
    val accentSecondary  = Color(0xFFFFD080) // diya warm gold
    val accentGold       = Color(0xFFFFD080)
    val accentGlow       = Color(0x14C8D8F0) // 8% alpha
    val malaBead         = Color(0xFF4D5D6B)
}

// ═══════════════════════════════════════════════════════════════
// THEME-AGNOSTIC COLORS (same across all 5 themes)
// ═══════════════════════════════════════════════════════════════

// Glass surfaces — PRD §4.4
val SurfaceGlass       = Color(0x0AFFFFFF) // 4% white
val SurfaceGlassHover  = Color(0x12FFFFFF) // 7% white
val SurfaceGlassActive = Color(0x1AFFFFFF) // 10% white
val BorderGlass        = Color(0x14FFFFFF) // 8% white
val BorderGlassFocus   = Color(0x33FFFFFF) // 20% white

// Text opacity hierarchy — EXACTLY 3 levels
val TextPrimary        = Color(0xFFFFFFFF)            // 100%
val TextSecondary      = Color(0xA6FFFFFF)             // 65%
val TextTertiary       = Color(0x61FFFFFF)             // 38%

// State colors — semantic, universal
val StateExceeded      = Color(0xFFA8E6A0) // green
val StatePartial       = Color(0xFFF5D76E) // yellow
val StateMissed        = Color(0x33FFFFFF) // 20% white
val StateFuture        = Color(0x14FFFFFF) // 8% white
val StateStreakFire     = Color(0xFFFF8C42) // orange

// State backgrounds
val StateExceededBg     = Color(0x1FA8E6A0) // 12%
val StatePartialBg      = Color(0x1FF5D76E) // 12%
val StateMissedBg       = Color(0x0FFFFFFF) // 6%

// Progress track
val ProgressTrack      = Color(0x14FFFFFF) // 8% white

// Navigation
val NavIconInactive    = Color(0x59FFFFFF) // 35% white
val NavLabelInactive   = Color(0x59FFFFFF) // 35% white

// Glass sheet surfaces — high opacity for readability against any background
val SurfaceGlassSheet    = Color(0xD9080810) // ~85% opaque deep indigo-black
val BorderGlassSheet     = Color(0x40FFFFFF) // 25% white — visible edge highlight

// Glass card elevated
val SurfaceGlassElevated = Color(0x14FFFFFF) // 8% white
val SurfaceGlassInput    = Color(0x0FFFFFFF) // 6% white
val BorderGlassInput     = Color(0x24FFFFFF) // 14% white

// Overlay white — text-on-image / glass surfaces
val OverlayWhiteHigh     = Color(0xE6FFFFFF) // 90% white — counter values, stats
val OverlayWhiteMedium   = Color(0x8CFFFFFF) // 55% white — section labels, hints
val OverlayWhiteSubtle   = Color(0xBFFFFFFF) // 75% white — nav button text

// Overlay black — image scrims
val OverlayScrimLight    = Color(0x40000000) // 25% black — top scrim
val OverlayScrimMedium   = Color(0x73000000) // 45% black — counter backdrop
val OverlayScrimHeavy    = Color(0x8C000000) // 55% black — gradient mid
val OverlayScrimDense    = Color(0xE0000000) // 88% black — gradient bottom
