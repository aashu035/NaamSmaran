package com.radhavallabh.naamsmaran.data.repository

import androidx.room.withTransaction
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.NaamSmaranDatabase
import com.radhavallabh.naamsmaran.data.local.dao.DailyRecordDao
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord
import com.radhavallabh.naamsmaran.domain.engine.CarryOverEngine
import com.radhavallabh.naamsmaran.domain.engine.DayBoundaryEngine
import com.radhavallabh.naamsmaran.domain.engine.NityaPathProgressCalculator
import com.radhavallabh.naamsmaran.domain.engine.StreakEngine
import com.radhavallabh.naamsmaran.domain.engine.TargetEngine
import com.radhavallabh.naamsmaran.domain.model.CarryOverSection
import com.radhavallabh.naamsmaran.domain.model.CarryOverSectionState
import com.radhavallabh.naamsmaran.domain.model.didFor
import com.radhavallabh.naamsmaran.domain.model.targetFor
import com.radhavallabh.naamsmaran.domain.model.withCarryOverProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JapRepository — Single point of access for all sadhana data.
 * Orchestrates Room DAO, DataStore, and core engines.
 */
@Singleton
class JapRepository @Inject constructor(
    private val database: NaamSmaranDatabase,
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

    fun getCarryOverSectionState(section: CarryOverSection): Flow<CarryOverSectionState> =
        combine(getTodayRecord(), getAllRecords()) { todayRecord, allRecords ->
            val today = todayRecord ?: buildFreshRecord()
            val overallRead = allRecords.sumOf { it.didFor(section) }.coerceAtMost(section.totalUnits.takeIf { it > 0 } ?: Long.MAX_VALUE)
            CarryOverSectionState(
                spec = section,
                todayTarget = today.targetFor(section),
                todayDone = today.didFor(section),
                overallRead = overallRead
            )
        }

    fun isAshtayamDoneToday(): Flow<Boolean> =
        getTodayRecord().map { it?.checkAshtayamSeva ?: false }

    fun getNityaPathProgress(today: java.time.LocalDate = java.time.LocalDate.now()) =
        getAllRecords().map { records ->
            NityaPathProgressCalculator.calculate(records, today)
        }

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

        val newRecord = DailyRecord(
            date = today,
            dayOfWeek = DayBoundaryEngine.getDayOfWeekHindi(),
            target = todayTarget,
            chaturasi_target = nextCarryOverTarget(yesterdayRecord, CarryOverSection.CHATURASI),
            sudhanidhi_target = nextCarryOverTarget(yesterdayRecord, CarryOverSection.SUDHANIDHI),
            sevakVani_target = nextCarryOverTarget(yesterdayRecord, CarryOverSection.SEVAK_VANI),
            vrindavan_target = nextCarryOverTarget(yesterdayRecord, CarryOverSection.VRINDAVAN_LILA),
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
        val record = ensureTodayRecord()
        val newTotal = record.did + count
        upsertJapRecord(record, newTotal)
    }

    /**
     * Set exact jap count for today (used by custom number input).
     */
    suspend fun setJapCount(count: Long) {
        val record = ensureTodayRecord()
        upsertJapRecord(record, count)
    }

    suspend fun addCarryOverProgress(section: CarryOverSection, count: Long) {
        val record = ensureTodayRecord()
        val updatedDid = (record.didFor(section) + count).coerceAtLeast(0L)
        dao.upsertRecord(
            record.withCarryOverProgress(
                section = section,
                did = updatedDid,
                checked = updatedDid > 0
            )
        )
    }

    suspend fun resetCarryOverProgress(section: CarryOverSection) {
        val record = ensureTodayRecord()
        dao.upsertRecord(
            record.withCarryOverProgress(
                section = section,
                did = 0L,
                checked = false
            )
        )
    }

    suspend fun setAshtayamDoneToday(done: Boolean) {
        val record = ensureTodayRecord()
        dao.upsertRecord(
            record.copy(
                checkAshtayamSeva = done,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun setNityaPathDoneToday(done: Boolean) {
        val record = ensureTodayRecord()
        dao.upsertRecord(
            record.copy(
                checkNityaPath = done,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun replaceAllRecords(records: List<DailyRecord>, initialTarget: Long, targetIncrement: Int) {
        database.withTransaction {
            dao.deleteAllRecords()
            if (records.isNotEmpty()) {
                dao.upsertRecords(recomputeAllRecords(records.sortedBy { it.date }, initialTarget, targetIncrement))
            }
        }
    }

    suspend fun getAllRecordsOnce(): List<DailyRecord> =
        dao.getAllRecordsOnce()

    private suspend fun upsertJapRecord(record: DailyRecord, newCount: Long) {
        val yesterday = DayBoundaryEngine.getYesterdaySpiritualDate()
        val yesterdayRecord = dao.getRecordByDateOnce(yesterday)
        val newStreak = StreakEngine.calculateStreak(
            currentStreak = yesterdayRecord?.streakCount ?: 0,
            targetToday = record.target,
            countToday = newCount
        )

        dao.upsertRecord(
            record.copy(
                did = newCount,
                checkNaamJap = newCount > 0,
                streakCount = newStreak,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    private fun nextCarryOverTarget(
        yesterdayRecord: DailyRecord?,
        section: CarryOverSection
    ): Long {
        if (yesterdayRecord == null) return section.dailyBase
        return CarryOverEngine.calculateNextTarget(
            targetToday = yesterdayRecord.targetFor(section),
            countToday = yesterdayRecord.didFor(section),
            dailyBase = section.dailyBase,
            metIncrement = section.metIncrement
        )
    }

    private fun buildFreshRecord(): DailyRecord = DailyRecord(
        date = DayBoundaryEngine.getSpiritualDate(),
        dayOfWeek = DayBoundaryEngine.getDayOfWeekHindi(),
    )

    private fun recomputeAllRecords(
        records: List<DailyRecord>,
        initialTarget: Long,
        increment: Int
    ): List<DailyRecord> {
        if (records.isEmpty()) return emptyList()
        val result = mutableListOf<DailyRecord>()

        records.forEachIndexed { index, original ->
            val previous = result.getOrNull(index - 1)
            val todayTarget = if (previous == null) {
                initialTarget
            } else {
                TargetEngine.calculateNextTarget(previous.target, previous.did, increment)
            }
            val streak = if (previous == null) {
                StreakEngine.calculateStreak(0, todayTarget, original.did)
            } else {
                StreakEngine.calculateStreak(previous.streakCount, todayTarget, original.did)
            }

            var updated = original.copy(
                target = todayTarget,
                streakCount = streak,
                updatedAt = System.currentTimeMillis()
            )

            CarryOverSection.entries.forEach { section ->
                val nextTarget = if (previous == null) {
                    section.dailyBase
                } else {
                    CarryOverEngine.calculateNextTarget(
                        targetToday = previous.targetFor(section),
                        countToday = previous.didFor(section),
                        dailyBase = section.dailyBase,
                        metIncrement = section.metIncrement
                    )
                }

                updated = updated.withCarryOverProgress(
                    section = section,
                    target = nextTarget,
                    did = updated.didFor(section),
                    checked = updated.didFor(section) > 0,
                    updatedAt = updated.updatedAt
                )
            }

            result += updated
        }

        return result
    }
}
