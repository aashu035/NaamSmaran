package com.radhavallabh.naamsmaran.ui.screens.santsmaran

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radhavallabh.naamsmaran.domain.model.SantPage
import com.radhavallabh.naamsmaran.ui.theme.ChipShape
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranMotion
import com.radhavallabh.naamsmaran.ui.theme.SantDevanagari
import com.radhavallabh.naamsmaran.ui.theme.SantSmaranColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * "प्रातः संत नाम स्मरण" — full-screen, swipe-through reading screen.
 *
 * One card per page: opening line → sections 1–10 → collective vandana → prayer →
 * धाम/ब्रज/सखी section → jaykara (see [com.radhavallabh.naamsmaran.domain.engine.SantSmaranPageBuilder]).
 * Fully offline; text and photos come from the bundled assets.
 */
@Composable
fun SantSmaranScreen(
    onBack: () -> Unit,
    viewModel: SantSmaranViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    KeepScreenOn()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .santBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when {
            state.isLoading -> CenterMessage("लोड हो रहा है…")
            state.errorMessage != null -> CenterMessage(state.errorMessage.orEmpty())
            else -> SantSmaranBody(
                state = state,
                onBack = onBack,
                onToggleAutoAdvance = viewModel::toggleAutoAdvance,
                onAutoAdvanceFinished = { viewModel.setAutoAdvance(false) },
                onToggleListView = viewModel::toggleListView,
                onCloseListView = { viewModel.setListView(false) }
            )
        }
    }
}

@Composable
private fun SantSmaranBody(
    state: SantSmaranUiState,
    onBack: () -> Unit,
    onToggleAutoAdvance: () -> Unit,
    onAutoAdvanceFinished: () -> Unit,
    onToggleListView: () -> Unit,
    onCloseListView: () -> Unit
) {
    val pages = state.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    // In list view, Back returns to the pager instead of leaving the screen.
    BackHandler(enabled = state.listView, onBack = onCloseListView)

    // Optional auto-advance: restarts its timer after every page change (manual swipes included).
    LaunchedEffect(state.autoAdvance, state.listView, pagerState.currentPage) {
        if (state.autoAdvance && !state.listView) {
            delay(Dimens.SantAutoAdvanceMillis)
            if (pagerState.currentPage < pages.lastIndex) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(NaamSmaranMotion.MaxAnimationDurationMs)
                )
            } else {
                onAutoAdvanceFinished()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SantTopBar(
            counter = "${pagerState.currentPage + 1} / ${pages.size}",
            autoAdvance = state.autoAdvance,
            listView = state.listView,
            onBack = onBack,
            onToggleAutoAdvance = onToggleAutoAdvance,
            onToggleListView = onToggleListView
        )
        ProgressLine(fraction = (pagerState.currentPage + 1).toFloat() / pages.size)

        if (state.listView) {
            SantListView(
                pages = pages,
                currentPage = pagerState.currentPage,
                onSelect = { index ->
                    onCloseListView()
                    scope.launch { pagerState.scrollToPage(index) }
                }
            )
        } else {
            SectionChipRow(label = chipLabel(pages[pagerState.currentPage]))
            SantPager(pages = pages, pagerState = pagerState, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SantPager(
    pages: List<SantPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
        beyondViewportPageCount = 1,
        key = { index -> pageKey(pages[index]) }
    ) { index ->
        when (val page = pages[index]) {
            is SantPage.Opening -> SantTextPageContent(lines = listOf(page.text), bodySize = 26)
            is SantPage.Entry -> SantEntryPageContent(item = page.item)
            is SantPage.CollectiveVandana -> SantTextPageContent(lines = listOf(page.text))
            is SantPage.Prayer -> SantTextPageContent(lines = listOf(page.text))
            is SantPage.Jaykara -> SantTextPageContent(lines = page.lines, bodySize = 22)
        }
    }
}

// ── Header ──────────────────────────────────────────────────────────────

@Composable
private fun SantTopBar(
    counter: String,
    autoAdvance: Boolean,
    listView: Boolean,
    onBack: () -> Unit,
    onToggleAutoAdvance: () -> Unit,
    onToggleListView: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Space4, vertical = Dimens.Space3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.Space10)
                .clip(CircleShape)
                .background(SantSmaranColors.CardSurface)
                .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "←", color = SantSmaranColors.TextPrimary, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(Dimens.Space3))

        Text(
            text = counter,
            fontSize = 14.sp,
            color = SantSmaranColors.TextMuted,
            modifier = Modifier.weight(1f)
        )

        ToggleChip(text = "ऑटो", selected = autoAdvance, onClick = onToggleAutoAdvance)
        Spacer(modifier = Modifier.width(Dimens.Space2))
        ToggleChip(text = "सूची", selected = listView, onClick = onToggleListView)
    }
}

@Composable
private fun ToggleChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(ChipShape)
            .background(if (selected) SantSmaranColors.Saffron else SantSmaranColors.CardSurface)
            .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, ChipShape)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Space4, vertical = Dimens.Space2),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = SantDevanagari,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = if (selected) SantSmaranColors.OnAccent else SantSmaranColors.Gold
        )
    }
}

@Composable
private fun ProgressLine(fraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SantIndicatorHeight)
            .background(SantSmaranColors.GoldSoft)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(Dimens.SantIndicatorHeight)
                .background(SantSmaranColors.Saffron)
        )
    }
}

/** Fixed-height row so the pager doesn't jump when the chip appears/disappears (opening page). */
@Composable
private fun SectionChipRow(label: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.Space12),
        contentAlignment = Alignment.Center
    ) {
        if (label != null) {
            Box(
                modifier = Modifier
                    .clip(ChipShape)
                    .background(SantSmaranColors.CardSurface)
                    .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, ChipShape)
                    .padding(horizontal = Dimens.Space4, vertical = Dimens.Space2)
            ) {
                Text(
                    text = label,
                    fontFamily = SantDevanagari,
                    fontWeight = FontWeight.W700,
                    fontSize = 14.sp,
                    color = SantSmaranColors.Gold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ── List view (optional) ────────────────────────────────────────────────

@Composable
private fun SantListView(
    pages: List<SantPage>,
    currentPage: Int,
    onSelect: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentPage)
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = Dimens.Space4,
            vertical = Dimens.Space3
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space2)
    ) {
        itemsIndexed(pages, key = { _, page -> pageKey(page) }) { index, page ->
            val previousSection = (pages.getOrNull(index - 1) as? SantPage.Entry)?.sectionTitle
            val sectionHeader = (page as? SantPage.Entry)?.sectionTitle?.takeIf { it != previousSection }
                ?: chipLabel(page).takeIf { page !is SantPage.Entry }

            Column {
                if (sectionHeader != null) {
                    Text(
                        text = sectionHeader,
                        fontFamily = SantDevanagari,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = SantSmaranColors.Gold,
                        modifier = Modifier.padding(top = Dimens.Space3, bottom = Dimens.Space1)
                    )
                }
                SantListRow(
                    page = page,
                    selected = index == currentPage,
                    onClick = { onSelect(index) }
                )
            }
        }
    }
}

@Composable
private fun SantListRow(
    page: SantPage,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(Dimens.Space3)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) SantSmaranColors.CardSurface else Color.Transparent)
            .border(
                Dimens.Space1 / 4,
                if (selected) SantSmaranColors.CardBorder else SantSmaranColors.GoldSoft,
                shape
            )
            .clickable(onClick = onClick)
            .padding(Dimens.Space3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (number, text) = when (page) {
            is SantPage.Entry -> page.item.order.toString() to page.item.displayName
            is SantPage.Opening -> "" to page.text
            is SantPage.CollectiveVandana -> "" to page.text
            is SantPage.Prayer -> "" to page.text
            is SantPage.Jaykara -> "" to page.lines.joinToString(separator = " ")
        }
        Text(
            text = number,
            fontSize = 14.sp,
            fontWeight = FontWeight.W700,
            color = SantSmaranColors.Gold,
            modifier = Modifier.width(Dimens.Space10)
        )
        Text(
            text = text,
            fontFamily = SantDevanagari,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = SantSmaranColors.TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── Helpers ─────────────────────────────────────────────────────────────

@Composable
private fun CenterMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontFamily = SantDevanagari,
            fontSize = 18.sp,
            color = SantSmaranColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(Dimens.Space6)
        )
    }
}

/** Keeps the display on while reading (a recitation can take well over the screen timeout). */
@Composable
private fun KeepScreenOn() {
    val view = LocalView.current
    DisposableEffect(view) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }
}

/** Header chip for a page: the section title for entries, a short label for the closing texts. */
private fun chipLabel(page: SantPage): String? = when (page) {
    is SantPage.Opening -> null
    is SantPage.Entry -> page.sectionTitle
    is SantPage.CollectiveVandana -> "सामूहिक वंदना"
    is SantPage.Prayer -> "प्रार्थना"
    is SantPage.Jaykara -> "जयकारा"
}

private fun pageKey(page: SantPage): Any = when (page) {
    is SantPage.Opening -> "opening"
    is SantPage.Entry -> "entry:${page.item.id}"
    is SantPage.CollectiveVandana -> "vandana"
    is SantPage.Prayer -> "prayer"
    is SantPage.Jaykara -> "jaykara"
}
