package com.radhavallabh.naamsmaran.data.backup

import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppBackupCodecTest {

    @Test
    fun `round trips records and settings through json`() {
        val payload = AppBackupPayload(
            exportedAt = 1_234_567L,
            records = listOf(
                DailyRecord(
                    date = "2026-05-15",
                    target = 21_600L,
                    did = 1_080L,
                    checkNaamJap = true,
                    chaturasi_target = 12L,
                    chaturasi_did = 3L,
                    checkChaturasi = true
                )
            ),
            settings = AppSettingsSnapshot(
                activeTheme = NaamSmaranThemeId.SHARAD_MOON.name,
                initialTarget = 21_600L,
                targetIncrement = 5_000,
                dayBoundaryHour = 3,
                dailyReminderEnabled = true,
                dailyReminderHour = 6,
                dailyReminderMinute = 15,
                hapticEnabled = true,
                autoBackupEnabled = false,
                lastBackupAt = null,
                galleryImageUris = listOf("content://gallery/1")
            )
        )

        val json = AppBackupCodec.toJson(payload)
        val decoded = AppBackupCodec.fromJson(json)

        assertEquals(payload, decoded)
        assertTrue(json.contains("2026-05-15"))
        assertTrue(json.contains("gallery/1"))
    }
}
