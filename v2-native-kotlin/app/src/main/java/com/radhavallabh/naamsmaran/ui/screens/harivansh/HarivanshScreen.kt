package com.radhavallabh.naamsmaran.ui.screens.harivansh

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.components.StatRow
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * HarivanshScreen — Section 5: अष्टयाम सेवा पद्धति
 *
 * Checklist of daily seva timings:
 * Minimum 3 required (Mangala, Bhog, Shayan). Full 8 seva periods.
 * No carry-over escalation — flexible completion.
 *
 * Track A (हरिवंश नाम जप माला count) is shown in NaamJapScreen, not here.
 * This screen maps to bottom-sheet index 1 = "हरिवंश चालीसा" (Seva checklist).
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun HarivanshScreen(onBack: () -> Unit) {

    val sevaTimings = listOf(
        "मंगला" to "प्रातः",
        "श्रृंगार" to "सुबह",
        "ग्वाल" to "दोपहर पूर्व",
        "राज भोग" to "दोपहर",
        "उत्थापन" to "दोपहर बाद",
        "भोग" to "सायं पूर्व",
        "संध्या" to "सायं",
        "शयन" to "रात्रि"
    )

    SectionScaffold(
        title = "अष्टयाम सेवा",
        emoji = "🌸",
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "अष्टयाम सेवा पद्धति",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "दिन में न्यूनतम ३ सेवा काल",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            sevaTimings.forEach { (seva, time) ->
                StatRow(label = seva, value = time)
                Spacer(modifier = Modifier.height(Dimens.Space2))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "📋 इंटरैक्टिव चेकलिस्ट",
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
                    text = "🌙 इंटरैक्टिव चेकलिस्ट\nअगले संस्करण में आएगी",
                    style = NaamSmaranTypography.bodyMedium,
                    color = TextTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}
