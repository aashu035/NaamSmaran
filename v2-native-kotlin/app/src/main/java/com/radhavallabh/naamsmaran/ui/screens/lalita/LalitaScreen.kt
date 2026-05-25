package com.radhavallabh.naamsmaran.ui.screens.lalita

import androidx.compose.runtime.Composable
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.ui.screens.sections.CarryOverSectionScreen

@Composable
fun LalitaScreen(onBack: () -> Unit) {
    CarryOverSectionScreen(
        spec = CarryOverSection.SEVAK_VANI,
        onBack = onBack
    )
}
