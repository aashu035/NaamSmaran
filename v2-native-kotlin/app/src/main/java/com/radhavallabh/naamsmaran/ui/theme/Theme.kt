package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Naam Smaran — Theme System
 *
 * 5 sacred themes from the Radhavallabh Sampraday visual world.
 * Each theme provides a NaamSmaranColorScheme (our extended palette)
 * which is exposed via CompositionLocal for use throughout the app.
 *
 * Usage in composables:
 *   val colors = LocalNaamSmaranColors.current
 *   colors.accentPrimary
 *   colors.bgPrimary
 */

// ═══════════════════════════════════════════════════════════════
// Theme identifier enum
// ═══════════════════════════════════════════════════════════════
enum class NaamSmaranThemeId(val displayName: String, val displayNameHindi: String) {
    SHARAD_MOON("Sharad Moon", "शरद पूर्णिमा"),
    VRINDAVAN_DAWN("Vrindavan Dawn", "वृन्दावन प्रभात"),
    NIKUNJ("Nikunj", "निकुञ्ज"),
    VAN_VIHAAR("Van Vihaar", "वन विहार"),
    SHAYAN("Shayan", "शयन")
}

// ═══════════════════════════════════════════════════════════════
// Extended color scheme — beyond Material3's built-in palette
// ═══════════════════════════════════════════════════════════════
data class NaamSmaranColorScheme(
    val bgPrimary: Color,
    val accentPrimary: Color,
    val accentSecondary: Color,
    val accentGold: Color,
    val accentGlow: Color,
    val malaBead: Color,

    // Glass surfaces (same for all themes)
    val surfaceGlass: Color = SurfaceGlass,
    val surfaceGlassHover: Color = SurfaceGlassHover,
    val surfaceGlassActive: Color = SurfaceGlassActive,
    val borderGlass: Color = BorderGlass,
    val borderGlassFocus: Color = BorderGlassFocus,

    // Text (same for all themes)
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val textTertiary: Color = TextTertiary,

    // States (same for all themes)
    val stateExceeded: Color = StateExceeded,
    val statePartial: Color = StatePartial,
    val stateMissed: Color = StateMissed,
    val stateStreakFire: Color = StateStreakFire,
)

// CompositionLocal for providing our extended color scheme
val LocalNaamSmaranColors = staticCompositionLocalOf {
    sharadMoonScheme // Default
}

// ═══════════════════════════════════════════════════════════════
// Pre-built color scheme instances
// ═══════════════════════════════════════════════════════════════
val sharadMoonScheme = NaamSmaranColorScheme(
    bgPrimary = SharadMoonColors.bgPrimary,
    accentPrimary = SharadMoonColors.accentPrimary,
    accentSecondary = SharadMoonColors.accentSecondary,
    accentGold = SharadMoonColors.accentGold,
    accentGlow = SharadMoonColors.accentGlow,
    malaBead = SharadMoonColors.malaBead,
)

val vrindavanDawnScheme = NaamSmaranColorScheme(
    bgPrimary = VrindavanDawnColors.bgPrimary,
    accentPrimary = VrindavanDawnColors.accentPrimary,
    accentSecondary = VrindavanDawnColors.accentSecondary,
    accentGold = VrindavanDawnColors.accentGold,
    accentGlow = VrindavanDawnColors.accentGlow,
    malaBead = VrindavanDawnColors.malaBead,
)

val nikunjScheme = NaamSmaranColorScheme(
    bgPrimary = NikunjColors.bgPrimary,
    accentPrimary = NikunjColors.accentPrimary,
    accentSecondary = NikunjColors.accentSecondary,
    accentGold = NikunjColors.accentGold,
    accentGlow = NikunjColors.accentGlow,
    malaBead = NikunjColors.malaBead,
)

val vanVihaarScheme = NaamSmaranColorScheme(
    bgPrimary = VanVihaarColors.bgPrimary,
    accentPrimary = VanVihaarColors.accentPrimary,
    accentSecondary = VanVihaarColors.accentSecondary,
    accentGold = VanVihaarColors.accentGold,
    accentGlow = VanVihaarColors.accentGlow,
    malaBead = VanVihaarColors.malaBead,
)

val shayanScheme = NaamSmaranColorScheme(
    bgPrimary = ShayanColors.bgPrimary,
    accentPrimary = ShayanColors.accentPrimary,
    accentSecondary = ShayanColors.accentSecondary,
    accentGold = ShayanColors.accentGold,
    accentGlow = ShayanColors.accentGlow,
    malaBead = ShayanColors.malaBead,
)

fun colorSchemeForTheme(themeId: NaamSmaranThemeId): NaamSmaranColorScheme = when (themeId) {
    NaamSmaranThemeId.SHARAD_MOON -> sharadMoonScheme
    NaamSmaranThemeId.VRINDAVAN_DAWN -> vrindavanDawnScheme
    NaamSmaranThemeId.NIKUNJ -> nikunjScheme
    NaamSmaranThemeId.VAN_VIHAAR -> vanVihaarScheme
    NaamSmaranThemeId.SHAYAN -> shayanScheme
}

// ═══════════════════════════════════════════════════════════════
// The root theme composable
// ═══════════════════════════════════════════════════════════════
@Composable
fun NaamSmaranTheme(
    themeId: NaamSmaranThemeId = NaamSmaranThemeId.SHARAD_MOON,
    content: @Composable () -> Unit
) {
    val naamSmaranColors = colorSchemeForTheme(themeId)

    // Map our palette onto Material3's dark scheme for components
    val materialColors = darkColorScheme(
        primary = naamSmaranColors.accentPrimary,
        secondary = naamSmaranColors.accentSecondary,
        tertiary = naamSmaranColors.accentGold,
        background = naamSmaranColors.bgPrimary,
        surface = naamSmaranColors.bgPrimary,
        onPrimary = naamSmaranColors.bgPrimary,
        onSecondary = naamSmaranColors.bgPrimary,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        surfaceVariant = naamSmaranColors.surfaceGlass,
        outline = naamSmaranColors.borderGlass,
    )

    CompositionLocalProvider(
        LocalNaamSmaranColors provides naamSmaranColors
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = NaamSmaranTypography,
            shapes = NaamSmaranShapes,
            content = content
        )
    }
}
