package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlassFocus
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.OverlayWhiteHigh
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassActive
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassHover
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary

@Composable
fun SectionNavButton(
    label: String,
    modifier: Modifier = Modifier,
    markerText: String? = null,
    icon: ImageVector? = null,
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
                shape = RoundedCornerShape(Dimens.Space4)
            )
            .border(
                width = Dimens.Space1 / 4,
                color = BorderGlassFocus,
                shape = RoundedCornerShape(Dimens.Space4)
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
            .padding(vertical = Dimens.Space3, horizontal = Dimens.Space2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.Space8)
                .background(OverlayWhiteHigh.copy(alpha = 0.14f), CircleShape)
                .border(Dimens.Space1 / 4, OverlayWhiteHigh.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when {
                icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = TextPrimary
                )
                !markerText.isNullOrBlank() -> Text(
                    text = markerText,
                    style = NaamSmaranTypography.labelLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space2))

        Text(
            text = label,
            style = NaamSmaranTypography.labelSmall.copy(
                fontWeight = FontWeight.W500,
                fontSize = 10.sp,
                lineHeight = 13.sp
            ),
            color = TextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
