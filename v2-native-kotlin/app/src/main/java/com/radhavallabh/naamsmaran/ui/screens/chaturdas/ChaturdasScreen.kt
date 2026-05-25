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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ChaturdasScreen(
    onBack: () -> Unit,
    viewModel: SectionsViewModel = hiltViewModel()
) {
    val progress by viewModel.nityaPathProgress.collectAsState()
    val colors = LocalNaamSmaranColors.current
    val today = LocalDate.now()
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val monthStr = today.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("hi")))
    val completedDays = progress.completedDaysInMonth

    SectionScaffold(
        title = "नित्य पाठ रसोपासना",
        emoji = null,
        onBack = onBack
    ) {
        GlassCardColumn {
            Text(
                text = "नित्य पाठ रसोपासना",
                style = NaamSmaranTypography.titleMedium,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "प्रतिदिन पूर्ण करें",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space5))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (progress.isDoneToday) StateExceeded.copy(alpha = 0.2f)
                        else colors.accentPrimary.copy(alpha = 0.15f),
                        RoundedCornerShape(Dimens.Space5)
                    )
                    .border(
                        width = Dimens.Space1 / 2,
                        color = if (progress.isDoneToday) StateExceeded else colors.accentPrimary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(Dimens.Space5)
                    )
                    .clickable { viewModel.setNityaPathDoneToday(!progress.isDoneToday) }
                    .padding(vertical = Dimens.Space5),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (progress.isDoneToday) "✅" else "🙏",
                        fontSize = 40.sp
                    )
                    Spacer(modifier = Modifier.height(Dimens.Space2))
                    Text(
                        text = if (progress.isDoneToday) "आज पूर्ण हुआ!" else "आज पूर्ण करें",
                        style = NaamSmaranTypography.titleSmall,
                        color = if (progress.isDoneToday) StateExceeded else TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = "लगातार शृंखला",
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))
            Text(
                text = if (progress.currentStreak > 0) "लगातार ${progress.currentStreak} दिन" else "अभी कोई शृंखला नहीं",
                style = NaamSmaranTypography.bodyMedium,
                color = if (progress.currentStreak > 0) StateExceeded else TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.GapStack))

        GlassCardColumn {
            Text(
                text = monthStr,
                style = NaamSmaranTypography.titleSmall,
                color = colors.accentPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))
            Text(
                text = "इस महीने: ${completedDays.size} / $daysInMonth दिन",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))

            (1..daysInMonth).chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space2)
                ) {
                    week.forEach { day ->
                        val isCompleted = day in completedDays
                        val isToday = day == today.dayOfMonth
                        Box(
                            modifier = Modifier
                                .size(Dimens.Space8)
                                .background(
                                    when {
                                        isToday && progress.isDoneToday -> StateExceeded.copy(alpha = 0.35f)
                                        isCompleted -> colors.accentPrimary.copy(alpha = 0.25f)
                                        isToday -> colors.accentSecondary.copy(alpha = 0.2f)
                                        else -> SurfaceGlassInput
                                    },
                                    CircleShape
                                )
                                .border(
                                    width = if (isToday) Dimens.Space1 / 2 else Dimens.Space1 / 4,
                                    color = when {
                                        isToday && progress.isDoneToday -> StateExceeded
                                        isToday -> colors.accentSecondary
                                        isCompleted -> colors.accentPrimary.copy(alpha = 0.5f)
                                        else -> BorderGlass
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isCompleted || (isToday && progress.isDoneToday)) "✓" else "$day",
                                style = NaamSmaranTypography.labelSmall,
                                color = when {
                                    isCompleted || (isToday && progress.isDoneToday) -> StateExceeded
                                    isToday -> colors.accentSecondary
                                    else -> TextTertiary
                                },
                                fontSize = if (isCompleted || (isToday && progress.isDoneToday)) 14.sp else 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.size(Dimens.Space8))
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.Space2))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}
