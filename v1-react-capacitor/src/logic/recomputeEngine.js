import { calculateNextTarget } from './targetEngine';
import { calculateStreak } from './streakEngine';

/**
 * Recomputes targets and streaks for a chain of daily records.
 * 
 * @param {Array} records - Array of DailyRecord objects, sorted chronologically.
 *                          The first record should be the one that was edited.
 * @param {number} initialTarget - The initial target for the first record if it doesn't have one
 * @param {number} initialStreak - The streak from the day BEFORE the first record, defaults to 0.
 * @param {number} increment - The target increment (from settings).
 * @returns {Array} - The modified array of records with updated targets and streaks.
 */
export function recomputeChain(records, initialTarget = 21600, initialStreak = 0, increment = 1000) {
  if (!records || records.length === 0) return [];

  const updatedRecords = [...records];
  
  // Initialize the first record's target if missing
  if (updatedRecords[0].target === undefined || updatedRecords[0].target === null) {
    updatedRecords[0].target = initialTarget;
  }

  let prevStreak = initialStreak;

  for (let i = 0; i < updatedRecords.length; i++) {
    const record = updatedRecords[i];
    const d = record.did || 0;
    const t = record.target;
    
    // Compute current day's streak and next day target
    record.streakCount = calculateStreak(prevStreak, t, d);
    record.nextDayTarget = calculateNextTarget(t, d, increment);

    // Update state for next iteration
    prevStreak = record.streakCount;
    
    // Apply target to the next record in the chain if there is one
    if (i + 1 < updatedRecords.length) {
      updatedRecords[i + 1].target = record.nextDayTarget;
    }
  }

  return updatedRecords;
}
