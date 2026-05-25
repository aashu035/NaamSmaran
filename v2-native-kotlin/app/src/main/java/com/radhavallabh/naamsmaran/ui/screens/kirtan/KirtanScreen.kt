package com.radhavallabh.naamsmaran.ui.screens.kirtan

import androidx.compose.runtime.Composable
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.ui.screens.sections.CarryOverSectionScreen

@Composable
fun KirtanScreen(onBack: () -> Unit) {
    CarryOverSectionScreen(
        spec = CarryOverSection.CHATURASI,
        onBack = onBack
    )
}
