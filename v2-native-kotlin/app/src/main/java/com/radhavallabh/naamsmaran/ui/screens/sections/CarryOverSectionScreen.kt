package com.radhavallabh.naamsmaran.ui.screens.sections

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.components.StatRow
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.ProgressTrack
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.StatePartial
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarryOverSectionScreen(
    spec: CarryOverSection,
    onBack: () -> Unit,
    viewModel: SectionsViewModel = hiltViewModel()
) {
    val state by viewModel.carryOverSectionState(spec).collectAsState()
    val format = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")) }
    val colors = LocalNaamSmaranColors.current

    SectionScaffold(
        title = spec.devotionalSectionId.hindiName,
        emoji = null,
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = spec.devotionalSectionId.hindiName,
                style = NaamSmaranTypography.titleMedium,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "${spec.subtitle} | आज का लक्ष्य: ${format.format(state.todayTarget)} ${spec.unitLabel}",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            Text(
                text = format.format(state.todayDone),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (state.isComplete) StateExceeded else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "आज पढ़े गए ${spec.unitLabel}",
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
                    .background(ProgressTrack, RoundedCornerShape(Dimens.Space1))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(state.todayProgress)
                        .height(Dimens.BarHeight)
                        .background(
                            if (state.isComplete) StateExceeded else colors.accentPrimary,
                            RoundedCornerShape(Dimens.Space1)
                        )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space4))

            StatRow(label = "पढ़े", value = "${format.format(state.todayDone)} ${spec.unitLabel}")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "लक्ष्य", value = "${format.format(state.todayTarget)} ${spec.unitLabel}")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(
                label = "शेष",
                value = if (state.isComplete) "✅ पूर्ण!" else "${format.format(state.remaining)} ${spec.unitLabel}",
                valueColor = if (state.isComplete) StateExceeded else StatePartial
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "${spec.unitLabel} जोड़ें",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                spec.quickAdds.forEach { amount ->
                    CarryOverActionButton(
                        label = "+${amount.toDevanagari()}",
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.addCarryOverProgress(spec, amount)
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "रीसेट",
                style = NaamSmaranTypography.labelMedium,
                color = TextTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.resetCarryOverProgress(spec) }
                    .padding(Dimens.Space2),
                textAlign = TextAlign.Center
            )
        }

        if (spec.totalUnits > 0) {
            Spacer(modifier = Modifier.height(Dimens.GapStack))

            GlassCardColumn {
                Text(
                    text = "सम्पूर्ण प्रगति",
                    style = NaamSmaranTypography.titleSmall,
                    color = colors.accentSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Dimens.Space3))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${format.format(state.overallRead)} / ${format.format(spec.totalUnits)}",
                            style = NaamSmaranTypography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${spec.unitLabel} पढ़े गए",
                            style = NaamSmaranTypography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "${(state.overallProgress * 100).toInt()}%",
                        style = NaamSmaranTypography.headlineMedium,
                        color = colors.accentPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Space3))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Space2)
                        .background(ProgressTrack, RoundedCornerShape(Dimens.Space1))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(state.overallProgress)
                            .height(Dimens.Space2)
                            .background(colors.accentSecondary, RoundedCornerShape(Dimens.Space1))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}

@Composable
private fun CarryOverActionButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalNaamSmaranColors.current

    Box(
        modifier = modifier
            .background(
                color = colors.accentPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(Dimens.Space4)
            )
            .border(
                width = Dimens.Space1 / 4,
                color = colors.accentPrimary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(Dimens.Space4)
            )
            .clickable(onClick = onClick)
            .padding(vertical = Dimens.Space4),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.labelLarge,
            color = colors.accentPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun Long.toDevanagari(): String {
    val map = mapOf(
        '0' to '०',
        '1' to '१',
        '2' to '२',
        '3' to '३',
        '4' to '४',
        '5' to '५',
        '6' to '६',
        '7' to '७',
        '8' to '८',
        '9' to '९'
    )
    return toString().map { map[it] ?: it }.joinToString("")
}
