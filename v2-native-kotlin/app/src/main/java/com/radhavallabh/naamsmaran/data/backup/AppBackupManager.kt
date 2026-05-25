package com.radhavallabh.naamsmaran.data.backup

import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.NaamSmaranDatabase
import com.radhavallabh.naamsmaran.data.local.settingsDataStore
import com.radhavallabh.naamsmaran.data.repository.JapRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: JapRepository,
    private val settingsStore: AppSettingsStore
) {

    suspend fun createPayload(): AppBackupPayload = AppBackupPayload(
        exportedAt = System.currentTimeMillis(),
        records = repository.getAllRecordsOnce(),
        settings = settingsStore.snapshot()
    )

    suspend fun exportToUri(uri: Uri) = withContext(Dispatchers.IO) {
        val payload = createPayload()
        context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
            writer.write(AppBackupCodec.toJson(payload))
        } ?: error("Backup export stream unavailable")
        settingsStore.setLastBackupAt(payload.exportedAt)
    }

    suspend fun restoreFromUri(uri: Uri) = withContext(Dispatchers.IO) {
        val payload = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
            AppBackupCodec.fromJson(reader.readText())
        } ?: error("Backup restore stream unavailable")

        repository.replaceAllRecords(
            records = payload.records,
            initialTarget = payload.settings.initialTarget,
            targetIncrement = payload.settings.targetIncrement
        )
        settingsStore.restoreFromSnapshot(payload.settings)
    }

    suspend fun writeAutomaticBackup() = withContext(Dispatchers.IO) {
        val payload = createPayload()
        val backupFile = automaticBackupFile()
        backupFile.parentFile?.mkdirs()
        backupFile.writeText(AppBackupCodec.toJson(payload))
        settingsStore.setLastBackupAt(payload.exportedAt)
    }

    fun automaticBackupFile(): File =
        File(context.filesDir, "backups/naam-smaran-auto.json")

    companion object {
        fun fromContext(context: Context): AppBackupManager {
            val appContext = context.applicationContext
            val store = AppSettingsStore(appContext.settingsDataStore)
            val database = Room.databaseBuilder(
                appContext,
                NaamSmaranDatabase::class.java,
                "naam_smaran_db"
            )
                .addMigrations(NaamSmaranDatabase.MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
            val repository = JapRepository(database, database.dailyRecordDao(), store)
            return AppBackupManager(appContext, repository, store)
        }
    }
}
