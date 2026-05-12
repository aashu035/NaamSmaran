package com.radhavallabh.naamsmaran.ui.screens.vrindavanlila

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * VrindavanLilaScreen — Section 7: श्री वृंदावन शत लीला.
 *
 * 100 छंद reading section with carry-over pattern.
 * NOT doubling formula — uses reading carry-over:
 *   unread + new increment daily, no penalty doubling.
 *
 * Scaffold screen — will be data-driven in next phase.
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
@Composable
fun VrindavanLilaScreen(
    onBack: () -> Unit = {}
) {
    // Placeholder reading state — will be Room-driven
    var currentChhanda by remember { mutableIntStateOf(1) }
    val totalChhandas = 100
    val todayTarget = 2
    val readToday = 0

    SectionScaffold(
        title = "वृंदावन शत लीला",
        emoji = "🛕",
        onBack = onBack
    ) {
        // ── Today's Reading Progress ──────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🛕 श्री वृंदावन शत लीला",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Dimens.Space2))

            Text(
                text = "१०० छंद — लीला वर्णन",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(Dimens.Space5))

            // Current position
            Text(
                text = "छंद $currentChhanda / $totalChhandas",
                style = NaamSmaranTypography.displaySmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            Text(
                text = "आज का लक्ष्य: $todayTarget छंद",
                style = NaamSmaranTypography.bodyMedium,
                color = SharadMoonColors.accentSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space2))

            Text(
                text = "आज पढ़े: $readToday / $todayTarget",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
