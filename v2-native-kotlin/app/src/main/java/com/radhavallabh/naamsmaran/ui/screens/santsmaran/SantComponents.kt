package com.radhavallabh.naamsmaran.ui.screens.santsmaran

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.theme.ButtonShape
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SantDevanagari
import com.radhavallabh.naamsmaran.ui.theme.SantSmaranColors

/** Dawn background shared by the reading screen and the alarm screen. */
fun Modifier.santBackground(): Modifier = background(
    Brush.verticalGradient(
        listOf(SantSmaranColors.BackgroundTop, SantSmaranColors.BackgroundBottom)
    )
)

/** Filled saffron call-to-action button. */
@Composable
fun SantPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.Space16 - Dimens.Space2)
            .clip(ButtonShape)
            .background(SantSmaranColors.Saffron)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Space6, vertical = Dimens.Space4),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = SantDevanagari,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            color = SantSmaranColors.OnAccent,
            textAlign = TextAlign.Center
        )
    }
}

/** Outlined gold secondary button. */
@Composable
fun SantSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.Space16 - Dimens.Space2)
            .clip(ButtonShape)
            .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, ButtonShape)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Space6, vertical = Dimens.Space4),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = SantDevanagari,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            color = SantSmaranColors.Gold,
            textAlign = TextAlign.Center
        )
    }
}
