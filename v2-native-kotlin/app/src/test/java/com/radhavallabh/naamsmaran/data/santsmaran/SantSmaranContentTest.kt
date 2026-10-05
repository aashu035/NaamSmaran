package com.radhavallabh.naamsmaran.data.santsmaran

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.security.MessageDigest

/**
 * Guards the verified "प्रातः संत नाम स्मरण" bundle (VERIFICATION.md).
 * The user is strict about every Devanagari string: the golden hash below makes
 * any edit — even a single matra, BOM or newline change — fail loudly.
 */
class SantSmaranContentTest {

    @Test
    fun jsonFile_matchesVerifiedGoldenHash() {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(SantSmaranTestData.jsonFile.readBytes())
            .joinToString("") { "%02x".format(it) }
        assertEquals(
            "sant_smaran.json differs from the verified bundle. If this change is intentional " +
                "(user-approved), update VERIFIED_SHA256 and re-run VERIFICATION.md checks.",
            VERIFIED_SHA256,
            digest
        )
    }

    @Test
    fun jsonFile_hasNoByteOrderMark() {
        val head = SantSmaranTestData.jsonFile.readBytes().take(3)
        assertFalse(head == listOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
    }

    @Test
    fun structure_is11SectionsAnd138Items() {
        val content = SantSmaranTestData.content()
        assertEquals("प्रातः संत नाम स्मरण", content.title)
        assertEquals(11, content.sections.size)
        assertEquals(138, content.itemCount)
    }

    @Test
    fun ids_areUnique() {
        val ids = SantSmaranTestData.content().sections.flatMap { it.items }.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun order_isContiguousFrom1To138() {
        val orders = SantSmaranTestData.content().sections.flatMap { it.items }.map { it.order }.sorted()
        assertEquals((1..138).toList(), orders)
    }

    @Test
    fun displayNames_startWithJayJayAndHaveNoBlankOrDoubleSpaces() {
        SantSmaranTestData.content().sections.flatMap { it.items }.forEach { item ->
            assertTrue("#${item.order} ${item.id}", item.displayName.startsWith("जय जय "))
            assertFalse("double space in #${item.order}", item.displayName.contains("  "))
            assertTrue(item.displayName.isNotBlank())
        }
    }

    @Test
    fun optionalFields_areNullOrNonBlank() {
        SantSmaranTestData.content().sections.flatMap { it.items }.forEach { item ->
            item.guptNaam?.let { assertTrue("guptNaam #${item.order}", it.isNotBlank()) }
            item.shortReference?.let { assertTrue("shortReference #${item.order}", it.isNotBlank()) }
            item.imageAsset?.let { assertTrue("imageAsset #${item.order}", it.startsWith("saints/")) }
        }
    }

    @Test
    fun itemTypes_areKnown() {
        val types = SantSmaranTestData.content().sections.flatMap { it.items }.map { it.type }.toSet()
        assertEquals(setOf("saint", "dham", "sakhi"), types)
    }

    @Test
    fun closingTexts_arePresent() {
        val content = SantSmaranTestData.content()
        assertTrue(content.opening.isNotBlank())
        assertTrue(content.collectiveVandana.isNotBlank())
        assertTrue(content.prayer.isNotBlank())
        assertEquals(11, content.jaykara.size)
        assertTrue(content.jaykara.all { it.isNotBlank() })
    }

    @Test
    fun alarm_defaultsTo0400() {
        val content = SantSmaranTestData.content()
        assertEquals(4, content.alarmHour)
        assertEquals(0, content.alarmMinute)
    }

    @Test
    fun images_79Referenced() {
        val referenced = SantSmaranTestData.content().sections.flatMap { it.items }.mapNotNull { it.imageAsset }
        assertEquals(79, referenced.size)
        assertEquals(79, referenced.toSet().size)
    }

    @Test
    fun images_everyReferencedFileExists_andNoOrphans() {
        // saints/ is gitignored (public repo) → absent on a fresh clone; the app then shows name-cards.
        assumeTrue("saints/ not present (fresh clone)", SantSmaranTestData.saintsDir.isDirectory)

        val referenced = SantSmaranTestData.content().sections.flatMap { it.items }
            .mapNotNull { it.imageAsset }
            .map { it.removePrefix("saints/") }
            .toSet()
        val onDisk = SantSmaranTestData.saintsDir.listFiles { f -> f.extension == "webp" }!!
            .map { it.name }.toSet()

        assertEquals("referenced but missing on disk", emptySet<String>(), referenced - onDisk)
        assertEquals("on disk but never referenced", emptySet<String>(), onDisk - referenced)
    }

    companion object {
        /** SHA-256 of the verified assets/sant_smaran.json (from sant_smaran_bundle.zip, 5 Oct 2026). */
        const val VERIFIED_SHA256 = "5cea10365b768df994ff377f8c6c611a39cc73c263ebe3153772d30e0ad7c4cc"
    }
}
