package com.radhavallabh.naamsmaran.ui.screens.santsmaran

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.radhavallabh.naamsmaran.data.santsmaran.santImageUri
import com.radhavallabh.naamsmaran.domain.model.SantItem
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.SantDevanagari
import com.radhavallabh.naamsmaran.ui.theme.SantSmaranColors

private const val RING_ALPHA_OUTER = 0.30f
private const val RING_ALPHA_MIDDLE = 0.20f
private const val RING_ALPHA_INNER = 0.12f

/**
 * One saint / dham / sakhi page: photo (or decorative name-card), then
 * `displayName` large, `guptNaam` (if any) as a second line, `shortReference` (if any) small
 * and muted. All text is shown exactly as given in sant_smaran.json.
 */
@Composable
fun SantEntryPageContent(
    item: SantItem,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val photoHeight = (maxHeight * Dimens.SantPhotoHeightFraction)
            .coerceIn(Dimens.SantPhotoMinHeight, Dimens.SantPhotoMaxHeight)
        val nameCardHeight = (maxHeight * Dimens.SantNameCardHeightFraction)
            .coerceIn(Dimens.SantNameCardMinHeight, Dimens.SantNameCardMaxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.Space6, vertical = Dimens.Space4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item.imageAsset != null) {
                SantPhotoFrame(imageAsset = item.imageAsset, description = item.displayName, height = photoHeight)
            } else {
                SantNameCard(order = item.order, height = nameCardHeight)
            }

            Spacer(modifier = Modifier.height(Dimens.Space6))

            Text(
                text = item.displayName,
                fontFamily = SantDevanagari,
                fontWeight = FontWeight.W700,
                fontSize = 26.sp,
                lineHeight = 38.sp,
                color = SantSmaranColors.TextPrimary,
                textAlign = TextAlign.Center
            )

            item.guptNaam?.let { gupt ->
                Spacer(modifier = Modifier.height(Dimens.Space3))
                Text(
                    text = gupt,
                    fontFamily = SantDevanagari,
                    fontWeight = FontWeight.W400,
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    color = SantSmaranColors.Gold,
                    textAlign = TextAlign.Center
                )
            }

            item.shortReference?.let { reference ->
                Spacer(modifier = Modifier.height(Dimens.Space3))
                Text(
                    text = reference,
                    fontFamily = SantDevanagari,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = SantSmaranColors.TextMuted,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Space8))
        }
    }
}

/** Saint photo, shown whole (Fit) inside a gold-edged frame — portraits are never cropped. */
@Composable
private fun SantPhotoFrame(
    imageAsset: String,
    description: String,
    height: Dp
) {
    val shape = RoundedCornerShape(Dimens.SantCardCorner)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(SantSmaranColors.MaroonDeep)
            .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, shape),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = santImageUri(imageAsset),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Space1)
        )
    }
}

/**
 * Decorative card for entries without a photo (59 of 138): maroon panel, concentric gold rings
 * and the entry's position in the recitation. Plain geometry only — no religious symbols.
 */
@Composable
private fun SantNameCard(
    order: Int,
    height: Dp
) {
    val shape = RoundedCornerShape(Dimens.SantCardCorner)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(
                Brush.verticalGradient(listOf(SantSmaranColors.Maroon, SantSmaranColors.MaroonDeep))
            )
            .border(Dimens.Space1 / 4, SantSmaranColors.CardBorder, shape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = Dimens.Space1.toPx() / 4f
            val maxRadius = size.minDimension / 2f
            listOf(
                0.90f to RING_ALPHA_OUTER,
                0.74f to RING_ALPHA_MIDDLE,
                0.58f to RING_ALPHA_INNER
            ).forEach { (fraction, alpha) ->
                drawCircle(
                    color = SantSmaranColors.Gold.copy(alpha = alpha),
                    radius = maxRadius * fraction,
                    center = center,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        Text(
            text = order.toString(),
            fontFamily = SantDevanagari,
            fontWeight = FontWeight.W700,
            fontSize = 40.sp,
            color = SantSmaranColors.Gold
        )
    }
}

/**
 * Text-only page (opening line, collective vandana, prayer, jaykara). Each entry of [lines] is
 * its own paragraph; the strings are rendered exactly as given.
 */
@Composable
fun SantTextPageContent(
    lines: List<String>,
    modifier: Modifier = Modifier,
    bodySize: Int = 24
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.Space6, vertical = Dimens.Space6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Space4)
    ) {
        lines.forEach { line ->
            Text(
                text = line,
                fontFamily = SantDevanagari,
                fontWeight = FontWeight.W400,
                fontSize = bodySize.sp,
                lineHeight = (bodySize * 1.6f).sp,
                color = SantSmaranColors.TextPrimary,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Space8))
    }
}
