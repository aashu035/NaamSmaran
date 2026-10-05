package com.radhavallabh.naamsmaran.data.santsmaran

import com.radhavallabh.naamsmaran.domain.model.SantItem
import com.radhavallabh.naamsmaran.domain.model.SantSection
import com.radhavallabh.naamsmaran.domain.model.SantSmaranContent

/**
 * Gson DTOs for assets/sant_smaran.json.
 *
 * Field names match the JSON keys exactly (R8 keeps this package, see
 * proguard-rules.pro). Optional keys are *omitted* in the JSON when absent, so
 * they are nullable here. Gson bypasses constructors, hence no default values.
 */
internal data class SantSmaranDto(
    val schemaVersion: Int,
    val title: String,
    val alarm: AlarmDto,
    val opening: String,
    val sections: List<SectionDto>,
    val collectiveVandana: String,
    val prayer: String,
    val jaykara: List<String>
)

internal data class AlarmDto(
    val hour: Int,
    val minute: Int
)

internal data class SectionDto(
    val id: String,
    val title: String,
    val items: List<ItemDto>
)

internal data class ItemDto(
    val id: String,
    val type: String,
    val displayName: String,
    val guptNaam: String?,
    val shortReference: String?,
    val imageAsset: String?,
    val order: Int
)

internal const val SUPPORTED_SCHEMA_VERSION = 1

/** Maps the DTO to domain models. No string is modified. */
internal fun SantSmaranDto.toDomain(): SantSmaranContent {
    require(schemaVersion == SUPPORTED_SCHEMA_VERSION) {
        "Unsupported sant_smaran.json schemaVersion=$schemaVersion (expected $SUPPORTED_SCHEMA_VERSION)"
    }
    return SantSmaranContent(
        title = title,
        alarmHour = alarm.hour,
        alarmMinute = alarm.minute,
        opening = opening,
        sections = sections.map { section ->
            SantSection(
                id = section.id,
                title = section.title,
                items = section.items.map { item ->
                    SantItem(
                        id = item.id,
                        type = item.type,
                        displayName = item.displayName,
                        guptNaam = item.guptNaam,
                        shortReference = item.shortReference,
                        imageAsset = item.imageAsset,
                        order = item.order
                    )
                }
            )
        },
        collectiveVandana = collectiveVandana,
        prayer = prayer,
        jaykara = jaykara
    )
}
