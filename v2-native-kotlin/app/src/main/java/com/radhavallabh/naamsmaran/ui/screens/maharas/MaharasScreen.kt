package com.radhavallabh.naamsmaran.ui.screens.maharas

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * MaharasScreen — Section 3: श्री हित राधा सुधानिधी जी
 *
 * Reading tracker: 10 श्लोक/day with meaning (अर्थ सहित).
 * If met: +5 श्लोक | Carry-over pattern (not doubling).
 * Includes a notes field for अनुभव/reflections.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun MaharasScreen(onBack: () -> Unit) {

    val todayTarget = 10
    var todayDone by remember { mutableIntStateOf(0) }
    var notes by remember { mutableStateOf("") }

    val remaining = (todayTarget - todayDone).coerceAtLeast(0)
    val isComplete = todayDone >= todayTarget
    val progress = (todayDone.toFloat() / todayTarget.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "progress"
    )

    SectionScaffold(
        title = "सुधानिधी स्तोत्र",
        emoji = "🪷",
        onBack = onBack
    ) {

        // ── Today's Progress ────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "श्री हित राधा सुधानिधी जी स्तोत्र",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "अर्थ सहित पाठ | आज: $todayTarget श्लोक",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Counter display
            Text(
                text = "$todayDone",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (isComplete) StateExceeded else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "आज पढ़े गए श्लोक",
                style = NaamSmaranTypography.bodyMedium,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space4))

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

            StatRow(label = "पढ़े", value = "$todayDone श्लोक")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "लक्ष्य", value = "$todayTarget श्लोक")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(
                label = "शेष",
                value = if (isComplete) "✅ पूर्ण!" else "$remaining श्लोक",
                valueColor = if (isComplete) StateExceeded else StatePartial
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Quick-mark buttons ───────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "श्लोक चिह्नित करें",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                ShlokButton(label = "+१", modifier = Modifier.weight(1f)) {
                    todayDone = (todayDone + 1).coerceAtMost(todayTarget * 2)
                }
                ShlokButton(label = "+२", modifier = Modifier.weight(1f)) {
                    todayDone = (todayDone + 2).coerceAtMost(todayTarget * 2)
                }
                ShlokButton(label = "+५", modifier = Modifier.weight(1f)) {
                    todayDone = (todayDone + 5).coerceAtMost(todayTarget * 2)
                }
            }
            Spacer(modifier = Modifier.height(Dimens.Space2))
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

        // ── अनुभव Notes field ────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "✍️ आज की अनुभूति",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceGlassInput)
                    .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
                    .padding(Dimens.Space3),
                contentAlignment = Alignment.TopStart
            ) {
                if (notes.isEmpty()) {
                    Text(
                        text = "अपनी अनुभूति यहाँ लिखें…",
                        style = NaamSmaranTypography.bodyMedium,
                        color = TextTertiary
                    )
                } else {
                    Text(
                        text = notes,
                        style = NaamSmaranTypography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "नोट्स सुविधा शीघ्र आएगी",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Carry-over rule
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
                        text = "• लक्ष्य पूरा हो तो अगले दिन: +५ श्लोक\n• अधूरा रहे तो: बचे श्लोक + नया लक्ष्य",
                        style = NaamSmaranTypography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}

@Composable
private fun ShlokButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SharadMoonColors.accentSecondary.copy(alpha = 0.18f))
            .border(1.dp, SharadMoonColors.accentSecondary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.labelLarge,
            color = SharadMoonColors.accentSecondary,
            fontWeight = FontWeight.SemiBold
        )
    }
}
