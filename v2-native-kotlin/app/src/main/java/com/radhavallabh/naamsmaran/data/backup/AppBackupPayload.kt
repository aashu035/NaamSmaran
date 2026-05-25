package com.radhavallabh.naamsmaran.data.backup

import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord

data class AppSettingsSnapshot(
    val activeTheme: String,
    val initialTarget: Long,
    val targetIncrement: Int,
    val dayBoundaryHour: Int,
    val dailyReminderEnabled: Boolean,
    val dailyReminderHour: Int,
    val dailyReminderMinute: Int,
    val hapticEnabled: Boolean,
    val autoBackupEnabled: Boolean,
    val lastBackupAt: Long?,
    val galleryImageUris: List<String>
)

data class AppBackupPayload(
    val exportedAt: Long,
    val records: List<DailyRecord>,
    val settings: AppSettingsSnapshot
)
