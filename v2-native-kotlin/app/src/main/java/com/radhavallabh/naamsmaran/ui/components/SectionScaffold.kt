package com.radhavallabh.naamsmaran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassElevated
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary

/**
 * SectionScaffold — Shared layout shell for all 7 devotional section screens.
 *
 * Design:
 * - Full-screen deep indigo gradient background
 * - Top bar: back arrow + Hindi section title
 * - Scrollable content area (glass cards provided by each screen)
 * - Bottom safe area respected
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun SectionScaffold(
    title: String,
    emoji: String? = null,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalNaamSmaranColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.bgPrimary,
                        colors.bgPrimary.copy(alpha = 0.95f),
                        colors.bgPrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ── Top bar ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingCard, vertical = Dimens.Space4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button — circular glass pill
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Space4))

                if (!emoji.isNullOrBlank()) {
                    Text(text = emoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(Dimens.Space2))
                }
                Text(
                    text = title,
                    style = NaamSmaranTypography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Subtle separator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderGlass)
            )

            // ── Scrollable section content ─────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens.PaddingCard, vertical = Dimens.Space6),
                content = content
            )
        }
    }
}

/**
 * GlassCardColumn — Reusable frosted glass info card for section screens.
 * Column-based variant; use when multiple vertically stacked children are needed.
 */
@Composable
fun GlassCardColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceGlassElevated)
            .border(1.dp, BorderGlass, RoundedCornerShape(16.dp))
            .padding(Dimens.PaddingCard),
        content = content
    )
}

/**
 * StatRow — Shows a label/value pair inside a glass card.
 */
@Composable
fun StatRow(label: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = NaamSmaranTypography.bodyLarge,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
