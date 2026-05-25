package com.radhavallabh.naamsmaran.ui.screens.vrindavanlila

import androidx.compose.runtime.Composable
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.ui.screens.sections.CarryOverSectionScreen

@Composable
fun VrindavanLilaScreen(onBack: () -> Unit = {}) {
    CarryOverSectionScreen(
        spec = CarryOverSection.VRINDAVAN_LILA,
        onBack = onBack
    )
}
