package com.radhavallabh.naamsmaran.ui.components

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.radhavallabh.naamsmaran.ui.theme.OverlayScrimDense
import com.radhavallabh.naamsmaran.ui.theme.OverlayScrimHeavy
import com.radhavallabh.naamsmaran.ui.theme.OverlayScrimLight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.radhavallabh.naamsmaran.R
import kotlinx.coroutines.delay

/**
 * ImageShowreelBackground — Fullscreen infinite image showreel.
 *
 * Cycles through bundled devotional images + user gallery images with crossfade.
 * Each image is displayed for [intervalMs] ms before transitioning.
 * Overlay gradient ensures foreground text remains legible.
 *
 * Gallery images (Uri-based) are prepended to the list so the user's
 * personal devotional photos appear first in the cycle.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

/** All bundled showreel drawable resource IDs — ordered for visual rhythm. */
val ShowreelDrawables: List<Int> = listOf(
    R.drawable.showreel_priya_pritam_01,
    R.drawable.showreel_harivansh_02,
    R.drawable.showreel_guruji_01,
    R.drawable.showreel_priya_pritam_02,
    R.drawable.showreel_harivansh_05,
    R.drawable.showreel_radha_naam_01,
    R.drawable.showreel_priya_pritam_03,
    R.drawable.showreel_guruji_03,
    R.drawable.showreel_harivansh_06,
    R.drawable.showreel_priya_pritam_05,
    R.drawable.showreel_harivansh_01,
    R.drawable.showreel_radha_naam_02,
    R.drawable.showreel_priya_pritam_04,
    R.drawable.showreel_harivansh_03,
    R.drawable.showreel_guruji_02,
    R.drawable.showreel_harivansh_04
)

/**
 * Sealed class to represent either a bundled drawable or a user gallery URI.
 * This lets us cycle through both types seamlessly.
 */
sealed class ShowreelImage {
    data class Bundled(val resId: Int) : ShowreelImage()
    data class Gallery(val uri: Uri) : ShowreelImage()
}

@Composable
fun ImageShowreelBackground(
    modifier: Modifier = Modifier,
    galleryUris: List<Uri> = emptyList(),
    intervalMs: Long = 6_000L,
    crossfadeDurationMs: Int = 1_500
) {
    // Merge gallery images (first) + bundled images into a single showreel list
    val allImages: List<ShowreelImage> = remember(galleryUris) {
        val gallery = galleryUris.map { ShowreelImage.Gallery(it) }
        val bundled = ShowreelDrawables.map { ShowreelImage.Bundled(it) }
        if (gallery.isNotEmpty()) {
            // Interleave: gallery images distributed among bundled ones
            val result = mutableListOf<ShowreelImage>()
            val ratio = (bundled.size.toFloat() / gallery.size.coerceAtLeast(1)).toInt().coerceAtLeast(1)
            var galleryIdx = 0
            for ((i, b) in bundled.withIndex()) {
                if (galleryIdx < gallery.size && i % ratio == 0) {
                    result.add(gallery[galleryIdx++])
                }
                result.add(b)
            }
            // Add any remaining gallery images
            while (galleryIdx < gallery.size) {
                result.add(gallery[galleryIdx++])
            }
            result
        } else {
            bundled
        }
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    // Auto-advance the showreel — delay FIRST, then advance, so index 0 shows immediately
    LaunchedEffect(allImages.size) {
        if (allImages.isEmpty()) return@LaunchedEffect
        while (true) {
            delay(intervalMs)                              // wait full interval
            currentIndex = (currentIndex + 1) % allImages.size  // then advance
        }
    }

    val context = LocalContext.current

    val colors = com.radhavallabh.naamsmaran.ui.theme.LocalNaamSmaranColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            // Deep indigo bg: eliminates black flash while first image decodes
            .background(colors.bgPrimary)
    ) {
        // Crossfade between images
        Crossfade(
            targetState = currentIndex,
            animationSpec = tween(durationMillis = crossfadeDurationMs),
            label = "showreel_crossfade"
        ) { index ->
            val image = allImages.getOrNull(index) ?: return@Crossfade

            when (image) {
                is ShowreelImage.Bundled -> {
                    Image(
                        painter = painterResource(id = image.resId),
                        contentDescription = null, // Decorative — screen readers skip it
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ShowreelImage.Gallery -> {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.uri)
                            .crossfade(false) // We handle crossfade ourselves
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Bottom-heavy gradient for legibility of counter & bottom sheet
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to OverlayScrimLight,                // subtle top scrim
                                0.42f to Color.Transparent,               // clear window above the focal text
                                0.58f to OverlayScrimLight,               // light center veil for text legibility
                                0.70f to OverlayScrimHeavy,               // darkening begins
                                1.0f to OverlayScrimDense                 // near-opaque bottom
                            )
                        )
                    )
                }
        )
    }
}
