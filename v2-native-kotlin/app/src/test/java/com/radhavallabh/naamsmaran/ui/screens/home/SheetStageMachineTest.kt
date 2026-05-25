package com.radhavallabh.naamsmaran.ui.screens.home

import org.junit.Assert.assertEquals
import org.junit.Test

class SheetStageMachineTest {

    @Test
    fun `expand steps hidden to quick actions to full grid`() {
        assertEquals(SheetStage.QuickActions, SheetStageMachine.expand(SheetStage.Hidden))
        assertEquals(SheetStage.FullGrid, SheetStageMachine.expand(SheetStage.QuickActions))
        assertEquals(SheetStage.FullGrid, SheetStageMachine.expand(SheetStage.FullGrid))
    }

    @Test
    fun `collapse steps full grid to quick actions to hidden`() {
        assertEquals(SheetStage.QuickActions, SheetStageMachine.collapse(SheetStage.FullGrid))
        assertEquals(SheetStage.Hidden, SheetStageMachine.collapse(SheetStage.QuickActions))
        assertEquals(SheetStage.Hidden, SheetStageMachine.collapse(SheetStage.Hidden))
    }
}
