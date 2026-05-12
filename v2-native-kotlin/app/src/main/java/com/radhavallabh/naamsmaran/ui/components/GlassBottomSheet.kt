package com.radhavallabh.naamsmaran.ui.components

import android.os.SystemClock
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassSheet
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * GlassBottomSheet — Premium frosted glass bottom sheet.
 *
 * Behaviour (review feedback):
 * - **Binary snap only** (open ⟷ peek) — open snaps to ~85 % viewport (near full), not mid-screen.
 * - **No bounce** on open (avoids “springs back” look from [DampingRatioMediumBouncy]).
 * - **Ignores spurious drag-ended** for a few hundred ms right after programmatic open
 *   (swipe-up-from-home can leak into the sheet draggable and snap to the wrong point).
 * - **Scroll** lives in the sheet body; drag-to-resize uses the **handle strip only** so
 *   vertical scroll gestures are not eaten by the outer draggable.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun GlassBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Top edge Y offset (px, positive = down). Smaller ⇒ sheet sits higher ⇒ more viewport used.
    // 0.08 opens near full viewport so section grid + gallery are visible without a second expand step.
    val collapsedOffset = screenHeightPx * 0.92f
    val openedOffset = screenHeightPx * 0.08f
    val midpoint = (openedOffset + collapsedOffset) / 2f

    var currentTarget by remember {
        mutableStateOf(if (isExpanded) openedOffset else collapsedOffset)
    }

    /** Drop drag-end snap briefly after open-from-home to avoid instant re-snap “recoil”. */
    var suppressDragEndUntilMs by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            suppressDragEndUntilMs = SystemClock.uptimeMillis() + 420L
            currentTarget = openedOffset
        } else {
            currentTarget = collapsedOffset
        }
    }

    val animatedOffset by animateDpAsState(
        targetValue = with(density) { currentTarget.toDp() },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sheet_offset"
    )

    var dragAccumulator by remember { mutableStateOf(0f) }

    val sheetScroll = rememberScrollState()
    val draggableState = rememberDraggableState { delta ->
        dragAccumulator += delta
        val newTarget = (currentTarget + delta).coerceIn(openedOffset, collapsedOffset)
        currentTarget = newTarget
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(0, animatedOffset.roundToPx()) }
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(SurfaceGlassSheet)
            .border(
                width = 1.dp,
                color = BorderGlassSheet,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingCard)
                .navigationBarsPadding()
        ) {
            // ── Handle strip: only this region drags the sheet (body scrolls independently) ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = draggableState,
                        onDragStarted = { dragAccumulator = 0f },
                        onDragStopped = { velocity ->
                            if (SystemClock.uptimeMillis() < suppressDragEndUntilMs) {
                                // #region agent log
                                com.radhavallabh.naamsmaran.debug.AgentDebugLog.log(
                                    location = "GlassBottomSheet.kt:onDragStopped",
                                    message = "drag_stopped_suppressed",
                                    hypothesisId = "H3",
                                    data = mapOf(
                                        "velocity" to velocity,
                                        "currentTargetPx" to currentTarget
                                    ),
                                    runId = "post-fix"
                                )
                                // #endregion
                                dragAccumulator = 0f
                                return@draggable
                            }
                            val oldTargetPx = currentTarget
                            val (branch, newTarget) = when {
                                velocity > 1000f ->
                                    "velocity_gt_1000_collapse" to collapsedOffset
                                velocity < -1000f ->
                                    "velocity_lt_neg1000_open" to openedOffset
                                currentTarget < midpoint ->
                                    "snap_open" to openedOffset
                                else ->
                                    "snap_collapse" to collapsedOffset
                            }
                            if (newTarget == collapsedOffset) {
                                onDismiss()
                            }
                            // #region agent log
                            com.radhavallabh.naamsmaran.debug.AgentDebugLog.log(
                                location = "GlassBottomSheet.kt:onDragStopped",
                                message = "drag_stopped_snap",
                                hypothesisId = "H1",
                                data = mapOf(
                                    "velocity" to velocity,
                                    "branch" to branch,
                                    "oldTargetPx" to oldTargetPx,
                                    "newTargetPx" to newTarget,
                                    "collapsedPx" to collapsedOffset,
                                    "onDismissForCollapse" to (newTarget == collapsedOffset)
                                ),
                                runId = "post-fix"
                            )
                            // #endregion
                            currentTarget = newTarget
                            dragAccumulator = 0f
                        }
                    )
                    .padding(top = 12.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
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
