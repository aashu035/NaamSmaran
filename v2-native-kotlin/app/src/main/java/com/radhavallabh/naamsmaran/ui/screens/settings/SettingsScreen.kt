package com.radhavallabh.naamsmaran.ui.screens.settings

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary

/**
 * SettingsScreen — App Configuration
 *
 * Contains:
 * - Theme switcher (future: Sharad Moon, Vrindavan Spring, Yamuna Night)
 * - Daily reminder toggle + time picker
 * - Initial Naam Jap target (configurable)
 * - Backup / export options
 * - App info
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun SettingsScreen(onBack: () -> Unit) {

    var dailyReminderEnabled by remember { mutableStateOf(true) }
    var hapticEnabled by remember { mutableStateOf(true) }
    var autoBackupEnabled by remember { mutableStateOf(false) }

    SectionScaffold(
        title = "सेटिंग्स",
        emoji = "⚙️",
        onBack = onBack
    ) {

        // ── Theme Settings ──────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🎨 थीम",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            listOf(
                Triple("🌸", "शरद मून", true),
                Triple("🌿", "वृंदावन स्प्रिंग", false),
                Triple("🌊", "यमुना नाइट", false)
            ).forEach { (emoji, name, isSelected) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) SharadMoonColors.accentPrimary.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .border(
                            1.dp,
                            if (isSelected) SharadMoonColors.accentPrimary.copy(alpha = 0.6f) else BorderGlass,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { /* theme switch logic */ }
                        .padding(Dimens.Space3),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Space2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                        Text(
                            text = name,
                            style = NaamSmaranTypography.bodyMedium,
                            color = if (isSelected) SharadMoonColors.accentPrimary else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                    if (isSelected) {
                        Text(text = "✓", color = SharadMoonColors.accentPrimary, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.Space2))
            }

            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "अन्य थीम शीघ्र आएंगी",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Notifications ───────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🔔 सूचनाएं",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "दैनिक स्मरण",
                subLabel = "प्रतिदिन नाम जप याद दिलाएं",
                checked = dailyReminderEnabled,
                onCheckedChange = { dailyReminderEnabled = it }
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Reminder time (placeholder)
            if (dailyReminderEnabled) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceGlassInput)
                        .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
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
                            text = "06:00 AM",
                            style = NaamSmaranTypography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "बदलें",
                        style = NaamSmaranTypography.labelMedium,
                        color = SharadMoonColors.accentPrimary,
                        modifier = Modifier.clickable { /* open time picker */ }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Preferences ─────────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🛠️ प्राथमिकताएं",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "हैप्टिक फीडबैक",
                subLabel = "बटन दबाने पर कंपन",
                checked = hapticEnabled,
                onCheckedChange = { hapticEnabled = it }
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Initial target setting (placeholder)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceGlassInput)
                    .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
                    .padding(Dimens.Space3),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "प्रारंभिक नाम जप लक्ष्य",
                        style = NaamSmaranTypography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Track B — राधा नाम जप",
                        style = NaamSmaranTypography.labelSmall,
                        color = TextTertiary
                    )
                }
                Text(
                    text = "२१,६००",
                    style = NaamSmaranTypography.bodyMedium,
                    color = SharadMoonColors.accentPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Data & Backup ────────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "💾 डेटा और बैकअप",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))

            SettingsToggleRow(
                label = "स्वत: बैकअप",
                subLabel = "प्रतिदिन JSON बैकअप सहेजें",
                checked = autoBackupEnabled,
                onCheckedChange = { autoBackupEnabled = it }
            )

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Export button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SharadMoonColors.accentPrimary.copy(alpha = 0.1f))
                    .border(1.dp, SharadMoonColors.accentPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable { /* export data */ }
                    .padding(Dimens.Space3),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📤 डेटा निर्यात करें",
                    style = NaamSmaranTypography.bodyMedium,
                    color = SharadMoonColors.accentPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── App Info ─────────────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "ℹ️ एप के बारे में",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentSecondary,
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
                text = "संस्करण 2.0.0-darshan",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Text(
                text = "जय श्री हित हरिवंश महाप्रभु 🙏\nराधावल्लभ संप्रदाय",
                style = NaamSmaranTypography.bodyMedium,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}

/**
 * Reusable toggle row for settings.
 */
@Composable
private fun SettingsToggleRow(
    label: String,
    subLabel: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
                checkedThumbColor = SharadMoonColors.accentPrimary,
                checkedTrackColor = SharadMoonColors.accentPrimary.copy(alpha = 0.35f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = SurfaceGlassInput
            )
        )
    }
}
