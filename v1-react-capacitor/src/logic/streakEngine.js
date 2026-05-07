/**
 * Calculates the current streak based on today's target and count.
 * Formula: S(n) = D(n) >= T(n) ? S(n-1) + 1 : 0
 * 
 * @param {number} currentStreak - S(n-1)
 * @param {number} targetToday - T(n)
 * @param {number} countToday - D(n)
 * @returns {number} S(n)
 */
export function calculateStreak(currentStreak, targetToday, countToday) {
  if (countToday >= targetToday) {
    return currentStreak + 1;
  } else {
    return 0;
  }
}
