package com.radhavallabh.naamsmaran.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * DonutChart — Custom Canvas-drawn 3-segment donut chart.
 * Perfect for showing Completed / Partial / Missed breakdowns.
 * Drawn with high efficiency, features rounded cap segments and center content slot.
 */
@Composable
fun DonutChart(
    values: List<Float>, // e.g. [22f, 5f, 4f]
    colors: List<Color>, // e.g. [Rose, Gold, Blue]
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    strokeWidth: Dp = 16.dp,
    centerContent: @Composable (() -> Unit)? = null
) {
    val strokeWidthPx = with(LocalDensity.current) { strokeWidth.toPx() }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val total = values.sum()
            if (total == 0f) {
                // Draw a gray placeholder track if there is no data yet
                drawArc(
                    color = Color(0x14FFFFFF),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidthPx)
                )
                return@Canvas
            }

            var startAngle = -90f
            values.forEachIndexed { index, valItem ->
                val sweepAngle = (valItem / total) * 360f
                if (sweepAngle > 0f) {
                    drawArc(
                        color = colors.getOrElse(index) { Color.LightGray },
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle
                }
            }
        }

        // Draw centered information text inside donut chart
        if (centerContent != null) {
            centerContent()
        }
    }
}
