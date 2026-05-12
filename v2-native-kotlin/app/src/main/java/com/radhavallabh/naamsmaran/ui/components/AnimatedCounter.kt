package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranMotion
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import java.text.NumberFormat
import java.util.Locale

/**
 * AnimatedCounter — Rolls up from 0 to [target] with a spring animation.
 *
 * Used in Day-End Analysis and section summary screens.
 * Formats output with Indian numeral locale (1,00,000).
 *
 * Uses [Animatable<Float>] to avoid Int overflow for large counts.
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Composable
fun AnimatedCounter(
    target: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    color: Color = TextPrimary
) {
    val format = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")) }
    val current = remember { Animatable(0f) }

    LaunchedEffect(target) {
        current.animateTo(
            targetValue = target.toFloat(),
            animationSpec = NaamSmaranMotion.CounterSpring
        )
    }

    Text(
        text = format.format(current.value.toLong()),
        style = style,
        color = color,
        modifier = modifier
    )
}
