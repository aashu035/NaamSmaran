package com.radhavallabh.naamsmaran.ui.screens.maharas

import androidx.compose.runtime.Composable
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.ui.screens.sections.CarryOverSectionScreen

@Composable
fun MaharasScreen(onBack: () -> Unit) {
    CarryOverSectionScreen(
        spec = CarryOverSection.SUDHANIDHI,
        onBack = onBack
    )
}
