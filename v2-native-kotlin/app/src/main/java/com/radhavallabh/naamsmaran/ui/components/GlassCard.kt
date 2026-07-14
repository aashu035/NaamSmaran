package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass

/**
 * GlassCard — Reusable Glassmorphism Card standard from Design System.
 * Features:
 * - 4% White Glass surface
 * - 8% Frosted border outline
 * - 24dp Rounded corners (never <12dp per guidelines)
 * - Tactile spring-based scale compression on click (scale to 0.96f)
 * - Optional soft radial glow backdrop
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(24.dp),
    glowColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth press scaling feed (spring animation: damping 0.75f, stiffness 300f per guidelines)
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.96f else 1.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 300f),
        label = "PressScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .then(
                if (glowColor != null) {
                    Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(glowColor.copy(alpha = 0.12f), Color.Transparent),
                            radius = 200f
                        )
                    )
                } else Modifier
            )
            .clip(shape)
            .background(SurfaceGlass)
            .border(1.dp, BorderGlass, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null, // No generic ripples to preserve premium feel
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(Dimens.PaddingCard),
        contentAlignment = Alignment.TopStart,
        content = content
    )
}
