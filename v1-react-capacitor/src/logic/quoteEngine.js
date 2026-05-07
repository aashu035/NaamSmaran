/**
 * Returns a random quote from the provided list.
 * Prioritizes quotes that have not been shown in the last 7 days.
 * 
 * @param {Array} quotes - Array of quote objects
 * @returns {Object|null} A random quote or null if empty
 */
export function getRandomQuote(quotes) {
  if (!quotes || quotes.length === 0) {
    return null;
  }

  const now = Date.now();
  const sevenDaysInMs = 7 * 24 * 60 * 60 * 1000;

  // Filter quotes not shown in the last 7 days
  const eligibleQuotes = quotes.filter(quote => {
    if (!quote.lastShown) return true;
    const lastShownTime = new Date(quote.lastShown).getTime();
    return (now - lastShownTime) >= sevenDaysInMs;
  });

  // If all quotes were shown recently, fall back to the full list
  const pool = eligibleQuotes.length > 0 ? eligibleQuotes : quotes;

  // Pick a random quote from the pool
  const randomIndex = Math.floor(Math.random() * pool.length);
  return pool[randomIndex];
}
