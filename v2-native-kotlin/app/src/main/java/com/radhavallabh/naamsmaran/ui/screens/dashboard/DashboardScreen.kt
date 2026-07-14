package com.radhavallabh.naamsmaran.ui.screens.dashboard

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.ui.components.CircularProgressIndicator
import com.radhavallabh.naamsmaran.ui.components.GlassCard
import com.radhavallabh.naamsmaran.ui.components.charts.AreaChart
import com.radhavallabh.naamsmaran.ui.components.charts.DonutChart
import com.radhavallabh.naamsmaran.ui.theme.BorderGlass
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTypography
import com.radhavallabh.naamsmaran.ui.theme.StateExceeded
import com.radhavallabh.naamsmaran.ui.theme.StateExceededBg
import com.radhavallabh.naamsmaran.ui.theme.StatePartial
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlass
import com.radhavallabh.naamsmaran.ui.theme.SurfaceGlassElevated
import com.radhavallabh.naamsmaran.ui.theme.TextPrimary
import com.radhavallabh.naamsmaran.ui.theme.TextSecondary
import com.radhavallabh.naamsmaran.ui.theme.TextTertiary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigateToSection: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val colors = LocalNaamSmaranColors.current
    val view = LocalView.current
    val context = LocalContext.current

    val weeklyOverview by viewModel.weeklyOverview.collectAsState()
    val trendData by viewModel.trendData.collectAsState()
    val achievementDonut by viewModel.achievementDonut.collectAsState()
    val sadhanaOverview by viewModel.sadhanaOverview.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // Local bottom-nav state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.bgPrimary,
                        colors.bgPrimary.copy(alpha = 0.95f),
                        colors.bgPrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── Top Header Navigation ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingCard, vertical = Dimens.Space3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, CircleShape)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Space4))

                Column {
                    Text(
                        text = "राधे राधे",
                        style = NaamSmaranTypography.titleMedium,
                        color = colors.accentPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "साधना सिंहावलोकन (Sadhana Overview)",
                        style = NaamSmaranTypography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Dotted separator line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderGlass)
            )

            // Scrollable Dashboard Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = Dimens.PaddingSection,
                        end = Dimens.PaddingSection,
                        top = Dimens.Space4,
                        bottom = Dimens.Space20 + 20.dp // Leave extra margin for bottom nav
                    ),
                verticalArrangement = Arrangement.spacedBy(Dimens.GapStack)
            ) {
                // ZONE 1: सप्ताह की झलक (Weekly Glance Strip)
                WeeklyGlanceSection(weeklyOverview)

                // ZONE 2 & 3: Double Grid (Trends and Achievement breakdown)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
                ) {
                    TargetTrendsSection(
                        trendData = trendData,
                        lineColor = colors.accentPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    AchievementDonutSection(
                        donutData = achievementDonut,
                        completedColor = StateExceeded,
                        partialColor = StatePartial,
                        missedColor = Color(0x33FFFFFF),
                        modifier = Modifier.weight(1f)
                    )
                }

                // ZONE 4: सातों साधना संक्षिप्त स्थिति (7-Sadhana Carousel)
                SadhanaCarouselSection(sadhanaOverview) { sectionId ->
                    performHapticFeedback(view)
                    onNavigateToSection(sectionId)
                }

                // ZONE 5: लक्ष्य शीघ्र क्रियाएँ (Quick Actions Grid)
                QuickActionsGridSection(sadhanaOverview) { sectionId ->
                    performHapticFeedback(view)
                    viewModel.quickIncrementSadhana(sectionId)
                }

                // ZONE 6: आज का प्रेरक पद (Inspirational Verse)
                InspirationalVerseSection(colors.accentPrimary)
            }
        }

        // ZONE 7: Fixed Bottom Navigation Bar
        BottomNavigationBar(
            activeTab = activeTab,
            onTabSelected = { index ->
                performHapticFeedback(view)
                when (index) {
                    0 -> { /* Already on dashboard */ }
                    1 -> {
                        Toast.makeText(context, "विश्लेषण — जल्द आ रहा है 🙏", Toast.LENGTH_SHORT).show()
                    }
                    2 -> onNavigateToHome() // FAB → Home for quick jap
                    3 -> onNavigateToSettings() // लक्ष्य → Settings
                    4 -> onNavigateToSettings() // प्रोफ़ाइल → Settings
                }
                activeTab = index
            },
            accentColor = colors.accentPrimary,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UI COMPONENT SUB-SECTIONS (Zone 1 - 7 Implementation)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun WeeklyGlanceSection(overview: WeeklyOverview) {
    val colors = LocalNaamSmaranColors.current
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "सप्ताह की झलक",
                    style = NaamSmaranTypography.titleMedium,
                    color = colors.accentGold,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceGlassElevated)
                        .border(1.dp, BorderGlass, RoundedCornerShape(999.dp))
                        .padding(horizontal = Dimens.Space3, vertical = Dimens.Space1)
                ) {
                    Text(
                        text = "इस सप्ताह ▾",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Space3))

            // Score details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${overview.completedCount} / ${overview.totalDays} दिन — लक्ष्य पूरे किए",
                    style = NaamSmaranTypography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "साधना प्रोग्रेस: ${((overview.completedCount.toFloat() / 7f) * 100).toInt()}%",
                    style = NaamSmaranTypography.bodySmall,
                    color = TextTertiary
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space2))

            // Score progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.BarHeight)
                    .clip(RoundedCornerShape(Dimens.BarHeight))
                    .background(Color(0x14FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(overview.completedCount.toFloat() / 7f)
                        .height(Dimens.BarHeight)
                        .clip(RoundedCornerShape(Dimens.BarHeight))
                        .background(
                            Brush.horizontalGradient(
                                listOf(colors.accentPrimary, colors.accentGold)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space4))

            // 7 Days indicators row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                overview.days.forEach { day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.Space1)
                    ) {
                        Text(
                            text = day.dayName,
                            fontSize = 10.sp,
                            color = TextTertiary,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when (day.state) {
                                        DayState.DONE -> StateExceededBg
                                        DayState.PARTIAL -> Color(0x1FF5CBA7)
                                        DayState.EMPTY -> Color.Transparent
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = when (day.state) {
                                        DayState.DONE -> StateExceeded
                                        DayState.PARTIAL -> colors.accentGold
                                        DayState.EMPTY -> BorderGlass
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (day.state) {
                                    DayState.DONE -> "✓"
                                    DayState.PARTIAL -> "▲"
                                    DayState.EMPTY -> ""
                                },
                                color = when (day.state) {
                                    DayState.DONE -> StateExceeded
                                    DayState.PARTIAL -> colors.accentGold
                                    DayState.EMPTY -> Color.Transparent
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TargetTrendsSection(
    trendData: List<Long>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"))
    val totalSum = trendData.sum()

    GlassCard(modifier = modifier.height(220.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "लक्ष्य रुझान (30 दिन)",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(Dimens.Space1))
            Text(
                text = formatter.format(totalSum),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "कुल नाम जप",
                style = NaamSmaranTypography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(Dimens.Space3))
            
            AreaChart(
                points = trendData,
                lineColor = lineColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AchievementDonutSection(
    donutData: AchievementDonut,
    completedColor: Color,
    partialColor: Color,
    missedColor: Color,
    modifier: Modifier = Modifier
) {
    val total = (donutData.completed + donutData.partial + donutData.missed).toFloat().coerceAtLeast(1f)
    val completePercent = ((donutData.completed.toFloat() / total) * 100).toInt()

    GlassCard(modifier = modifier.height(230.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "लक्ष्य बनाम प्राप्ति",
                style = NaamSmaranTypography.bodySmall,
                color = TextTertiary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(Dimens.Space2))

            DonutChart(
                values = listOf(
                    donutData.completed.toFloat(),
                    donutData.partial.toFloat(),
                    donutData.missed.toFloat()
                ),
                colors = listOf(completedColor, partialColor, missedColor),
                size = 72.dp,
                strokeWidth = 8.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$completePercent%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "पूर्णता",
                        fontSize = 8.sp,
                        color = TextTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Space2))

            // Micro Legend Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(completedColor))
                    Text(text = "${donutData.completed} दिन", fontSize = 9.sp, color = TextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(partialColor))
                    Text(text = "${donutData.partial} दिन", fontSize = 9.sp, color = TextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(missedColor))
                    Text(text = "${donutData.missed} दिन", fontSize = 9.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun SadhanaCarouselSection(
    sadhanaList: List<SadhanaCardData>,
    onCardClick: (Int) -> Unit
) {
    val colors = LocalNaamSmaranColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "सातों साधना संक्षिप्त स्थिति",
            style = NaamSmaranTypography.titleMedium,
            color = colors.accentPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Dimens.Space1)
        )
        Spacer(modifier = Modifier.height(Dimens.Space2))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
        ) {
            items(sadhanaList) { sadhana ->
                GlassCard(
                    modifier = Modifier.size(152.dp, 224.dp),
                    glowColor = sadhana.accentColor.copy(alpha = 0.05f),
                    onClick = { onCardClick(sadhana.id) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = sadhana.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )

                        CircularProgressIndicator(
                            progress = sadhana.percentage,
                            size = 52.dp,
                            strokeWidth = 4.dp,
                            progressColor = sadhana.accentColor
                        ) {
                            Text(
                                text = "${(sadhana.percentage * 100).toInt()}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = sadhana.didLabel,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(Dimens.Space1))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(sadhana.accentColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (sadhana.streak != null) "🔥 ${sadhana.streak} दिन" else sadhana.statusText,
                                    fontSize = 8.sp,
                                    color = sadhana.accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionsGridSection(
    sadhanaList: List<SadhanaCardData>,
    onActionClick: (Int) -> Unit
) {
    val colors = LocalNaamSmaranColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "साधना शीघ्र क्रियाएँ (Quick Log)",
            style = NaamSmaranTypography.titleMedium,
            color = colors.accentGold,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Dimens.Space1)
        )
        Spacer(modifier = Modifier.height(Dimens.Space2))

        // Manually chunking list into rows of 2 for clean 2-column layout
        val chunked = sadhanaList.chunked(2)
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.Space2),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space3)
            ) {
                rowItems.forEach { item ->
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp),
                        onClick = { onActionClick(item.id) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(item.accentColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.id.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = item.accentColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(Dimens.Space2))
                                Text(
                                    text = when (item.id) {
                                        1 -> "+१०८ जप"
                                        2 -> "+१ पद"
                                        3 -> "+१ श्लोक"
                                        4 -> "+१ छंद"
                                        5 -> "सेवा पद्धति टॉगल"
                                        6 -> "पाठ टॉगल"
                                        7 -> "+१ छंद (लीला)"
                                        else -> item.name
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.width(Dimens.Space1))
                            Text(
                                text = "→",
                                color = TextTertiary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                // Add a blank spacer if the last row has only 1 item to preserve sizing
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun InspirationalVerseSection(accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = accentColor.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elegant vertical quote line highlight
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor)
            )

            Spacer(modifier = Modifier.width(Dimens.Space4))

            Column {
                Text(
                    text = "आज का प्रेरक पद",
                    style = NaamSmaranTypography.bodySmall,
                    color = TextTertiary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(Dimens.Space1))
                Text(
                    // TODO: User will provide the verse text
                    text = "...",
                    style = NaamSmaranTypography.bodyLarge,
                    color = TextPrimary,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(Dimens.Space1))
                Text(
                    // TODO: User will provide the attribution
                    text = "",
                    style = NaamSmaranTypography.bodySmall,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BOTTOM NAVIGATION BAR
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun BottomNavigationBar(
    activeTab: Int,
    onTabSelected: (Int) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
            .height(Dimens.BottomNavHeight)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xD9070010)) // highly opaque deep night indigo
            .border(1.dp, BorderGlass, RoundedCornerShape(32.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTabItem(
                isSelected = activeTab == 0,
                icon = Icons.Default.Home,
                label = "डैशबोर्ड",
                accentColor = accentColor,
                onClick = { onTabSelected(0) }
            )
            BottomTabItem(
                isSelected = activeTab == 1,
                icon = Icons.Default.DateRange,
                label = "विश्लेषण",
                accentColor = accentColor,
                onClick = { onTabSelected(1) }
            )

            // Central Glowing Floating Action Button (FAB)
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(accentColor, accentColor.copy(alpha = 0.6f))
                        )
                    )
                    .clickable { onTabSelected(2) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Quick Add",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            BottomTabItem(
                isSelected = activeTab == 3,
                icon = Icons.Default.Star,
                label = "लक्ष्य",
                accentColor = accentColor,
                onClick = { onTabSelected(3) }
            )
            BottomTabItem(
                isSelected = activeTab == 4,
                icon = Icons.Default.Person,
                label = "प्रोफ़ाइल",
                accentColor = accentColor,
                onClick = { onTabSelected(4) }
            )
        }
    }
}

@Composable
fun BottomTabItem(
    isSelected: Boolean,
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    val tabColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else TextTertiary,
        label = "TabColorSpring"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = Dimens.Space2)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Space1)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tabColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = tabColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// Perform haptic feedback
private fun performHapticFeedback(view: View) {
    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
}
