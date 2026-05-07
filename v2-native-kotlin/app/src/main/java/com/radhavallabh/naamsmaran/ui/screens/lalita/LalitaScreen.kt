package com.radhavallabh.naamsmaran.ui.screens.lalita

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
 * LalitaScreen — Section 4: श्री हित सेवक वाणी
 *
 * Reading tracker: 5 छंद/day with meaning.
 * If met: +2 छंद | Carry-over pattern (not doubling).
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun LalitaScreen(onBack: () -> Unit) {

    val todayTarget = 5
    var todayDone by remember { mutableIntStateOf(0) }

    val remaining = (todayTarget - todayDone).coerceAtLeast(0)
    val isComplete = todayDone >= todayTarget
    val progress = (todayDone.toFloat() / todayTarget.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "progress"
    )

    SectionScaffold(
        title = "सेवक वाणी",
        emoji = "🌺",
        onBack = onBack
    ) {

        // ── Today's Progress ────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🌺 श्री हित सेवक वाणी",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "अर्थ सहित | आज का लक्ष्य: $todayTarget छंद",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            Text(
                text = "$todayDone",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (isComplete) StateExceeded else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "आज पढ़े गए छंद",
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

            StatRow(label = "पढ़े", value = "$todayDone छंद")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "लक्ष्य", value = "$todayTarget छंद")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(
                label = "शेष",
                value = if (isComplete) "✅ पूर्ण!" else "$remaining छंद",
                valueColor = if (isComplete) StateExceeded else StatePartial
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Quick-mark buttons ───────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "छंद चिह्नित करें",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                ChandButton(label = "+१", modifier = Modifier.weight(1f)) {
                    todayDone = (todayDone + 1).coerceAtMost(todayTarget * 3)
                }
                ChandButton(label = "+२", modifier = Modifier.weight(1f)) {
                    todayDone = (todayDone + 2).coerceAtMost(todayTarget * 3)
                }
                ChandButton(label = "पूर्ण", modifier = Modifier.weight(1f)) {
                    todayDone = todayTarget
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

        // ── Notes + Rule ────────────────────────────────────────────────────
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
                    .padding(Dimens.Space4),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "नोट्स सुविधा शीघ्र आएगी",
                    style = NaamSmaranTypography.bodyMedium,
                    color = TextTertiary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space3))

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
                        text = "• लक्ष्य पूरा हो तो अगले दिन: +२ छंद\n• अधूरा रहे तो: बचे छंद + नया लक्ष्य",
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
private fun ChandButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SharadMoonColors.accentPrimary.copy(alpha = 0.15f))
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
