package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.screens.home.SheetStage
import com.radhavallabh.naamsmaran.ui.screens.home.SheetStageMachine
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import kotlinx.coroutines.launch

/**
 * GlassBottomSheet — controlled bottom sheet component.
 * Features:
 * - 3-stage anchored positions (Hidden, QuickActions, FullGrid)
 * - Translucent glassmorphism (SurfaceGlassSheet) and 25% white border
 * - Stateless design: updates and releases flow through onStageChange
 *
 * Smooth-Drag Refactor (Merge A+C):
 * - Uses Compose Animatable to preserve exact touch coordinates on release
 * - Eliminates sudden snap-backs and layout recoil jumps
 * - Disables inner scroll list when in non-expanded stages, allowing drag gestures
 *   across the entire surface of the sheet without freezing.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun GlassBottomSheet(
    modifier: Modifier = Modifier,
    stage: SheetStage,
    onStageChange: (SheetStage) -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    // Use BoxWithConstraints so height is measured from the actual rendered parent,
    // not from configuration.screenHeightDp (which excludes status bar on some devices
    // and mismatches the real window offset coordinates).
    BoxWithConstraints(modifier = modifier.fillMaxWidth().fillMaxHeight()) {
        val screenHeightPx = with(density) { maxHeight.toPx() }

        val hiddenOffset = screenHeightPx * Dimens.SheetHiddenOffsetFraction
        val quickActionsOffset = screenHeightPx * Dimens.SheetQuickActionsOffsetFraction
        val fullGridOffset = screenHeightPx * Dimens.SheetFullGridOffsetFraction

        val targetOffset = when (stage) {
            SheetStage.Hidden -> hiddenOffset
            SheetStage.QuickActions -> quickActionsOffset
            SheetStage.FullGrid -> fullGridOffset
        }

        val coroutineScope = rememberCoroutineScope()
        val offsetAnimatable = remember { Animatable(targetOffset) }

        LaunchedEffect(stage) {
            offsetAnimatable.animateTo(
                targetValue = targetOffset,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }

        val swipeThresholdPx = screenHeightPx * Dimens.SheetDragThresholdFraction

        val draggableState = rememberDraggableState { delta ->
            coroutineScope.launch {
                val newOffset = (offsetAnimatable.value + delta).coerceIn(fullGridOffset, hiddenOffset)
                offsetAnimatable.snapTo(newOffset)
            }
        }
        val sheetScroll = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset {
                    IntOffset(0, offsetAnimatable.value.toInt())
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = draggableState,
                    onDragStarted = { /* SnapTo handled inside draggableState callback */ },
                    onDragStopped = { velocity ->
                        val currentOffset = offsetAnimatable.value
                        val dragDistance = currentOffset - targetOffset
                        val nextStage = when {
                            velocity < -1_000f || dragDistance < -swipeThresholdPx ->
                                SheetStageMachine.expand(stage)
                            velocity > 1_000f || dragDistance > swipeThresholdPx ->
                                SheetStageMachine.collapse(stage)
                            else -> stage
                        }
                        if (nextStage != stage) {
                            onStageChange(nextStage)
                        } else {
                            coroutineScope.launch {
                                offsetAnimatable.animateTo(
                                    targetValue = targetOffset,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        }
                    }
                )
                .clip(
                    RoundedCornerShape(
                        topStart = Dimens.SheetCornerRadius,
                        topEnd = Dimens.SheetCornerRadius
                    )
                )
                .background(SurfaceGlassSheet)
                .border(
                    width = 1.dp,
                    color = BorderGlassSheet,
                    shape = RoundedCornerShape(
                        topStart = Dimens.SheetCornerRadius,
                        topEnd = Dimens.SheetCornerRadius
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingCard)
                    .navigationBarsPadding()
            ) {
                // Drag handle area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Space3, bottom = Dimens.Space2),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(Dimens.SheetHandleWidth)
                            .height(Dimens.Space1)
                            .clip(CircleShape)
                            .background(TextTertiary)
                    )
                }

                // Scrollable Content Column — scroll active only when fully expanded
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (stage == SheetStage.FullGrid) {
                                Modifier.verticalScroll(sheetScroll)
                            } else {
                                Modifier
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                    Spacer(modifier = Modifier.height(Dimens.PaddingCard))
                }
            }
        }
    }
}
