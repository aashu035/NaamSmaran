package com.radhavallabh.naamsmaran.ui.screens.nityapath

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
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary

/**
 * NityaPathScreen — Section 6: नित्य पाठ रसोपासना.
 *
 * Daily prayers/recitations with completion toggle + monthly calendar view.
 * No carry-over: either done today or not.
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Composable
fun NityaPathScreen(
    onBack: () -> Unit = {}
) {
    SectionScaffold(
        title = "नित्य पाठ",
        emoji = "🪷",
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "🪷 आज का नित्य पाठ",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "☐ रसोपासना पाठ पूर्ण करें",
                style = NaamSmaranTypography.bodyLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "कैलेंडर दृश्य शीघ्र उपलब्ध",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
