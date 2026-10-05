package com.radhavallabh.naamsmaran.domain.engine

import com.radhavallabh.naamsmaran.data.santsmaran.SantSmaranTestData
import com.radhavallabh.naamsmaran.domain.model.SantItem
import com.radhavallabh.naamsmaran.domain.model.SantPage
import com.radhavallabh.naamsmaran.domain.model.SantSection
import com.radhavallabh.naamsmaran.domain.model.SantSmaranContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SantSmaranPageBuilderTest {

    private val content = SantSmaranTestData.content()
    private val pages = SantSmaranPageBuilder.build(content)

    @Test
    fun realContent_has142Pages() {
        // opening + 138 entries + vandana + prayer + jaykara
        assertEquals(142, pages.size)
    }

    @Test
    fun realContent_startsWithOpening_endsWithJaykara() {
        assertEquals(SantPage.Opening(content.opening), pages.first())
        assertEquals(SantPage.Jaykara(content.jaykara), pages.last())
    }

    @Test
    fun realContent_entriesAreInGlobalOrder() {
        val orders = pages.filterIsInstance<SantPage.Entry>().map { it.item.order }
        assertEquals((1..138).toList(), orders)
    }

    @Test
    fun realContent_vandanaThenPrayer_beforeDhamSection() {
        val vandanaIndex = pages.indexOfFirst { it is SantPage.CollectiveVandana }
        assertEquals(SantPage.CollectiveVandana(content.collectiveVandana), pages[vandanaIndex])
        assertEquals(SantPage.Prayer(content.prayer), pages[vandanaIndex + 1])

        // Everything before is sections 1–10 (110 entries); the page right after the prayer is
        // the first item (#111) of the last section.
        val before = pages.subList(0, vandanaIndex).filterIsInstance<SantPage.Entry>()
        assertEquals(110, before.size)
        assertEquals(110, before.last().item.order)

        val after = pages[vandanaIndex + 2] as SantPage.Entry
        assertEquals(111, after.item.order)
        assertEquals(content.sections.last().title, after.sectionTitle)
    }

    @Test
    fun entries_carrySectionTitle() {
        val byOrder = content.sections.flatMap { s -> s.items.map { it.order to s.title } }.toMap()
        pages.filterIsInstance<SantPage.Entry>().forEach {
            assertEquals(byOrder[it.item.order], it.sectionTitle)
        }
    }

    @Test
    fun builder_sortsUnorderedInput_andUsesLastSectionAsClosing() {
        fun item(order: Int) = SantItem("i$order", "saint", "जय जय $order", null, null, null, order)
        val shuffled = SantSmaranContent(
            title = "t", alarmHour = 4, alarmMinute = 0, opening = "open",
            sections = listOf(
                SantSection("b", "second", listOf(item(4), item(3))),
                SantSection("a", "first", listOf(item(2), item(1))),
                SantSection("c", "closing", listOf(item(6), item(5)))
            ),
            collectiveVandana = "vandana", prayer = "prayer", jaykara = listOf("j1", "j2")
        )

        val result = SantSmaranPageBuilder.build(shuffled)

        val kinds = result.map {
            when (it) {
                is SantPage.Opening -> "open"
                is SantPage.Entry -> "e${it.item.order}"
                is SantPage.CollectiveVandana -> "vandana"
                is SantPage.Prayer -> "prayer"
                is SantPage.Jaykara -> "jaykara"
            }
        }
        assertEquals(
            listOf("open", "e1", "e2", "e3", "e4", "vandana", "prayer", "e5", "e6", "jaykara"),
            kinds
        )
        assertTrue(result.filterIsInstance<SantPage.Entry>().last().sectionTitle == "closing")
    }
}
