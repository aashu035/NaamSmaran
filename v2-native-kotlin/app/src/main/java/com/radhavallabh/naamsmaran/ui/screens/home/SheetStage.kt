package com.radhavallabh.naamsmaran.ui.screens.home

enum class SheetStage {
    Hidden,
    QuickActions,
    FullGrid
}

object SheetStageMachine {

    fun expand(stage: SheetStage): SheetStage = when (stage) {
        SheetStage.Hidden -> SheetStage.QuickActions
        SheetStage.QuickActions -> SheetStage.FullGrid
        SheetStage.FullGrid -> SheetStage.FullGrid
    }

    fun collapse(stage: SheetStage): SheetStage = when (stage) {
        SheetStage.FullGrid -> SheetStage.QuickActions
        SheetStage.QuickActions -> SheetStage.Hidden
        SheetStage.Hidden -> SheetStage.Hidden
    }
}
