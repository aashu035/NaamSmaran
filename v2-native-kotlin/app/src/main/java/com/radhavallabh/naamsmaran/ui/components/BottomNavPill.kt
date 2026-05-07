package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NavIconInactive
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary

/**
 * BottomNavPill — Floating pill navigation.
 *
 * Engineering requirements from the directive:
 * 1. Float just above the bottom safe area
 * 2. Active indicator (glowing dot) animates with SpringSpec
 * 3. Damping ratio ~0.6 for physical slider bounce feel
 *
 * Phase 1: 2 tabs only — Home and Sections (more added later)
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

data class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@Composable
fun BottomNavPill(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNaamSmaranColors.current

    // Calculate the indicator offset based on selected index
    // Each icon slot is evenly spaced within the pill
    val density = LocalDensity.current
    val slotCount = items.size

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 48.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // The pill container
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(SurfaceGlass)
                .border(
                    width = 1.dp,
                    color = BorderGlass,
                    shape = RoundedCornerShape(999.dp)
                )
                .height(Dimens.BottomNavHeight)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BottomNavHeight),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = index == selectedIndex

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onItemSelected(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Icon + glow dot column
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) TextPrimary else NavIconInactive
                            )

                            // Animated glowing dot below the icon
                            if (isSelected) {
                                // Dot size animates in with spring bounce
                                val dotSize by animateDpAsState(
                                    targetValue = 5.dp,
                                    animationSpec = spring(
                                        dampingRatio = 0.6f,  // Per directive: slight bounce
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "dot_size"
                                )

                                Box(
                                    modifier = Modifier
                                        .offset(y = 18.dp)
                                        .size(dotSize)
                                        .clip(CircleShape)
                                        .background(colors.accentPrimary)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
