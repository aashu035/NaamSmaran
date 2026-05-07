package com.radhavallabh.naamsmaran.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import kotlinx.coroutines.flow.Flow

/**
 * DailyRecordDao — Room DAO for daily record CRUD operations.
 * All queries return Flow for reactive UI updates.
 */
@Dao
interface DailyRecordDao {

    @Query("SELECT * FROM daily_records WHERE date = :date LIMIT 1")
    fun getRecordByDate(date: String): Flow<DailyRecord?>

    @Query("SELECT * FROM daily_records WHERE date = :date LIMIT 1")
    suspend fun getRecordByDateOnce(date: String): DailyRecord?

    @Query("SELECT * FROM daily_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<DailyRecord>>

    @Query("SELECT * FROM daily_records ORDER BY date DESC LIMIT :limit")
    fun getRecentRecords(limit: Int): Flow<List<DailyRecord>>

    @Query("SELECT * FROM daily_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getRecordsBetween(startDate: String, endDate: String): Flow<List<DailyRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecord(record: DailyRecord)

    @Update
    suspend fun updateRecord(record: DailyRecord)

    @Query("UPDATE daily_records SET did = :newCount, checkNaamJap = :checkNaam, updatedAt = :timestamp WHERE date = :date")
    suspend fun updateJapCount(date: String, newCount: Long, checkNaam: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT SUM(did) FROM daily_records")
    fun getLifetimeJapCount(): Flow<Long?>

    @Query("SELECT MAX(streakCount) FROM daily_records")
    fun getMaxStreak(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM daily_records WHERE did >= target AND target > 0")
    fun getDaysTargetMet(): Flow<Int?>
}
