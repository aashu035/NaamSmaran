package com.radhavallabh.naamsmaran.ui.screens.ashtayamseva

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary

/**
 * AshtayamSevaScreen — Section 5: अष्टयाम सेवा पद्धति.
 *
 * A checklist of daily devotional service times.
 * Minimum 3 seva times per day; full schedule has 8 periods:
 *   मंगला, श्रृंगार, ग्वाल, राज भोग, उत्थापन, भोग, संध्या, शयन
 *
 * No target escalation — this is a flexible checklist, not a counter.
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Composable
fun AshtayamSevaScreen(
    onBack: () -> Unit = {}
) {
    SectionScaffold(
        title = "अष्टयाम सेवा",
        emoji = "💙💛",
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "अष्टयाम सेवा",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Seva period checklist with timings
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

            sevaPeriods.forEach { (period, time) ->
                Text(
                    text = "☐  $period",
                    style = NaamSmaranTypography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = "      $time",
                    style = NaamSmaranTypography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "न्यूनतम ३ सेवा समय प्रतिदिन",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
