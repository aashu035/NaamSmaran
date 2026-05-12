package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranMotion

/**
 * TapScale — A Modifier extension that provides subtle press-scale feedback.
 *
 * Replaces ripple indication with a 0.96× scale spring animation (Rule 6).
 * Zero-UI compliant: no color change, no ripple, just motion feedback.
 *
 * Usage:
 * ```
 * Box(modifier = Modifier.tapScale { doSomething() }) { ... }
 * ```
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Composable
fun Modifier.tapScale(
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) NaamSmaranMotion.TapScale else 1f,
        animationSpec = NaamSmaranMotion.DefaultSpring,
        label = "tapScale"
    )

    return this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null  // No ripple — scale feedback only (Rule 6)
        ) {
            onClick()
        }
}
