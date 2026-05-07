package com.radhavallabh.naamsmaran.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.radhavallabh.naamsmaran.ui.theme.OverlayScrimMedium
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteHigh
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteMedium
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.domain.model.DevotionalSectionId
import com.radhavallabh.naamsmaran.engine.HapticEngine
import com.radhavallabh.naamsmaran.ui.components.GlassBottomSheet
import com.radhavallabh.naamsmaran.ui.components.ImageShowreelBackground
import com.radhavallabh.naamsmaran.ui.components.QuickAddButton
import com.radhavallabh.naamsmaran.ui.components.SectionNavButton
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import java.text.NumberFormat
import java.util.Locale

/**
 * HomeScreen — Zero-UI Darshan Experience (v2.0-darshan).
 *
 * Layout (back to front):
 * 1. [ImageShowreelBackground] — fullscreen infinite crossfade of devotional images
 * 2. [StreakBadge]             — top-right streak badge (always visible, subtle)
 * 3. [FloatingCounter]         — animated-in/out counter overlay (center)
 * 4. SwipeHint                 — "↑ स्लाइड करें" at bottom, fades after 5 seconds
 * 5. [GlassBottomSheet]        — swipeable quick-add + section nav panel
 *
 * Interaction model:
 * - Single tap anywhere → +1 jap (haptic bead click) + flash counter
 * - Swipe up (≥120dp)  → opens bottom sheet (replaces long-press)
 * - Bottom sheet dismiss → re-flashes counter for 3.5s
 *
 * (Note: Double tap for +108 was removed to allow flawless rapid single tapping).
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun HomeScreen(
    onNavigateToSection: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val record by viewModel.todayRecord.collectAsState()
    val counterVisible by viewModel.counterVisible.collectAsState()
    val hintVisible by viewModel.hintVisible.collectAsState()
    val galleryUris by viewModel.galleryImageUris.collectAsState()

    val context = LocalContext.current
    val hapticEngine = remember { HapticEngine(context) }

    var sheetOpen by remember { mutableStateOf(false) }
    var swipeDeltaY by remember { mutableFloatStateOf(0f) }

    // Gallery picker launcher — persists read URI permission
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            // Take persistent permission so we can read this URI across sessions
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.addGalleryImage(uri)
        }
    }

    val format = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))

    val target = record?.target ?: 21_600L
    val did = record?.did ?: 0L
    val streak = record?.streakCount?.toLong() ?: 0L
    val progress = if (target > 0) did.toFloat() / target.toFloat() else 0f

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Tap + Swipe-Up gesture layer ──────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Tap gestures — single tap (+1)
                .pointerInput(sheetOpen) {
                    if (!sheetOpen) {
                        detectTapGestures(
                            onTap = {
                                hapticEngine.playBeadClick()
                                viewModel.addJap(1)
                            }
                        )
                    }
                }
                // Swipe-up gesture — replaces long-press
                .pointerInput(sheetOpen) {
                    if (!sheetOpen) {
                        detectVerticalDragGestures(
                            onDragStart = { swipeDeltaY = 0f },
                            onDragEnd = {
                                // Threshold: 120dp upward swipe
                                if (swipeDeltaY < -120.dp.toPx()) {
                                    viewModel.dismissHint()
                                    sheetOpen = true
                                }
                                swipeDeltaY = 0f
                            },
                            onDragCancel = { swipeDeltaY = 0f },
                            onVerticalDrag = { _, dragAmount ->
                                swipeDeltaY += dragAmount
                            }
                        )
                    }
                }
        ) {
            // ── Layer 1: Fullscreen devotional image showreel ──────────────
            ImageShowreelBackground(
                modifier = Modifier.fillMaxSize(),
                galleryUris = galleryUris
            )

            // ── Layer 2: Streak badge (top-right) ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = Dimens.Space2, end = Dimens.Space4),
                contentAlignment = Alignment.TopEnd
            ) {
                StreakBadge(streak = streak, format = format)
            }

            // ── Layer 3: Floating Counter (center) ────────────────────────
            AnimatedVisibility(
                visible = counterVisible,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                FloatingCounter(
                    did = did,
                    target = target,
                    progress = progress,
                    format = format
                )
            }

            // ── Layer 4: "राधे राधे" resting text (when counter hidden) ──
            AnimatedVisibility(
                visible = !counterVisible && !sheetOpen,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = Dimens.Space8)
            ) {
                Text(
                    text = "राधे राधे",
                    style = NaamSmaranTypography.displaySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )
            }

            // ── Layer 5: Swipe-up hint at bottom ──────────────────────────
            AnimatedVisibility(
                visible = hintVisible && !sheetOpen,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 32.dp) // comfortable clearance above system nav bar
            ) {
                Text(
                    text = "↑ स्लाइड करें",
                    style = NaamSmaranTypography.bodySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
            }
        }

        // ── Layer 6: Glass Bottom Sheet ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlassBottomSheet(
                isExpanded = sheetOpen,
                onDismiss = {
                    sheetOpen = false
                    viewModel.onBottomSheetDismissed()
                }
            ) {
                BottomSheetContent(
                    did = did,
                    target = target,
                    streak = streak,
                    format = format,
                    onQuickAdd = { count ->
                        hapticEngine.playCountSuccess()
                        viewModel.addJap(count)
                    },
                    onNavigateToSection = { index ->
                        sheetOpen = false
                        onNavigateToSection(index)
                    },
                    onAddGalleryImage = {
                        galleryLauncher.launch(arrayOf("image/*"))
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-composables
// ─────────────────────────────────────────────────────────────────────────────

/** Floating counter — big bold number + target fraction. */
@Composable
private fun FloatingCounter(
    did: Long,
    target: Long,
    progress: Float,
    format: NumberFormat
) {
    val percent = (progress * 100).toInt().coerceIn(0, 100)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = format.format(did),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-1).sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${format.format(target)} का $percent%",
            style = NaamSmaranTypography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

/** Pill-shaped streak badge for the top-right corner. */
@Composable
private fun StreakBadge(streak: Long, format: NumberFormat) {
    if (streak <= 0L) return

    Row(
        modifier = Modifier
            .background(
                color = OverlayScrimMedium,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🔥", fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${format.format(streak)} दिन",
            style = NaamSmaranTypography.bodySmall,
            color = OverlayWhiteHigh,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** All 7 devotional sections + Settings for the navigation grid. */
private data class SectionEntry(val emoji: String, val label: String, val index: Int)

private val allSections: List<SectionEntry> = buildList {
    // 7 devotional sections from the single source of truth
    DevotionalSectionId.entries.forEach { section ->
        add(SectionEntry(
            emoji = section.emoji,
            label = section.shortLabel,
            index = section.sectionNumber - 1  // 0-based index for Screen.fromSectionIndex
        ))
    }
    // Settings tile at index 7
    add(SectionEntry(emoji = "⚙️", label = "सेटिंग्स", index = 7))
}

/**
 * Bottom sheet content:
 * - Stats row (किया / लक्ष्य / शेष / अखंडता)
 * - Quick-add buttons (+१०८ / +१,००० / +५,०००)
 * - Section navigation grid (all 7 sections)
 * - Gallery picker link
 */
@Composable
private fun BottomSheetContent(
    did: Long,
    target: Long,
    streak: Long,
    format: NumberFormat,
    onQuickAdd: (Long) -> Unit,
    onNavigateToSection: (Int) -> Unit,
    onAddGalleryImage: () -> Unit
) {
    val remaining = (target - did).coerceAtLeast(0)

    // ── Section header ────────────────────────────────────────────────────
    Text(
        text = "राधा नाम जप",
        style = NaamSmaranTypography.titleMedium,
        color = OverlayWhiteHigh,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(Dimens.Space2))

    // ── Stats row ─────────────────────────────────────────────────────────
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(label = "किया",     value = format.format(did))
        StatItem(label = "लक्ष्य",   value = format.format(target))
        StatItem(label = "शेष",      value = format.format(remaining))
        if (streak > 0) StatItem(label = "अखंडता", value = "${format.format(streak)}d")
    }

    Spacer(modifier = Modifier.height(Dimens.Space6))

    // ── Quick-add label ───────────────────────────────────────────────────
    Text(
        text = "जोड़ें",
        style = NaamSmaranTypography.labelLarge,
        color = OverlayWhiteMedium
    )

    Spacer(modifier = Modifier.height(Dimens.Space3))

    // ── Quick-add buttons ─────────────────────────────────────────────────
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
    ) {
        QuickAddButton(label = "+१०८",   modifier = Modifier.weight(1f)) { onQuickAdd(108) }
        QuickAddButton(label = "+१,०००", modifier = Modifier.weight(1f)) { onQuickAdd(1_000) }
        QuickAddButton(label = "+५,०००", modifier = Modifier.weight(1f)) { onQuickAdd(5_000) }
    }

    Spacer(modifier = Modifier.height(Dimens.Space6))

    // ── Section navigation label ──────────────────────────────────────────
    Text(
        text = "अनुभाग",
        style = NaamSmaranTypography.labelLarge,
        color = OverlayWhiteMedium
    )

    Spacer(modifier = Modifier.height(Dimens.Space3))

    // ── 7-section navigation grid (4 columns) ─────────────────────────────
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space2),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(allSections) { section ->
            SectionNavButton(
                emoji = section.emoji,
                label = section.label,
                onClick = { onNavigateToSection(section.index) }
            )
        }

        // Gallery picker tile at the end
        item {
            SectionNavButton(
                emoji = "🖼️",
                label = "गैलरी\nजोड़ें",
                onClick = onAddGalleryImage
            )
        }
    }

    Spacer(modifier = Modifier.height(Dimens.Space2))
}

/** One stat column (value above, label below). */
@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = NaamSmaranTypography.titleMedium,
            color = OverlayWhiteHigh,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = NaamSmaranTypography.bodySmall,
            color = OverlayWhiteMedium
        )
    }
}
