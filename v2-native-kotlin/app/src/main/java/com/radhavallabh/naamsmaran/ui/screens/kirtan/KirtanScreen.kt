package com.radhavallabh.naamsmaran.ui.screens.kirtan

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.R
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.components.StatRow
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.ProgressTrack
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.StatePartial
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * KirtanScreen — Section 2: श्री हित चतुरसी जी
 *
 * Reading tracker for 84 पद (the complete Chaturasi text).
 * Daily baseline: 12 पद | If met: +6 पद | Carry-over pattern (not doubling).
 * Cycle restarts after all 84 पद are complete.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun KirtanScreen(onBack: () -> Unit) {

    // Today's reading state (local UI state; will be VM-driven in next phase)
    val todayTarget = 12
    var todayDone by remember { mutableIntStateOf(0) }
    val totalPad = 84
    val overallRead = 42 // placeholder from DB in future

    val remaining = (todayTarget - todayDone).coerceAtLeast(0)
    val isComplete = todayDone >= todayTarget
    val progress = (todayDone.toFloat() / todayTarget.toFloat()).coerceIn(0f, 1f)
    val overallProgress = (overallRead.toFloat() / totalPad.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "progress"
    )

    SectionScaffold(
        title = "चतुरसी",
        emoji = "📖",
        onBack = onBack
    ) {

        // ── Today's Progress ────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "📖 श्री हित चतुरसी जी",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "कुल ८४ पद | आज का लक्ष्य: $todayTarget पद",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Big count display
            Text(
                text = "$todayDone",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (isComplete) StateExceeded else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "आज पढ़े गए पद",
                style = NaamSmaranTypography.bodyMedium,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Today's progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BarHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(ProgressTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(Dimens.BarHeight)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isComplete) StateExceeded else SharadMoonColors.accentPrimary)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space4))

            StatRow(label = "पढ़े", value = "$todayDone पद")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "लक्ष्य", value = "$todayTarget पद")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(
                label = "शेष",
                value = if (isComplete) "✅ पूर्ण!" else "$remaining पद",
                valueColor = if (isComplete) StateExceeded else StatePartial
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Quick-mark buttons (+1, +3, +6) ────────────────────────────────
        GlassCardColumn {
            Text(
                text = "पद चिह्नित करें",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                PadMarkButton(
                    label = "+१",
                    modifier = Modifier.weight(1f)
                ) { todayDone = (todayDone + 1).coerceAtMost(todayTarget * 2) }

                PadMarkButton(
                    label = "+३",
                    modifier = Modifier.weight(1f)
                ) { todayDone = (todayDone + 3).coerceAtMost(todayTarget * 2) }

                PadMarkButton(
                    label = "+६",
                    modifier = Modifier.weight(1f)
                ) { todayDone = (todayDone + 6).coerceAtMost(todayTarget * 2) }
            }
            Spacer(modifier = Modifier.height(Dimens.Space3))
            // Reset button
            Text(
                text = "रीसेट",
                style = NaamSmaranTypography.labelMedium,
                color = TextTertiary,
                modifier = Modifier
                    .clickable { todayDone = 0 }
                    .padding(Dimens.Space2)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Overall Progress across full 84 पद ──────────────────────────────
        GlassCardColumn {
            Text(
                text = "सम्पूर्ण प्रगति",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Circular progress visual (text-based for now)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$overallRead / $totalPad",
                        style = NaamSmaranTypography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "पद पढ़े गए",
                        style = NaamSmaranTypography.bodySmall,
                        color = TextSecondary
                    )
                }
                Text(
                    text = "${(overallProgress * 100).toInt()}%",
                    style = NaamSmaranTypography.headlineMedium,
                    color = SharadMoonColors.accentPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space3))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(ProgressTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(overallProgress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SharadMoonColors.accentSecondary)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Carry-over rule display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceGlassInput)
                    .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
                    .padding(Dimens.Space3)
            ) {
                Column {
                    Text(
                        text = "📋 नियम",
                        style = NaamSmaranTypography.labelLarge,
                        color = SharadMoonColors.accentPrimary
                    )
                    Spacer(modifier = Modifier.height(Dimens.Space2))
                    Text(
                        text = "• लक्ष्य पूरा हो तो अगले दिन: +६ पद\n• अधूरा रहे तो: बचे पद + नया लक्ष्य",
                        style = NaamSmaranTypography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}

/**
 * Reusable pill button for marking पद (reading units).
 */
@Composable
private fun PadMarkButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SharadMoonColors.accentPrimary.copy(alpha = 0.18f))
            .border(1.dp, SharadMoonColors.accentPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.labelLarge,
            color = SharadMoonColors.accentPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}
