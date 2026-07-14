package com.radhavallabh.naamsmaran.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * AreaChart — Reusable smooth bezier area chart drawn on Canvas.
 * No external library dependencies. Lean, customizable, high-performance.
 * Interpolates points using cubic Bezier curves.
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun AreaChart(
    points: List<Long>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFFE8A0BF), // Rose pink default
    fillGradient: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8A0BF).copy(alpha = 0.35f), Color.Transparent)
    ),
    labels: List<String> = emptyList()
) {
    Box(modifier = modifier.padding(bottom = 16.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (points.size < 2) return@Canvas

            val maxVal = points.maxOrNull()?.coerceAtLeast(1L) ?: 1L
            val width = size.width
            val height = size.height - 24f // reserve space at bottom for x-axis labels
            val segmentWidth = width / (points.size - 1).coerceAtLeast(1)

            val path = Path()
            val fillPath = Path()

            val firstX = 0f
            val firstY = height - (points[0].toFloat() / maxVal * (height - 30f)).coerceAtLeast(10f)

            path.moveTo(firstX, firstY)
            fillPath.moveTo(firstX, height)
            fillPath.lineTo(firstX, firstY)

            for (i in 1 until points.size) {
                val prevX = (i - 1) * segmentWidth
                val prevY = height - (points[i - 1].toFloat() / maxVal * (height - 30f)).coerceAtLeast(10f)
                val currX = i * segmentWidth
                val currY = height - (points[i].toFloat() / maxVal * (height - 30f)).coerceAtLeast(10f)

                // Control points for smooth cubic bezier
                val cp1X = prevX + segmentWidth / 2f
                val cp1Y = prevY
                val cp2X = prevX + segmentWidth / 2f
                val cp2Y = currY

                path.cubicTo(cp1X, cp1Y, cp2X, cp2Y, currX, currY)
                fillPath.cubicTo(cp1X, cp1Y, cp2X, cp2Y, currX, currY)
            }

            fillPath.lineTo(width, height)
            fillPath.close()

            // Draw filled gradient area
            drawPath(path = fillPath, brush = fillGradient)

            // Draw smooth top line
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw horizontal dotted grid base line at bottom of chart
            drawLine(
                color = Color(0x1FFFFFFF),
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
