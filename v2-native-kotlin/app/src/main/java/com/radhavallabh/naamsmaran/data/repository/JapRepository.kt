package com.radhavallabh.naamsmaran.data.repository

import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.dao.DailyRecordDao
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.domain.engine.DayBoundaryEngine
import com.radhavallabh.naamsmaran.domain.engine.StreakEngine
import com.radhavallabh.naamsmaran.domain.engine.TargetEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JapRepository — Single point of access for all sadhana data.
 * Orchestrates Room DAO, DataStore, and core engines.
 */
@Singleton
class JapRepository @Inject constructor(
    private val dao: DailyRecordDao,
    private val settings: AppSettingsStore
) {

    // ═══════════════════════════════════════════════════════════
    // Read operations (reactive)
    // ═══════════════════════════════════════════════════════════

    fun getTodayRecord(): Flow<DailyRecord?> {
        val today = DayBoundaryEngine.getSpiritualDate()
        return dao.getRecordByDate(today)
    }

    fun getRecordByDate(date: String): Flow<DailyRecord?> =
        dao.getRecordByDate(date)

    fun getRecentRecords(limit: Int = 7): Flow<List<DailyRecord>> =
        dao.getRecentRecords(limit)

    fun getAllRecords(): Flow<List<DailyRecord>> =
        dao.getAllRecords()

    fun getLifetimeCount(): Flow<Long?> =
        dao.getLifetimeJapCount()

    fun getMaxStreak(): Flow<Int?> =
        dao.getMaxStreak()

    // ═══════════════════════════════════════════════════════════
    // Write operations
    // ═══════════════════════════════════════════════════════════

    /**
     * Ensure today's record exists. If it doesn't, compute today's target
     * from yesterday's data and create a fresh record.
     */
    suspend fun ensureTodayRecord(): DailyRecord {
        val today = DayBoundaryEngine.getSpiritualDate()
        val existing = dao.getRecordByDateOnce(today)
        if (existing != null) return existing

        // Compute today's target from yesterday
        val yesterday = DayBoundaryEngine.getYesterdaySpiritualDate()
        val yesterdayRecord = dao.getRecordByDateOnce(yesterday)
        val increment = settings.targetIncrement.first()

        val todayTarget = if (yesterdayRecord != null) {
            TargetEngine.calculateNextTarget(
                targetToday = yesterdayRecord.target,
                countToday = yesterdayRecord.did,
                increment = increment
            )
        } else {
            settings.initialTarget.first()
        }

        // Compute streak
        val yesterdayStreak = yesterdayRecord?.streakCount ?: 0
        // New day starts with streak from yesterday (will be evaluated at day close)

        val newRecord = DailyRecord(
            date = today,
            dayOfWeek = DayBoundaryEngine.getDayOfWeekHindi(),
            target = todayTarget,
        )

        dao.upsertRecord(newRecord)
        return newRecord
    }

    /**
     * Add jap count to today's total.
     * This is the primary write operation — called from quick-add buttons,
     * custom input, and tap-per-jap mode.
     */
    suspend fun addJapCount(count: Long) {
        val today = DayBoundaryEngine.getSpiritualDate()
        val record = ensureTodayRecord()
        val newTotal = record.did + count

        dao.updateJapCount(
            date = today,
            newCount = newTotal,
            checkNaam = newTotal > 0
        )

        // Update streak if target just met
        if (newTotal >= record.target && record.did < record.target) {
            val yesterday = DayBoundaryEngine.getYesterdaySpiritualDate()
            val yesterdayRecord = dao.getRecordByDateOnce(yesterday)
            val newStreak = StreakEngine.calculateStreak(
                currentStreak = yesterdayRecord?.streakCount ?: 0,
                targetToday = record.target,
                countToday = newTotal
            )
            dao.upsertRecord(record.copy(
                did = newTotal,
                checkNaamJap = true,
                streakCount = newStreak,
                updatedAt = System.currentTimeMillis()
            ))
        }
    }

    /**
     * Set exact jap count for today (used by custom number input).
     */
    suspend fun setJapCount(count: Long) {
        val today = DayBoundaryEngine.getSpiritualDate()
        val record = ensureTodayRecord()
        dao.updateJapCount(
            date = today,
            newCount = count,
            checkNaam = count > 0
        )
    }
}
