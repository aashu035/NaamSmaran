package com.radhavallabh.naamsmaran.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * TargetEngineTest — Verification of THE FORMULA.
 *
 * T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
 *
 * Canonical verification table from agents/02-formula.md (INCREMENT = 5,000):
 * | Day | Target    | Did       | Case     | Next Target |
 * |-----|-----------|-----------|----------|-------------|
 * | 1   | 21,600    | 25,000    | exceeded | 30,000      |
 * | 2   | 30,000    | 30,000    | exact    | 35,000      |
 * | 3   | 35,000    | 20,000    | deficit  | 50,000      |
 * | 4   | 50,000    | 0         | missed   | 1,00,000    |
 * | 5   | 1,00,000  | 0         | missed   | 2,00,000    |
 * | 6   | 2,00,000  | 2,10,000  | exceeded | 2,15,000    |
 *
 * जय श्री हित हरिवंश महाप्रभु 🙏
 */
class TargetEngineTest {

    // ═══════════════════════════════════════════════════════════
    // Canonical Verification Table — agents/02-formula.md
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `day 1 - exceeded - 21600 target 25000 done = 30000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 25_000L
        )
        assertEquals(30_000L, result)
    }

    @Test
    fun `day 2 - exact - 30000 target 30000 done = 35000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 30_000L,
            countToday = 30_000L
        )
        assertEquals(35_000L, result)
    }

    @Test
    fun `day 3 - deficit - 35000 target 20000 done = 50000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 35_000L,
            countToday = 20_000L
        )
        assertEquals(50_000L, result)
    }

    @Test
    fun `day 4 - missed - 50000 target 0 done = 100000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 50_000L,
            countToday = 0L
        )
        assertEquals(100_000L, result)
    }

    @Test
    fun `day 5 - missed - 100000 target 0 done = 200000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 100_000L,
            countToday = 0L
        )
        assertEquals(200_000L, result)
    }

    @Test
    fun `day 6 - exceeded - 200000 target 210000 done = 215000 next`() {
        val result = TargetEngine.calculateNextTarget(
            targetToday = 200_000L,
            countToday = 210_000L
        )
        assertEquals(215_000L, result)
    }

    // ═══════════════════════════════════════════════════════════
    // Edge Cases
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `edge - D equals T minus 1 - one short triggers penalty`() {
        // T=21600, D=21599 → deficit: 21600 + (21600 - 21599) = 21601
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 21_599L
        )
        assertEquals(21_601L, result)
    }

    @Test
    fun `edge - D equals T exactly - met triggers reward`() {
        // T=21600, D=21600 → exact: 21600 + 5000 = 26600
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 21_600L
        )
        assertEquals(26_600L, result)
    }

    @Test
    fun `edge - D equals T plus 1 - exceeded by 1`() {
        // T=21600, D=21601 → exceeded: 21601 + 5000 = 26601
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 21_601L
        )
        assertEquals(26_601L, result)
    }

    @Test
    fun `edge - D is zero - full miss doubles target`() {
        // T=21600, D=0 → missed: 21600 + 21600 = 43200
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 0L
        )
        assertEquals(43_200L, result)
    }

    @Test
    fun `custom increment - uses provided increment not default`() {
        // T=21600, D=25000, increment=1000 → 25000 + 1000 = 26000
        val result = TargetEngine.calculateNextTarget(
            targetToday = 21_600L,
            countToday = 25_000L,
            increment = 1_000
        )
        assertEquals(26_000L, result)
    }

    // ═══════════════════════════════════════════════════════════
    // StreakEngine — Smoke test
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `streak increments when target met`() {
        assertEquals(1, StreakEngine.calculateStreak(0, 21_600L, 21_600L))
        assertEquals(5, StreakEngine.calculateStreak(4, 30_000L, 35_000L))
    }

    @Test
    fun `streak resets to zero when target missed`() {
        assertEquals(0, StreakEngine.calculateStreak(10, 21_600L, 21_599L))
        assertEquals(0, StreakEngine.calculateStreak(5, 50_000L, 0L))
    }
}
