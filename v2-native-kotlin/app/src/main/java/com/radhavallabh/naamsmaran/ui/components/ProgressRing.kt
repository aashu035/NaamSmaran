package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.ProgressTrack

/**
 * ProgressRing — Animated circular progress indicator.
 *
 * Shows sadhana progress as a ring with the accent color.
 * Animates smoothly when the value changes using spring physics.
 *
 * Track: 8% white (ProgressTrack)
 * Fill: theme accent primary → accent secondary gradient
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun ProgressRing(
    progress: Float,  // 0f..1f (can exceed 1.0 for target exceeded)
    modifier: Modifier = Modifier,
    size: Dp = Dimens.RingSizeLarge,
    strokeWidth: Dp = Dimens.RingStrokeLarge,
    centerContent: @Composable () -> Unit = {}
) {
    val colors = LocalNaamSmaranColors.current
    val clampedProgress = progress.coerceIn(0f, 1f)

    // Animated progress with spring for fluid motion
    var targetProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(clampedProgress) {
        targetProgress = clampedProgress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Dimens.SpringDampingDefault,
            stiffness = Dimens.SpringStiffnessLow
        ),
        label = "ring_progress"
    )

    // Determine color based on completion
    val fillColor = when {
        progress >= 1f -> com.radhavallabh.naamsmaran.ui.theme.StateExceeded
        progress >= 0.5f -> colors.accentPrimary
        else -> colors.accentSecondary
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size
            val stroke = strokeWidth.toPx()
            val arcSize = Size(canvasSize.width - stroke, canvasSize.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)

            // Track ring (full circle, dim)
            drawArc(
                color = ProgressTrack,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // Progress fill
            drawArc(
                color = fillColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        // Center content (counter text, etc.)
        centerContent()
    }
}
