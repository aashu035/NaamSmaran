package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteSubtle
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassFocus
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassActive
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassHover

/**
 * SectionNavButton — Premium glass navigation tile for the bottom sheet.
 *
 * Compact 2-line tile: emoji icon on top, section name below.
 * Spring-scale tap animation for premium feel.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun SectionNavButton(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) Dimens.ScaleTap else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "section_nav_scale"
    )

    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                color = if (pressed) SurfaceGlassHover else SurfaceGlassActive,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 0.5.dp,
                color = BorderGlassFocus,
                shape = RoundedCornerShape(16.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    },
                    onTap = { onClick() }
                )
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = NaamSmaranTypography.labelSmall.copy(
                fontWeight = FontWeight.W400,
                fontSize = 10.sp,
                lineHeight = 13.sp
            ),
            color = OverlayWhiteSubtle,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
