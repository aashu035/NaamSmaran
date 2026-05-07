package com.radhavallabh.naamsmaran.ui.components

import android.os.Build
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import kotlin.math.roundToInt

/**
 * GlassBottomSheet — Premium frosted glass bottom sheet.
 *
 * Engineering requirements from the directive:
 * 1. RenderEffect.createBlurEffect on Android 12+ for true background blur
 * 2. Spring animations for drag snap points — heavy, physical feel
 * 3. Draggable with velocity-based fling detection
 *
 * Snap points: COLLAPSED (handle only), HALF (50% screen), EXPANDED (85% screen)
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun GlassBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onDismiss: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Snap points as fractions of screen height
    val collapsedOffset = screenHeightPx * 0.92f  // Nearly hidden, just handle visible
    val halfOffset = screenHeightPx * 0.50f       // Half screen
    val expandedOffset = screenHeightPx * 0.15f   // Near full screen

    var currentTarget by remember {
        mutableStateOf(if (isExpanded) halfOffset else collapsedOffset)
    }

    // ── React to external isExpanded changes ────────────────────────────────
    // Without this, the sheet stays hidden even when HomeScreen sets sheetOpen=true,
    // because remember{} only evaluates once at first composition.
    LaunchedEffect(isExpanded) {
        currentTarget = if (isExpanded) halfOffset else collapsedOffset
    }

    // Spring animation — heavy, physical feel per directive
    val animatedOffset by animateDpAsState(
        targetValue = with(density) { currentTarget.toDp() },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,  // 0.5 — springy
            stiffness = Spring.StiffnessMediumLow             // 200 — heavy feel
        ),
        label = "sheet_offset"
    )

    var dragAccumulator by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(0, animatedOffset.roundToPx()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    dragAccumulator += delta
                    // Real-time drag follow
                    val newTarget = (currentTarget + delta).coerceIn(expandedOffset, collapsedOffset)
                    currentTarget = newTarget
                },
                onDragStarted = {
                    dragAccumulator = 0f
                },
                onDragStopped = { velocity ->
                    // Snap to nearest point based on position and velocity
                    currentTarget = when {
                        velocity > 1000f -> collapsedOffset  // Fast fling down → collapse
                        velocity < -1000f -> expandedOffset  // Fast fling up → expand
                        currentTarget < (expandedOffset + halfOffset) / 2 -> expandedOffset
                        currentTarget < (halfOffset + collapsedOffset) / 2 -> halfOffset
                        else -> {
                            onDismiss()
                            collapsedOffset
                        }
                    }
                    dragAccumulator = 0f
                }
            )
            .then(
                // Android 12+ real blur effect
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer {
                        renderEffect = android.graphics.RenderEffect
                            .createBlurEffect(60f, 60f, android.graphics.Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(SurfaceGlass)
            .border(
                width = 1.dp,
                color = BorderGlass,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingCard)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag handle
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TextTertiary)
            )
            Spacer(modifier = Modifier.height(20.dp))

            content()

            Spacer(modifier = Modifier.height(Dimens.PaddingCard))
        }
    }
}
