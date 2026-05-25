package com.radhavallabh.naamsmaran.ui.screens.ashtayamseva

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.screens.sections.SectionsViewModel
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

@Composable
fun AshtayamSevaScreen(
    onBack: () -> Unit = {},
    viewModel: SectionsViewModel = hiltViewModel()
) {
    val isDoneToday by viewModel.ashtayamDoneToday.collectAsState()
    val colors = LocalNaamSmaranColors.current

    val sevaPeriods = listOf(
        "मंगला" to "प्रातः ४:३०–५:३०",
        "श्रृंगार" to "प्रातः ५:३०–७:३०",
        "ग्वाल" to "प्रातः ७:३०–८:३०",
        "राज भोग" to "मध्याह्न १०:३०–१२:३०",
        "उत्थापन" to "दोपहर ३:३०–४:३०",
        "भोग" to "सायं ५:३०–६:३०",
        "संध्या" to "सायं ७:३०–८:३०",
        "शयन" to "रात्रि ९:३०–१०:३०"
    )

    SectionScaffold(
        title = "अष्टयाम सेवा पद्धति",
        emoji = null,
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "अष्टयाम सेवा",
                style = NaamSmaranTypography.titleMedium,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "आठ सेवा समय प्रतिदिन",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isDoneToday) StateExceeded.copy(alpha = 0.18f) else SurfaceGlassInput,
                        RoundedCornerShape(Dimens.Space4)
                    )
                    .border(
                        width = Dimens.Space1 / 4,
                        color = if (isDoneToday) StateExceeded else BorderGlass,
                        shape = RoundedCornerShape(Dimens.Space4)
                    )
                    .clickable { viewModel.setAshtayamDoneToday(!isDoneToday) }
                    .padding(vertical = Dimens.Space4),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isDoneToday) "आज की सेवा पूर्ण हुई" else "आज की सेवा पूर्ण चिह्नित करें",
                    style = NaamSmaranTypography.bodyMedium,
                    color = if (isDoneToday) StateExceeded else TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            sevaPeriods.forEachIndexed { index, (period, time) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = period,
                            style = NaamSmaranTypography.bodyLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = time,
                            style = NaamSmaranTypography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "•",
                        style = NaamSmaranTypography.titleLarge,
                        color = colors.accentSecondary
                    )
                }
                if (index != sevaPeriods.lastIndex) {
                    Spacer(modifier = Modifier.height(Dimens.Space3))
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Space3))
            Text(
                text = "न्यूनतम ३ सेवा समय प्रतिदिन",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary
            )
        }
    }
}
