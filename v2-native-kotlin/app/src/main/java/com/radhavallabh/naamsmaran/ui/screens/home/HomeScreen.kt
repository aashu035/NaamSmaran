package com.radhavallabh.naamsmaran.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import com.radhavallabh.naamsmaran.ui.theme.DashboardAlpha
import com.radhavallabh.naamsmaran.ui.theme.OverlayScrimMedium
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteHigh
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteMedium
import com.radhavallabh.naamsmaran.ui.theme.ProgressTrack
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassFocus
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassActive
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.domain.model.DevotionalSectionId
import com.radhavallabh.naamsmaran.engine.HapticEngine
import com.radhavallabh.naamsmaran.ui.components.GlassBottomSheet
import com.radhavallabh.naamsmaran.ui.components.ImageShowreelBackground
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
 * 4. SwipeHint                 — "↑ स्लाईड करें" at bottom, fades after 5 seconds
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
    val galleryState by viewModel.galleryState.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val greetingName by viewModel.greetingName.collectAsState()
    val colors = LocalNaamSmaranColors.current

    val context = LocalContext.current
    val hapticEngine = remember { HapticEngine(context) }

    var sheetStage by remember { mutableStateOf(SheetStage.Hidden) }
    var swipeDeltaY by remember { mutableFloatStateOf(0f) }
    val sheetVisible = sheetStage != SheetStage.Hidden

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

    // C2 fix: capture mutable state as stable refs so pointerInput(Unit) never restarts.
    // Restarting mid-swipe orphaned the touch pipeline and froze all gestures.
    val sheetVisibleRef = rememberUpdatedState(sheetVisible)
    val hapticEnabledRef = rememberUpdatedState(hapticEnabled)

        // ── Tap + Swipe-Up gesture layer ──────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Tap gestures — single tap (+1)
                // pointerInput(Unit): stable key — coroutine never restarts.
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (!sheetVisibleRef.value) {
                                if (hapticEnabledRef.value) {
                                    hapticEngine.playBeadClick()
                                }
                                viewModel.addJap(1)
                            }
                        }
                    )
                }
                // Swipe-up gesture — replaces long-press
                // pointerInput(Unit): stable key — never interrupted by sheet open/close.
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = { swipeDeltaY = 0f },
                        onDragEnd = {
                            // Threshold: 120dp upward swipe
                            if (!sheetVisibleRef.value && swipeDeltaY < -Dimens.SheetSwipeThreshold.toPx()) {
                                viewModel.dismissHint()
                                sheetStage = SheetStageMachine.expand(sheetStage)
                            }
                            swipeDeltaY = 0f
                        },
                        onDragCancel = { swipeDeltaY = 0f },
                        onVerticalDrag = { _, dragAmount ->
                            swipeDeltaY += dragAmount
                        }
                    )
                }
        ) {
            // ── Layer 1: Fullscreen devotional image showreel ──────────────
            if (galleryState.isLoaded) {
                ImageShowreelBackground(
                    modifier = Modifier.fillMaxSize(),
                    galleryUris = galleryState.uris
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.bgPrimary)
                )
            }

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
                visible = sheetStage == SheetStage.Hidden,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                HomeRestingOverlay(
                    did = did,
                    target = target,
                    progress = progress,
                    format = format,
                    greetingName = greetingName,
                    isActive = counterVisible
                )
            }

            // ── Layer 5: Swipe-up hint at bottom ──────────────────────────
            AnimatedVisibility(
                visible = hintVisible && sheetStage == SheetStage.Hidden,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = Dimens.SwipeHintBottomPadding)
            ) {
                Text(
                    text = "↑ स्लाईड करें",
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
            contentAlignment = Alignment.TopCenter
        ) {
            GlassBottomSheet(
                stage = sheetStage,
                onStageChange = { newStage ->
                    if (sheetStage != SheetStage.Hidden && newStage == SheetStage.Hidden) {
                        viewModel.onBottomSheetDismissed()
                    }
                    sheetStage = newStage
                }
            ) {
                BottomSheetContent(
                    record = record,
                    stage = sheetStage,
                    format = format,
                    onQuickAdd = { count ->
                        if (hapticEnabled) {
                            hapticEngine.playCountSuccess()
                        }
                        viewModel.addJap(count)
                    },
                    onNavigateToSection = { index ->
                        sheetStage = SheetStage.Hidden
                        onNavigateToSection(index)
                    },
                    onAddGalleryImage = {
                        if (sheetStage != SheetStage.Hidden) {
                            sheetStage = SheetStage.Hidden
                            viewModel.onBottomSheetDismissed()
                        }
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
        Spacer(modifier = Modifier.height(Dimens.Space1))
        Text(
            text = "${format.format(target)} का $percent%",
            style = NaamSmaranTypography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HomeRestingOverlay(
    did: Long,
    target: Long,
    progress: Float,
    format: NumberFormat,
    greetingName: String,
    isActive: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingCounter(
            did = did,
            target = target,
            progress = progress,
            format = format
        )
        Spacer(modifier = Modifier.height(Dimens.Space3))
        Text(
            text = greetingName,
            style = NaamSmaranTypography.bodyMedium,
            color = if (isActive) TextSecondary else TextPrimary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
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
                shape = CircleShape
            )
            .padding(horizontal = Dimens.Space3, vertical = Dimens.Space2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🔥", fontSize = 14.sp)
        Spacer(modifier = Modifier.width(Dimens.Space1))
        Text(
            text = "${format.format(streak)} दिन",
            style = NaamSmaranTypography.bodySmall,
            color = OverlayWhiteHigh,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** All 7 devotional sections + Settings for the navigation grid. */
private data class SectionEntry(
    val label: String,
    val index: Int,
    val markerText: String? = null
)

private val allSections: List<SectionEntry> = buildList {
    // 7 devotional sections from the single source of truth
    DevotionalSectionId.entries.forEach { section ->
        add(SectionEntry(
            label = section.shortLabel,
            index = section.sectionNumber - 1,  // 0-based index for Screen.fromSectionIndex
            markerText = section.sectionNumber.toDevanagari()
        ))
    }
    // Settings tile at index 7
    add(SectionEntry(label = "सेटिंग्स", index = 7, markerText = "⚙"))
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
    record: DailyRecord?,
    stage: SheetStage,
    format: NumberFormat,
    onQuickAdd: (Long) -> Unit,
    onNavigateToSection: (Int) -> Unit,
    onAddGalleryImage: () -> Unit
) {
    val did = record?.did ?: 0L
    val target = record?.target ?: 21_600L
    val streak = record?.streakCount?.toLong() ?: 0L
    val remaining = (target - did).coerceAtLeast(0)
    val progress = if (target > 0) did.toFloat() / target.toFloat() else 0f
    val completedSections = record?.completedSectionCount() ?: 0

    // Scroll handled by GlassBottomSheet; keep this as intrinsic-height content.
    Column(modifier = Modifier.fillMaxWidth()) {
        DashboardHeader(
            completedSections = completedSections,
            streak = streak,
            format = format
        )

        Spacer(modifier = Modifier.height(Dimens.Space5))

        DashboardSummary(
            did = did,
            target = target,
            remaining = remaining,
            progress = progress,
            streak = streak,
            format = format
        )

        Spacer(modifier = Modifier.height(Dimens.Space5))

        // ── Quick-add label ───────────────────────────────────────────────
        Text(
            text = "जोड़ें",
            style = NaamSmaranTypography.labelLarge,
            color = OverlayWhiteMedium
        )

        Spacer(modifier = Modifier.height(Dimens.Space3))

        // ── Quick-add buttons ─────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PremiumQuickAddButton(
                label = "+१०८",
                style = QuickAddStyle.Ghost,
                modifier = Modifier
                    .weight(0.82f)
                    .height(Dimens.Space12),
                onClick = { onQuickAdd(108) }
            )
            PremiumQuickAddButton(
                label = "+१,०००",
                style = QuickAddStyle.Glass,
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.Space12 + Dimens.Space1),
                onClick = { onQuickAdd(1_000) }
            )
            PremiumQuickAddButton(
                label = "+५,०००",
                style = QuickAddStyle.Primary,
                modifier = Modifier
                    .weight(1.18f)
                    .height(Dimens.Space12 + Dimens.Space2),
                onClick = { onQuickAdd(5_000) }
            )
        }

        if (stage == SheetStage.FullGrid) {
            Spacer(modifier = Modifier.height(Dimens.Space5))

            Text(
                text = "आज की साधना",
                style = NaamSmaranTypography.labelLarge,
                color = OverlayWhiteMedium
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            SectionProgressOverview(record = record, format = format)
        }

        if (stage == SheetStage.FullGrid) {
            Spacer(modifier = Modifier.height(Dimens.Space5))

            Text(
                text = "अनुभाग",
                style = NaamSmaranTypography.labelLarge,
                color = OverlayWhiteMedium
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            SectionPillRail(
                onNavigateToSection = onNavigateToSection,
                onAddGalleryImage = onAddGalleryImage
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space2))
    }
}

@Composable
private fun DashboardHeader(
    completedSections: Int,
    streak: Long,
    format: NumberFormat
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "आज",
                style = NaamSmaranTypography.labelSmall,
                color = OverlayWhiteMedium
            )
            Text(
                text = "साधना डैशबोर्ड",
                style = NaamSmaranTypography.titleMedium,
                color = OverlayWhiteHigh,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Space2)) {
            DashboardPill(label = "${completedSections}/७ पूर्ण")
            if (streak > 0) {
                DashboardPill(label = "${format.format(streak)} दिन")
            }
        }
    }
}

@Composable
private fun DashboardSummary(
    did: Long,
    target: Long,
    remaining: Long,
    progress: Float,
    streak: Long,
    format: NumberFormat
) {
    val colors = LocalNaamSmaranColors.current
    val percent = (progress * 100).toInt().coerceIn(0, 100)
    val streakText = if (streak > 0) "${format.format(streak)} दिन" else "आरंभ"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SurfaceGlassInput,
                shape = RoundedCornerShape(Dimens.Space5)
            )
            .border(
                width = Dimens.Space1 / 4,
                color = colors.accentPrimary.copy(alpha = DashboardAlpha.SummaryBorder),
                shape = RoundedCornerShape(Dimens.Space5)
            )
            .padding(Dimens.Space5)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "राधा नाम जप",
                style = NaamSmaranTypography.titleSmall,
                color = OverlayWhiteHigh,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "अखंडता: $streakText",
                style = NaamSmaranTypography.labelMedium,
                color = colors.accentGold,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space5))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.RingSizeLarge + Dimens.Space2),
            contentAlignment = Alignment.Center
        ) {
            CounterProgressRing(
                did = did,
                percent = percent,
                progress = progress,
                format = format
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space5))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space2)
        ) {
            StatChip(
                label = "लक्ष्य",
                value = format.format(target),
                valueColor = TextTertiary,
                modifier = Modifier.weight(1f)
            )
            StatChip(
                label = "शेष",
                value = format.format(remaining),
                valueColor = colors.accentPrimary,
                modifier = Modifier.weight(1f)
            )
            StatChip(
                label = "अखंडता",
                value = streakText,
                valueColor = colors.accentGold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CounterProgressRing(
    did: Long,
    percent: Int,
    progress: Float,
    format: NumberFormat
) {
    val colors = LocalNaamSmaranColors.current
    val strokeWidth = Dimens.RingStrokeLarge

    Box(
        modifier = Modifier.size(Dimens.RingSizeLarge),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = OverlayWhiteHigh.copy(alpha = DashboardAlpha.RingTrack),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(colors.accentPrimary, colors.accentSecondary, colors.accentPrimary)
                ),
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = format.format(did),
                fontSize = 54.sp,
                lineHeight = 58.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.Space1))
            Text(
                text = "$percent%",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatChip(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = SurfaceGlassInput,
                shape = RoundedCornerShape(Dimens.Space3)
            )
            .border(
                width = Dimens.Space1 / 4,
                color = BorderGlass,
                shape = RoundedCornerShape(Dimens.Space3)
            )
            .padding(horizontal = Dimens.Space2, vertical = Dimens.Space3),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = NaamSmaranTypography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(Dimens.Space1 / 2))
        Text(
            text = label,
            style = NaamSmaranTypography.labelSmall,
            color = TextTertiary,
            maxLines = 1
        )
    }
}

private enum class QuickAddStyle {
    Ghost,
    Glass,
    Primary
}

@Composable
private fun PremiumQuickAddButton(
    label: String,
    style: QuickAddStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalNaamSmaranColors.current
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) Dimens.ScaleTap else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "premium_quick_add_scale"
    )
    val shape = RoundedCornerShape(Dimens.Space4)
    val containerColor = when (style) {
        QuickAddStyle.Ghost -> Color.Transparent
        QuickAddStyle.Glass -> SurfaceGlassActive
        QuickAddStyle.Primary -> Color.Transparent
    }
    val borderColor = when (style) {
        QuickAddStyle.Ghost -> BorderGlassFocus.copy(alpha = DashboardAlpha.GhostButtonBorder)
        QuickAddStyle.Glass -> BorderGlassFocus
        QuickAddStyle.Primary -> Color.Transparent
    }

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(containerColor, shape)
            .border(Dimens.Space1 / 4, borderColor, shape)
            .clip(shape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (style == QuickAddStyle.Primary) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(colors.accentPrimary, colors.accentSecondary)
                        ),
                        shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = NaamSmaranTypography.titleSmall,
                    color = colors.bgPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = label,
                style = if (style == QuickAddStyle.Ghost) {
                    NaamSmaranTypography.labelLarge
                } else {
                    NaamSmaranTypography.titleSmall
                },
                color = if (style == QuickAddStyle.Ghost) TextSecondary else TextPrimary,
                fontWeight = if (style == QuickAddStyle.Ghost) FontWeight.Medium else FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DashboardPill(label: String) {
    val colors = LocalNaamSmaranColors.current

    Box(
        modifier = Modifier
            .background(
                color = colors.accentPrimary.copy(alpha = DashboardAlpha.HeaderPillBackground),
                shape = CircleShape
            )
            .border(
                width = Dimens.Space1 / 4,
                color = colors.accentPrimary.copy(alpha = DashboardAlpha.HeaderPillBorder),
                shape = CircleShape
            )
            .padding(horizontal = Dimens.Space3, vertical = Dimens.Space2)
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.labelSmall,
            color = OverlayWhiteHigh,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SectionProgressOverview(
    record: DailyRecord?,
    format: NumberFormat
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Space2)) {
        sectionDashboardRows(record, format).forEach { item ->
            SectionProgressRow(
                markerText = item.markerText,
                label = item.label,
                progressLabel = item.progressLabel,
                isComplete = item.isComplete,
                progress = item.progress,
                accent = item.accent
            )
        }
    }
}

@Composable
private fun SectionProgressRow(
    markerText: String,
    label: String,
    progressLabel: String,
    isComplete: Boolean,
    progress: Float?,
    accent: DashboardAccent
) {
    val colors = LocalNaamSmaranColors.current
    val accentColor = accent.color(colors)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isComplete) {
                    accentColor.copy(alpha = DashboardAlpha.SectionCompleteBackground)
                } else {
                    SurfaceGlassInput
                },
                shape = RoundedCornerShape(Dimens.Space4)
            )
            .border(
                width = Dimens.Space1 / 4,
                color = if (isComplete) {
                    accentColor.copy(alpha = DashboardAlpha.SectionCompleteBorder)
                } else {
                    BorderGlass
                },
                shape = RoundedCornerShape(Dimens.Space4)
            )
            .drawBehind {
                drawRoundRect(
                    color = accentColor,
                    size = Size(Dimens.Space1.toPx(), size.height),
                    cornerRadius = CornerRadius(Dimens.Space1.toPx(), Dimens.Space1.toPx())
                )
            }
            .padding(horizontal = Dimens.Space3, vertical = Dimens.Space3)
            .padding(start = Dimens.Space2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.Space8)
                .background(accentColor.copy(alpha = DashboardAlpha.SectionMarkerBackground), CircleShape)
                .border(
                    Dimens.Space1 / 4,
                    accentColor.copy(alpha = DashboardAlpha.SectionMarkerBorder),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = markerText,
                style = NaamSmaranTypography.labelLarge,
                color = accentColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(Dimens.Space3))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = NaamSmaranTypography.bodyMedium,
                    color = OverlayWhiteHigh,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(Dimens.Space2))
                Text(
                    text = progressLabel,
                    style = NaamSmaranTypography.labelSmall,
                    color = if (isComplete) accentColor else TextTertiary,
                    fontWeight = if (isComplete) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1
                )
            }

            if (progress != null) {
                Spacer(modifier = Modifier.height(Dimens.Space2))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Space1)
                        .clip(CircleShape)
                        .background(ProgressTrack)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .height(Dimens.Space1)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionPillRail(
    onNavigateToSection: (Int) -> Unit,
    onAddGalleryImage: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(end = Dimens.Space1),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space2)
    ) {
        items(allSections) { section ->
            SectionPill(
                label = "${section.markerText.orEmpty()} ${section.label.replace('\n', ' ')}",
                isAccent = section.index == 0,
                onClick = { onNavigateToSection(section.index) }
            )
        }
        item {
            SectionPill(
                label = "▧ चित्र गैलरी",
                isAccent = false,
                onClick = onAddGalleryImage
            )
        }
    }
}

@Composable
private fun SectionPill(
    label: String,
    isAccent: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalNaamSmaranColors.current
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) Dimens.ScaleTap else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "section_pill_scale"
    )
    val backgroundColor = if (isAccent) {
        colors.accentPrimary.copy(alpha = DashboardAlpha.ActivePillBackground)
    } else {
        SurfaceGlassInput
    }
    val borderColor = if (isAccent) {
        colors.accentPrimary.copy(alpha = DashboardAlpha.ActivePillBorder)
    } else {
        BorderGlass
    }
    val textColor = if (isAccent) colors.accentPrimary else TextSecondary

    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(backgroundColor, CircleShape)
            .border(Dimens.Space1 / 4, borderColor, CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    },
                    onTap = { onClick() }
                )
            }
            .padding(horizontal = Dimens.Space4, vertical = Dimens.Space2),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.labelMedium,
            color = textColor,
            fontWeight = if (isAccent) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

private enum class DashboardAccent {
    Primary,
    Secondary,
    Gold;

    fun color(colors: com.radhavallabh.naamsmaran.ui.theme.NaamSmaranColorScheme): Color = when (this) {
        Primary -> colors.accentPrimary
        Secondary -> colors.accentSecondary
        Gold -> colors.accentGold
    }
}

private data class DashboardSectionRow(
    val markerText: String,
    val label: String,
    val progressLabel: String,
    val isComplete: Boolean,
    val progress: Float?,
    val accent: DashboardAccent
)

private fun sectionDashboardRows(
    record: DailyRecord?,
    format: NumberFormat
): List<DashboardSectionRow> = listOf(
    DashboardSectionRow(
        markerText = "१",
        label = "नाम जप",
        progressLabel = "${format.format(record?.did ?: 0L)}/${format.format(record?.target ?: 21_600L)}",
        isComplete = record?.checkNaamJap == true,
        progress = progressOf(record?.did ?: 0L, record?.target ?: 21_600L),
        accent = DashboardAccent.Primary
    ),
    DashboardSectionRow(
        markerText = "२",
        label = "श्री हित चतुरसी जी",
        progressLabel = "${format.format(record?.chaturasi_did ?: 0L)}/${format.format(record?.chaturasi_target ?: 12L)}",
        isComplete = record?.checkChaturasi == true,
        progress = progressOf(record?.chaturasi_did ?: 0L, record?.chaturasi_target ?: 12L),
        accent = DashboardAccent.Secondary
    ),
    DashboardSectionRow(
        markerText = "३",
        label = "श्री हित राधा सुधानिधी जी स्तोत्र",
        progressLabel = "${format.format(record?.sudhanidhi_did ?: 0L)}/${format.format(record?.sudhanidhi_target ?: 10L)}",
        isComplete = record?.checkSudhanidhi == true,
        progress = progressOf(record?.sudhanidhi_did ?: 0L, record?.sudhanidhi_target ?: 10L),
        accent = DashboardAccent.Gold
    ),
    DashboardSectionRow(
        markerText = "४",
        label = "श्री हित सेवक वाणी",
        progressLabel = "${format.format(record?.sevakVani_did ?: 0L)}/${format.format(record?.sevakVani_target ?: 5L)}",
        isComplete = record?.checkSevakVani == true,
        progress = progressOf(record?.sevakVani_did ?: 0L, record?.sevakVani_target ?: 5L),
        accent = DashboardAccent.Primary
    ),
    DashboardSectionRow(
        markerText = "५",
        label = "अष्टयाम सेवा पद्धति",
        progressLabel = if (record?.checkAshtayamSeva == true) "पूर्ण" else "शेष",
        isComplete = record?.checkAshtayamSeva == true,
        progress = null,
        accent = DashboardAccent.Secondary
    ),
    DashboardSectionRow(
        markerText = "६",
        label = "नित्य पाठ रसोपासना",
        progressLabel = if (record?.checkNityaPath == true) "पूर्ण" else "शेष",
        isComplete = record?.checkNityaPath == true,
        progress = null,
        accent = DashboardAccent.Gold
    ),
    DashboardSectionRow(
        markerText = "७",
        label = "श्री वृंदावन शत लीला",
        progressLabel = "${format.format(record?.vrindavan_did ?: 0L)}/${format.format(record?.vrindavan_target ?: 10L)}",
        isComplete = record?.checkVrindavan == true,
        progress = progressOf(record?.vrindavan_did ?: 0L, record?.vrindavan_target ?: 10L),
        accent = DashboardAccent.Primary
    )
)

private fun DailyRecord.completedSectionCount(): Int = listOf(
    checkNaamJap,
    checkChaturasi,
    checkSudhanidhi,
    checkSevakVani,
    checkAshtayamSeva,
    checkNityaPath,
    checkVrindavan
).count { it }

private fun progressOf(did: Long, target: Long): Float =
    if (target > 0L) did.toFloat() / target.toFloat() else 0f

private fun Int.toDevanagari(): String {
    val map = mapOf(
        '0' to '०',
        '1' to '१',
        '2' to '२',
        '3' to '३',
        '4' to '४',
        '5' to '५',
        '6' to '६',
        '7' to '७',
        '8' to '८',
        '9' to '९'
    )
    return toString().map { map[it] ?: it }.joinToString("")
}
