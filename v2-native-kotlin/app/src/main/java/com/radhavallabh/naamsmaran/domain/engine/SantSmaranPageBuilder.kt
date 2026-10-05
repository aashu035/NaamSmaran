package com.radhavallabh.naamsmaran.domain.engine

import com.radhavallabh.naamsmaran.domain.model.SantPage
import com.radhavallabh.naamsmaran.domain.model.SantSmaranContent

/**
 * Builds the ordered pages of the "प्रातः संत नाम स्मरण" reading screen.
 *
 * Order (matches the user's spoken sequence, handoff §4):
 *   1. opening line
 *   2. every item of every section EXCEPT the last, sorted by `order`
 *   3. collective vandana
 *   4. prayer
 *   5. every item of the LAST section (धाम, ब्रज एवं सखी वृंद), sorted by `order`
 *   6. jaykara (always the very last page)
 *
 * The "last section" is `sections.last()` — no hardcoded section id.
 * Pure function: no Android dependencies, trivially unit-testable.
 */
object SantSmaranPageBuilder {

    fun build(content: SantSmaranContent): List<SantPage> {
        val sectionsInOrder = content.sections.sortedBy { section ->
            section.items.minOfOrNull { it.order } ?: Int.MAX_VALUE
        }
        val closingSection = sectionsInOrder.lastOrNull()
        val leadingSections = sectionsInOrder.dropLast(1)

        val pages = ArrayList<SantPage>(content.itemCount + 4)
        pages += SantPage.Opening(content.opening)

        leadingSections.forEach { section ->
            section.items.sortedBy { it.order }.forEach { item ->
                pages += SantPage.Entry(section.title, item)
            }
        }

        pages += SantPage.CollectiveVandana(content.collectiveVandana)
        pages += SantPage.Prayer(content.prayer)

        closingSection?.let { section ->
            section.items.sortedBy { it.order }.forEach { item ->
                pages += SantPage.Entry(section.title, item)
            }
        }

        pages += SantPage.Jaykara(content.jaykara)
        return pages
    }
}
