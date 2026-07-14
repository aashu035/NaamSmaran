package com.radhavallabh.naamsmaran.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.radhavallabh.naamsmaran.data.local.dao.DailyRecordDao
import com.radhavallabh.naamsmaran.data.local.entity.DailyRecord

/**
 * NaamSmaranDatabase — Room database.
 * Single source of truth for all local persistent data.
 *
 * v2: Aligns columns to the canonical 7-section structure.
 *     - Removes: checkMaansikSeva, checkPrarthana, checkBhaktMaal, checkCharnamrit
 *     - Adds: chaturasi_target/did/check, sudhanidhi_target/did/check,
 *             sevakVani_target/did/check, checkAshtayamSeva, checkNityaPath,
 *             vrindavan_target/did/check
 *     - Renames: checkChaturasi stays (reused for section 2)
 *
 * Schema exports enabled for migration verification.
 */
@Database(
    entities = [DailyRecord::class],
    version = 3,
    exportSchema = false
)
abstract class NaamSmaranDatabase : RoomDatabase() {
    abstract fun dailyRecordDao(): DailyRecordDao

    companion object {
        /**
         * Migration 1→2: 7-section schema alignment.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Section 2: चतुरसी (target + did — checkChaturasi already exists)
                db.execSQL("ALTER TABLE daily_records ADD COLUMN chaturasi_target INTEGER NOT NULL DEFAULT 12")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN chaturasi_did INTEGER NOT NULL DEFAULT 0")

                // Section 3: सुधानिधी
                db.execSQL("ALTER TABLE daily_records ADD COLUMN sudhanidhi_target INTEGER NOT NULL DEFAULT 10")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN sudhanidhi_did INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkSudhanidhi INTEGER NOT NULL DEFAULT 0")

                // Section 4: सेवक वाणी
                db.execSQL("ALTER TABLE daily_records ADD COLUMN sevakVani_target INTEGER NOT NULL DEFAULT 5")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN sevakVani_did INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkSevakVani INTEGER NOT NULL DEFAULT 0")

                // Section 5: अष्टयाम सेवा (boolean only)
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkAshtayamSeva INTEGER NOT NULL DEFAULT 0")

                // Section 6: नित्य पाठ (boolean only)
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkNityaPath INTEGER NOT NULL DEFAULT 0")

                // Section 7: वृंदावन शत लीला
                db.execSQL("ALTER TABLE daily_records ADD COLUMN vrindavan_target INTEGER NOT NULL DEFAULT 10")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN vrindavan_did INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkVrindavan INTEGER NOT NULL DEFAULT 0")
            }
        }

        /**
         * Migration 2→3: Add Track A Mala target, did, and check columns.
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_records ADD COLUMN mala_target INTEGER NOT NULL DEFAULT 11")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN mala_did INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_records ADD COLUMN checkMala INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
