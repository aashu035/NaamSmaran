package com.radhavallabh.naamsmaran.ui.screens.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.platform.santsmaran.SantAlarmPermissions
import com.radhavallabh.naamsmaran.platform.santsmaran.SantScheduleResult
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val nextAlarmFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE, d MMM, HH:mm", Locale.ENGLISH)

/**
 * Settings card for "प्रातः संत नाम स्मरण": alarm on/off, time, open-now and test buttons, and a
 * live checklist of the system permissions the alarm needs (with a shortcut to each setting).
 */
@Composable
fun SantSmaranSettingsCard(
    onOpenSantSmaran: () -> Unit,
    viewModel: SantAlarmSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val colors = LocalNaamSmaranColors.current

    // Permissions can change in system Settings while the app is in the background.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.refresh()
        // Denied for good (or blocked at app level): the dialog can't reappear → open the settings page.
        if (!granted) context.startSafely(SantAlarmPermissions.notificationSettings(context))
    }

    val timeText = remember(state.hour, state.minute) {
        String.format(Locale.ENGLISH, "%02d:%02d", state.hour, state.minute)
    }
    val nextAlarmText = remember(state.nextAlarmMillis) {
        state.nextAlarmMillis?.let {
            nextAlarmFormatter.format(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()))
        }
    }

    GlassCardColumn {
        Text(
            text = "प्रातः संत नाम स्मरण",
            style = NaamSmaranTypography.titleSmall,
            color = colors.accentPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(Dimens.Space3))

        SettingsToggleRow(
            label = "सुबह का अलार्म",
            subLabel = "प्रतिदिन जगाए और संत नाम स्मरण खोले",
            checked = state.enabled,
            onCheckedChange = viewModel::setEnabled
        )

        if (state.enabled) {
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceGlassInput, RoundedCornerShape(Dimens.Space3))
                    .border(Dimens.Space1 / 4, BorderGlass, RoundedCornerShape(Dimens.Space3))
                    .clickable {
                        TimePickerDialog(
                            context,
                            { _, hour, minute -> viewModel.setTime(hour, minute) },
                            state.hour,
                            state.minute,
                            false
                        ).show()
                    }
                    .padding(Dimens.Space3),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "समय", style = NaamSmaranTypography.labelMedium, color = TextTertiary)
                    Text(
                        text = timeText,
                        style = NaamSmaranTypography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(text = "बदलें", style = NaamSmaranTypography.labelMedium, color = colors.accentPrimary)
            }

            nextAlarmText?.let {
                Spacer(modifier = Modifier.height(Dimens.Space2))
                Text(
                    text = "अगला अलार्म: $it",
                    style = NaamSmaranTypography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        if (state.lastResult == SantScheduleResult.EXACT_ALARM_DENIED) {
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "सिस्टम ने सटीक अलार्म की अनुमति नहीं दी — नीचे \"सटीक अलार्म\" ठीक करें।",
                style = NaamSmaranTypography.bodySmall,
                color = colors.stateStreakFire
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space3))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
        ) {
            ActionButton(label = "स्मरण खोलें", modifier = Modifier.weight(1f), onClick = onOpenSantSmaran)
            ActionButton(label = "टेस्ट (10 सेकंड)", modifier = Modifier.weight(1f)) {
                viewModel.scheduleTest()
            }
        }

        if (state.testArmed) {
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "10 सेकंड में अलार्म बजेगा — अभी फ़ोन लॉक कर दें।",
                style = NaamSmaranTypography.bodySmall,
                color = colors.accentSecondary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space6))
        Text(
            text = "अलार्म के लिए आवश्यक अनुमतियाँ",
            style = NaamSmaranTypography.labelMedium,
            color = TextSecondary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(Dimens.Space2))

        val readiness = state.readiness

        ChecklistRow(
            label = "सूचनाएँ",
            okDetail = "चालू",
            fixDetail = "बंद — अलार्म स्क्रीन नहीं दिखेगी",
            ok = readiness.notificationsAllowed,
            actionLabel = "चालू करें"
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                context.startSafely(SantAlarmPermissions.notificationSettings(context))
            }
        }
        ChecklistRow(
            label = "सटीक अलार्म",
            okDetail = "अनुमति है",
            fixDetail = "अनुमति नहीं — अलार्म समय पर नहीं बजेगा",
            ok = readiness.exactAlarmAllowed,
            actionLabel = "अनुमति दें"
        ) { context.startSafely(SantAlarmPermissions.exactAlarmSettings(context)) }
        ChecklistRow(
            label = "लॉक स्क्रीन पर अलार्म",
            okDetail = "अनुमति है",
            fixDetail = "अनुमति नहीं — लॉक स्क्रीन पर अलार्म नहीं खुलेगा",
            ok = readiness.fullScreenIntentAllowed,
            actionLabel = "अनुमति दें"
        ) { context.startSafely(SantAlarmPermissions.fullScreenIntentSettings(context)) }
        ChecklistRow(
            label = "बैटरी प्रतिबंध (सुझावित)",
            okDetail = "प्रतिबंध नहीं",
            fixDetail = "इस ऐप को \"प्रतिबंधित नहीं\" करें ताकि अलार्म न रुके",
            ok = readiness.batteryUnrestricted,
            actionLabel = "खोलें"
        ) { context.startSafely(SantAlarmPermissions.batteryOptimizationSettings()) }

        Spacer(modifier = Modifier.height(Dimens.Space2))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startSafely(
                        Intent(Intent.ACTION_VIEW, SantAlarmPermissions.oemGuideUrl())
                    )
                }
                .padding(vertical = Dimens.Space2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Xiaomi / Realme / Oppo / Vivo: सेटिंग्स में ऑटोस्टार्ट चालू रखें",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(Dimens.Space2))
            Text(text = "गाइड", style = NaamSmaranTypography.labelMedium, color = colors.accentPrimary)
        }
    }
}

@Composable
private fun ChecklistRow(
    label: String,
    okDetail: String,
    fixDetail: String,
    ok: Boolean,
    actionLabel: String,
    onFix: () -> Unit
) {
    val colors = LocalNaamSmaranColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Space2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (ok) "✓" else "✗",
            color = if (ok) colors.stateExceeded else colors.stateStreakFire,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(Dimens.Space6)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = NaamSmaranTypography.bodyMedium, color = TextPrimary)
            Text(
                text = if (ok) okDetail else fixDetail,
                style = NaamSmaranTypography.labelSmall,
                color = TextTertiary
            )
        }
        if (!ok) {
            Text(
                text = actionLabel,
                style = NaamSmaranTypography.labelMedium,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(onClick = onFix)
                    .padding(Dimens.Space2)
            )
        }
    }
}

/** Opens a system settings page; some OEM builds lack a given page, so never crash on it. */
private fun Context.startSafely(intent: Intent) {
    try {
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        // Fall back to the app's own details page, which every Android build has.
        try {
            startActivity(SantAlarmPermissions.appDetailsSettings(this).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: ActivityNotFoundException) {
            // Nothing more we can do.
        }
    }
}
