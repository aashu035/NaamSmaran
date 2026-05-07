package com.radhavallabh.naamsmaran.ui.screens.chaturdas

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.components.GlassCardColumn
import com.radhavallabh.naamsmaran.ui.components.SectionScaffold
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.SharadMoonColors
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassInput
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * ChaturdasScreen — Section 6: नित्य पाठ रसोपासना
 *
 * Simple daily completion toggle (done / not done).
 * No target escalation — just mark complete each day.
 * Shows 7-day streak + monthly calendar completion grid.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
@Composable
fun ChaturdasScreen(onBack: () -> Unit) {

    val today = LocalDate.now()
    var isDoneToday by remember { mutableStateOf(false) }

    // Placeholder data — will be DB-driven in next phase
    val completedDays = remember { setOf(1, 3, 5, 6, 8, 9, 10, 12, 14, 15, 17, 18, 20, 21, 22) }
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val monthStr = today.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("hi")))

    // 7-day streak (placeholder)
    val streakDays = 5

    SectionScaffold(
        title = "नित्य पाठ",
        emoji = "🌙",
        onBack = onBack
    ) {

        // ── Today's Toggle ────────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🌙 नित्य पाठ रसोपासना",
                style = NaamSmaranTypography.titleMedium,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "प्रतिदिन पूर्ण करें",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space5))

            // Large toggle button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isDoneToday)
                            StateExceeded.copy(alpha = 0.2f)
                        else
                            SharadMoonColors.accentPrimary.copy(alpha = 0.15f)
                    )
                    .border(
                        2.dp,
                        if (isDoneToday) StateExceeded else SharadMoonColors.accentPrimary.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { isDoneToday = !isDoneToday }
                    .padding(vertical = Dimens.Space5),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isDoneToday) "✅" else "🙏",
                        fontSize = 40.sp
                    )
                    Spacer(modifier = Modifier.height(Dimens.Space2))
                    Text(
                        text = if (isDoneToday) "आज पूर्ण हुआ!" else "आज पूर्ण करें",
                        style = NaamSmaranTypography.titleSmall,
                        color = if (isDoneToday) StateExceeded else TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── 7-Day Streak ──────────────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "🔥 लगातार श्रृंखला",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val dayLabels = listOf("सो", "मं", "बु", "गु", "शु", "श", "र")
                dayLabels.forEachIndexed { i, dayLabel ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i < streakDays)
                                        StateExceeded.copy(alpha = 0.35f)
                                    else
                                        SurfaceGlassInput
                                )
                                .border(
                                    1.dp,
                                    if (i < streakDays) StateExceeded.copy(alpha = 0.7f) else BorderGlass,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (i < streakDays) "✓" else "",
                                style = NaamSmaranTypography.labelSmall,
                                color = StateExceeded
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dayLabel,
                            style = NaamSmaranTypography.labelSmall,
                            color = TextTertiary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.Space3))
            Text(
                text = "लगातार $streakDays दिन 🔥",
                style = NaamSmaranTypography.bodyMedium,
                color = StateExceeded,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        // ── Monthly Calendar Grid ────────────────────────────────────────────
        GlassCardColumn {
            Text(
                text = "📅 $monthStr",
                style = NaamSmaranTypography.titleSmall,
                color = SharadMoonColors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "इस महीने: ${completedDays.size} / $daysInMonth दिन",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            // 7-column calendar grid
            val weeks = (1..daysInMonth).chunked(7)
            weeks.forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    week.forEach { day ->
                        val isCompleted = day in completedDays
                        val isToday = day == today.dayOfMonth
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isToday && isDoneToday -> StateExceeded.copy(alpha = 0.35f)
                                        isCompleted -> SharadMoonColors.accentPrimary.copy(alpha = 0.25f)
                                        isToday -> SharadMoonColors.accentSecondary.copy(alpha = 0.2f)
                                        else -> SurfaceGlassInput
                                    }
                                )
                                .border(
                                    width = if (isToday) 2.dp else 1.dp,
                                    color = when {
                                        isToday && isDoneToday -> StateExceeded
                                        isToday -> SharadMoonColors.accentSecondary
                                        isCompleted -> SharadMoonColors.accentPrimary.copy(alpha = 0.5f)
                                        else -> BorderGlass
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isCompleted || (isToday && isDoneToday)) "✓" else "$day",
                                style = NaamSmaranTypography.labelSmall,
                                color = when {
                                    isCompleted || (isToday && isDoneToday) -> StateExceeded
                                    isToday -> SharadMoonColors.accentSecondary
                                    else -> TextTertiary
                                },
                                fontSize = if (isCompleted || (isToday && isDoneToday)) 14.sp else 11.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}
