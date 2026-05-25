package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.screens.home.SheetStage
import com.radhavallabh.naamsmaran.ui.screens.home.SheetStageMachine
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

@Composable
fun GlassBottomSheet(
    modifier: Modifier = Modifier,
    stage: SheetStage,
    onStageChange: (SheetStage) -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val hiddenOffset = screenHeightPx * Dimens.SheetHiddenOffsetFraction
    val quickActionsOffset = screenHeightPx * Dimens.SheetQuickActionsOffsetFraction
    val fullGridOffset = screenHeightPx * Dimens.SheetFullGridOffsetFraction

    val baseOffsetPx = when (stage) {
        SheetStage.Hidden -> hiddenOffset
        SheetStage.QuickActions -> quickActionsOffset
        SheetStage.FullGrid -> fullGridOffset
    }

    var dragOffsetPx by remember(stage) { mutableFloatStateOf(0f) }
    val swipeThresholdPx = screenHeightPx * Dimens.SheetDragThresholdFraction

    val animatedOffset by animateDpAsState(
        targetValue = with(density) { baseOffsetPx.toDp() },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sheet_stage_offset"
    )

    val draggableState = rememberDraggableState { delta ->
        dragOffsetPx += delta
    }
    val sheetScroll = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset {
                val basePx = with(density) { animatedOffset.toPx() }
                val shownPx = (basePx + dragOffsetPx).coerceIn(fullGridOffset, hiddenOffset)
                IntOffset(0, shownPx.toInt())
            }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = draggableState,
                        onDragStarted = { dragOffsetPx = 0f },
                        onDragStopped = { velocity ->
                            val nextStage = when {
                                velocity < -1_000f || dragOffsetPx < -swipeThresholdPx ->
                                    SheetStageMachine.expand(stage)
                                velocity > 1_000f || dragOffsetPx > swipeThresholdPx ->
                                    SheetStageMachine.collapse(stage)
                                else -> stage
                            }
                            dragOffsetPx = 0f
                            if (nextStage != stage) {
                                onStageChange(nextStage)
                            }
                        }
                    )
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(sheetScroll),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Spacer(modifier = Modifier.height(Dimens.PaddingCard))
            }
        }
    }
}
