package com.radhavallabh.naamsmaran.domain.model

/**
 * प्रातः संत नाम स्मरण — domain models.
 *
 * Mirrors assets/sant_smaran.json (schemaVersion 1). Every string here is
 * carried verbatim from that file: never trim, transliterate, re-spell or
 * "fix" a Devanagari value. If a name looks wrong, ask the user.
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */
data class SantSmaranContent(
    val title: String,
    val alarmHour: Int,
    val alarmMinute: Int,
    val opening: String,
    val sections: List<SantSection>,
    val collectiveVandana: String,
    val prayer: String,
    val jaykara: List<String>
) {
    /** Total number of named entries (saints, dhams, sakhis) across all sections. */
    val itemCount: Int get() = sections.sumOf { it.items.size }
}

data class SantSection(
    val id: String,
    val title: String,
    val items: List<SantItem>
)

data class SantItem(
    val id: String,
    /** "saint" | "dham" | "sakhi" — kept as the raw string from the JSON. */
    val type: String,
    val displayName: String,
    val guptNaam: String?,
    val shortReference: String?,
    /** Path relative to the app's assets root (e.g. "saints/kabir.webp"), or null → name-card. */
    val imageAsset: String?,
    /** Global recitation order, 1..N, contiguous. */
    val order: Int
)

/**
 * One swipeable page of the reading screen.
 * Built by [com.radhavallabh.naamsmaran.domain.engine.SantSmaranPageBuilder].
 */
sealed interface SantPage {
    /** Opening line, shown first. */
    data class Opening(val text: String) : SantPage

    /** One named entry, with the section it belongs to (for the section chip). */
    data class Entry(val sectionTitle: String, val item: SantItem) : SantPage

    /** Collective vandana, spoken before the last (धाम…) section. */
    data class CollectiveVandana(val text: String) : SantPage

    /** Prayer, spoken right after the collective vandana. */
    data class Prayer(val text: String) : SantPage

    /** Jaykara lines, exactly as given — always the very last page. */
    data class Jaykara(val lines: List<String>) : SantPage
}
