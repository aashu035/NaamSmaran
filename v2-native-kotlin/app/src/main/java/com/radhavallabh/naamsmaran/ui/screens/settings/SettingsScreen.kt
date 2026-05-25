package com.radhavallabh.naamsmaran.ui.screens.settings

import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.BuildConfig
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colors = LocalNaamSmaranColors.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            viewModel.exportBackup(uri)
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.restoreBackup(uri)
        }
    }

    val timeText = remember(uiState.dailyReminderHour, uiState.dailyReminderMinute) {
        String.format(Locale.ENGLISH, "%02d:%02d", uiState.dailyReminderHour, uiState.dailyReminderMinute)
    }
    val lastBackupText = uiState.lastBackupAt?.let { "अंतिम बैकअप: $it" } ?: "अभी तक बैकअप नहीं"

    SectionScaffold(
        title = "सेटिंग्स",
        emoji = "⚙️",
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "थीम",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            NaamSmaranThemeId.entries.forEach { theme ->
                val isSelected = uiState.activeTheme == theme
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isSelected) colors.accentPrimary.copy(alpha = 0.15f) else SurfaceGlassInput,
                            RoundedCornerShape(Dimens.Space3)
                        )
                        .border(
                            width = Dimens.Space1 / 4,
                            color = if (isSelected) colors.accentPrimary.copy(alpha = 0.6f) else BorderGlass,
                            shape = RoundedCornerShape(Dimens.Space3)
                        )
                        .clickable { viewModel.setTheme(theme) }
                        .padding(Dimens.Space3),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = theme.displayNameHindi,
                            style = NaamSmaranTypography.bodyMedium,
                            color = if (isSelected) colors.accentPrimary else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                        Text(
                            text = theme.displayName,
                            style = NaamSmaranTypography.bodySmall,
                            color = TextTertiary
                        )
                    }
                    if (isSelected) {
                        Text(
                            text = "✓",
                            color = colors.accentPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.Space2))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "सूचनाएं",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "दैनिक स्मरण",
                subLabel = "प्रतिदिन नाम जप याद दिलाएं",
                checked = uiState.dailyReminderEnabled,
                onCheckedChange = viewModel::setDailyReminderEnabled
            )

            if (uiState.dailyReminderEnabled) {
                Spacer(modifier = Modifier.height(Dimens.Space3))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceGlassInput, RoundedCornerShape(Dimens.Space3))
                        .border(Dimens.Space1 / 4, BorderGlass, RoundedCornerShape(Dimens.Space3))
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> viewModel.setDailyReminderTime(hour, minute) },
                                uiState.dailyReminderHour,
                                uiState.dailyReminderMinute,
                                false
                            ).show()
                        }
                        .padding(Dimens.Space3),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "समय",
                            style = NaamSmaranTypography.labelMedium,
                            color = TextTertiary
                        )
                        Text(
                            text = timeText,
                            style = NaamSmaranTypography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "बदलें",
                        style = NaamSmaranTypography.labelMedium,
                        color = colors.accentPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "प्राथमिकताएं",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "हैप्टिक फीडबैक",
                subLabel = "बटन दबाने पर कंपन",
                checked = uiState.hapticEnabled,
                onCheckedChange = viewModel::setHapticEnabled
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceGlassInput, RoundedCornerShape(Dimens.Space3))
                    .border(Dimens.Space1 / 4, BorderGlass, RoundedCornerShape(Dimens.Space3))
                    .padding(Dimens.Space3),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "प्रारंभिक नाम जप लक्ष्य",
                        style = NaamSmaranTypography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Track B — राधा नाम जप",
                        style = NaamSmaranTypography.bodySmall,
                        color = TextTertiary
                    )
                }
                Text(
                    text = formatIndian(uiState.initialTarget),
                    style = NaamSmaranTypography.bodyMedium,
                    color = colors.accentPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "डेटा और बैकअप",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "स्वत: बैकअप",
                subLabel = "प्रतिदिन स्थानीय JSON बैकअप सहेजें",
                checked = uiState.autoBackupEnabled,
                onCheckedChange = viewModel::setAutoBackupEnabled
            )

            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = lastBackupText,
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                ActionButton(
                    label = "डेटा निर्यात करें",
                    modifier = Modifier.weight(1f)
                ) {
                    val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    exportLauncher.launch("naam-smaran-backup-$today.json")
                }

                ActionButton(
                    label = "डेटा पुनर्स्थापित करें",
                    modifier = Modifier.weight(1f)
                ) {
                    restoreLauncher.launch(arrayOf("application/json"))
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "एप के बारे में",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Text(
                text = "नाम स्मरण",
                style = NaamSmaranTypography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "संस्करण ${BuildConfig.VERSION_NAME}",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Text(
                text = "जय जय श्री हित हरिवंश\nराधावल्लभ श्री हरिवंश",
                style = NaamSmaranTypography.bodyMedium,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    subLabel: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = LocalNaamSmaranColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = NaamSmaranTypography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = subLabel,
                style = NaamSmaranTypography.labelSmall,
                color = TextTertiary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.accentPrimary,
                checkedTrackColor = colors.accentPrimary.copy(alpha = 0.35f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = SurfaceGlassInput
            )
        )
    }
}

@Composable
private fun ActionButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalNaamSmaranColors.current

    Box(
        modifier = modifier
            .background(colors.accentPrimary.copy(alpha = 0.1f), RoundedCornerShape(Dimens.Space3))
            .border(Dimens.Space1 / 4, colors.accentPrimary.copy(alpha = 0.4f), RoundedCornerShape(Dimens.Space3))
            .clickable(onClick = onClick)
            .padding(Dimens.Space3),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = NaamSmaranTypography.bodyMedium,
            color = colors.accentPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatIndian(value: Long): String =
    java.text.NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN")).format(value)
