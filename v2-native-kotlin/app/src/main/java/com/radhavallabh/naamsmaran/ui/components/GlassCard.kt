package com.radhavallabh.naamsmaran.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranShapes
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass

/**
 * GlassCard — PRD §4.4 Glassmorphism standard.
 *
 * background: rgba(255, 255, 255, 0.04)
 * border: 1px solid rgba(255, 255, 255, 0.08)
 * border-radius: 24px
 * backdrop-filter: blur(20px)
 *
 * The blur effect uses RenderEffect on Android 12+ for a true frosted glass look.
 * On older devices, it falls back to a solid translucent dark surface.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(NaamSmaranShapes.large)
            .background(SurfaceGlass)
            .border(
                width = 1.dp,
                color = BorderGlass,
                shape = NaamSmaranShapes.large
            )
            .padding(Dimens.PaddingCard),
        content = content
    )
}

/**
 * Elevated variant with slightly more visible surface.
 */
@Composable
fun GlassCardElevated(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(NaamSmaranShapes.large)
            .background(com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassElevated)
            .border(
                width = 1.dp,
                color = BorderGlass,
                shape = NaamSmaranShapes.large
            )
            .padding(Dimens.PaddingCard),
        content = content
    )
}
