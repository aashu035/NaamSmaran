package com.radhavallabh.naamsmaran.ui.screens.naamjap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.QuickAddButton
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.components.StatRow
import com.radhavallabh.naamsmaran.ui.screens.home.HomeViewModel
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.StatePartial
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.text.NumberFormat
import java.util.Locale

/**
 * NaamJapScreen — Section 1: नाम जप
 *
 * Shows both tracks:
 *   Track B — राधा Naam Jap (counter, today's count/target, quick-add, manual input)
 *   Track A — हरिवंश Naam Jap (mala count, manual entry)
 *
 * Uses HomeViewModel — same ViewModel as HomeScreen, for real-time data.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun NaamJapScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val record by viewModel.todayRecord.collectAsState()
    val format = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))

    val did = record?.did ?: 0L
    val target = record?.target ?: 21_600L
    val remaining = (target - did).coerceAtLeast(0)
    val progress = if (target > 0) did.toFloat() / target.toFloat() else 0f
    val progressPercent = (progress * 100).toInt().coerceIn(0, 100)
    val isComplete = did >= target

    var manualInput by remember { mutableStateOf("") }

    SectionScaffold(
        title = "नाम जप",
        emoji = "📿",
        onBack = onBack
    ) {
        // ── Track B: राधा Naam Jap ──────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🌸 राधा नाम जप — Track B",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Big counter display
            Text(
                text = format.format(did),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (isComplete) StateExceeded else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "का $progressPercent% पूर्ण",
                style = NaamSmaranTypography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BarHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(com.radhavallabh.naamsmaran.ui.theme.ProgressTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(Dimens.BarHeight)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isComplete) StateExceeded else SharadMoonColors.accentPrimary
                        )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space4))

            StatRow(label = "किया",  value = format.format(did))
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "लक्ष्य", value = format.format(target))
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(
                label = "शेष",
                value = if (isComplete) "✅ पूर्ण!" else format.format(remaining),
                valueColor = if (isComplete) StateExceeded else StatePartial
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Quick-add buttons ───────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "जल्दी जोड़ें",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                QuickAddButton(label = "+१०८",   modifier = Modifier.weight(1f)) { viewModel.addJap(108) }
                QuickAddButton(label = "+१,०००", modifier = Modifier.weight(1f)) { viewModel.addJap(1_000) }
                QuickAddButton(label = "+५,०००", modifier = Modifier.weight(1f)) { viewModel.addJap(5_000) }
            }

            Spacer(modifier = Modifier.height(Dimens.Space4))

            // Manual number input
            Text(
                text = "कस्टम संख्या",
                style = NaamSmaranTypography.labelLarge,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = manualInput,
                    onValueChange = { if (it.length <= 7) manualInput = it },
                    placeholder = {
                        Text(
                            text = "जप संख्या लिखें",
                            color = TextTertiary,
                            style = NaamSmaranTypography.bodyMedium
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val count = manualInput.toLongOrNull() ?: 0L
                            if (count > 0) {
                                viewModel.addJap(count)
                                manualInput = ""
                            }
                        }
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = SharadMoonColors.accentPrimary,
                        unfocusedBorderColor = BorderGlass,
                        cursorColor = SharadMoonColors.accentPrimary,
                        focusedContainerColor = SurfaceGlassInput,
                        unfocusedContainerColor = SurfaceGlassInput
                    )
                )
                QuickAddButton(
                    label = "जोड़ें",
                    modifier = Modifier
                ) {
                    val count = manualInput.toLongOrNull() ?: 0L
                    if (count > 0) {
                        viewModel.addJap(count)
                        manualInput = ""
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Track A: हरिवंश Naam Jap (माला) ────────────────────────────────
        GlassCardColumn {
            Text(
                text = "📿 हरिवंश नाम जप — Track A",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            StatRow(label = "आज का लक्ष्य",   value = "११ माला")
            Spacer(modifier = Modifier.height(Dimens.Space2))
            StatRow(label = "१ माला =",        value = "१०८ जप")
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
                    text = "🪷 3D माला काउंटर\nजल्द आएगा",
                    style = NaamSmaranTypography.bodyMedium,
                    color = TextTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}
