package com.radhavallabh.naamsmaran.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.NaamSmaranDatabase
import com.radhavallabh.naamsmaran.data.local.dao.DailyRecordDao
import com.radhavallabh.naamsmaran.data.local.settingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule — Hilt dependency injection module.
 * Provides Room database, DAO, DataStore, and settings instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NaamSmaranDatabase {
        return Room.databaseBuilder(
            context,
            NaamSmaranDatabase::class.java,
            "naam_smaran_db"
        )
            .addMigrations(NaamSmaranDatabase.MIGRATION_1_2, NaamSmaranDatabase.MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDailyRecordDao(database: NaamSmaranDatabase): DailyRecordDao {
        return database.dailyRecordDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.settingsDataStore
    }

    @Provides
    @Singleton
    fun provideAppSettingsStore(
        dataStore: DataStore<Preferences>
    ): AppSettingsStore {
        return AppSettingsStore(dataStore)
    }
}
