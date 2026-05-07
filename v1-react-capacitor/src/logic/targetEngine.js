/**
 * Calculates the next day's target based on today's performance.
 * Formula: T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
 * 
 * @param {number} targetToday - T(n)
 * @param {number} countToday - D(n)
 * @param {number} [increment=1000] - The daily increment amount
 * @returns {number} T(n+1)
 */
export function calculateNextTarget(targetToday, countToday, increment = 1000) {
  if (countToday >= targetToday) {
    return countToday + increment;
  } else {
    return targetToday + (targetToday - countToday);
  }
}
