package com.radhavallabh.naamsmaran.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Naam Smaran — Typography
 *
 * Uses system default fonts (which include Noto Sans Devanagari on all
 * Android 8+ devices). Bundled fonts will be re-introduced after
 * validating the TTF files on-device.
 *
 * Three logical families:
 * 1. DevanagariUi — Hindi labels → system default (includes Devanagari)
 * 2. DevanagariDeco — decorative Hindi → system Serif
 * 3. NumberFont — counters, English tech → system SansSerif
 *
 * Only two weights: 400 (Regular) and 700 (Bold). Never 500, never 600.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// ═══════════════════════════════════════════════════════════════
// Font families — System fonts (guaranteed on Android 8+)
// ═══════════════════════════════════════════════════════════════
val DevanagariUi = FontFamily.Default       // System renders Devanagari natively
val DevanagariDeco = FontFamily.Serif       // Serif variant for decorative use
val NumberFont = FontFamily.SansSerif       // Clean numbers

/**
 * Type scale matching tokens.css exactly:
 * --text-hero:      44px  → displayLarge
 * --text-large:     28px  → headlineLarge
 * --text-medium:    20px  → headlineSmall
 * --text-body:      16px  → bodyLarge
 * --text-secondary: 14px  → bodyMedium
 * --text-tertiary:  12px  → bodySmall
 * --text-label:     10px  → labelSmall
 */
val NaamSmaranTypography = Typography(
    // Hero — today's Naam Jap count, splash central number
    displayLarge = TextStyle(
        fontFamily = NumberFont,
        fontWeight = FontWeight.W700,
        fontSize = 44.sp,
        lineHeight = (44 * 1.2).sp,
        letterSpacing = (-0.02).sp
    ),

    // Sub-hero — resting "राधे राधे" hint text
    displaySmall = TextStyle(
        fontFamily = DevanagariDeco,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = (32 * 1.3).sp,
        letterSpacing = 0.sp
    ),

    // Large — section headings, day target
    headlineLarge = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = (28 * 1.2).sp
    ),

    // Medium — card titles, streak count
    headlineSmall = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = (20 * 1.5).sp
    ),

    // Large screen titles, section top headers
    titleLarge = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        lineHeight = (22 * 1.4).sp,
        letterSpacing = 0.sp
    ),

    // Section sub-headers, bottom sheet labels
    titleMedium = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = (18 * 1.4).sp,
        letterSpacing = 0.sp
    ),

    // Card sub-headings, compact section titles
    titleSmall = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 15.sp,
        lineHeight = (15 * 1.4).sp,
        letterSpacing = 0.sp
    ),

    // Medium headlines, mid-size numeric display
    headlineMedium = TextStyle(
        fontFamily = NumberFont,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = (24 * 1.2).sp
    ),

    // Body — primary body text, pad content
    bodyLarge = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = (16 * 1.5).sp
    ),

    // Supporting labels, timestamps
    bodyMedium = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = (14 * 1.5).sp
    ),

    // Hints, metadata
    bodySmall = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = (12 * 1.5).sp
    ),

    // Section category labels, caps-style UI
    labelLarge = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = (14 * 1.2).sp,
        letterSpacing = 0.08.sp
    ),

    // Medium labels, toggle labels, setting sub-labels
    labelMedium = TextStyle(
        fontFamily = DevanagariUi,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = (12 * 1.4).sp,
        letterSpacing = 0.06.sp
    ),

    // Badges, tiny chips
    labelSmall = TextStyle(
        fontFamily = NumberFont,
        fontWeight = FontWeight.W700,
        fontSize = 10.sp,
        lineHeight = (10 * 1.2).sp,
        letterSpacing = 0.05.sp
    )
)
