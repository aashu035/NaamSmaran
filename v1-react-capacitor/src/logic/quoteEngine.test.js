import { describe, it, expect, vi } from 'vitest';
import { getRandomQuote } from './quoteEngine';

describe('quoteEngine', () => {
  const mockQuotes = [
    { id: 'q1', lastShown: null },
    { id: 'q2', lastShown: new Date(Date.now() - 8 * 24 * 60 * 60 * 1000).toISOString() }, // 8 days ago
    { id: 'q3', lastShown: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString() }, // 2 days ago
  ];

  it('selects a quote that has not been shown in the last 7 days', () => {
    // Should pick q1 or q2, never q3
    const selected = getRandomQuote(mockQuotes);
    expect(['q1', 'q2']).toContain(selected.id);
  });

  it('falls back to any quote if all quotes were shown recently', () => {
    const allRecent = [
      { id: 'q1', lastShown: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString() },
      { id: 'q2', lastShown: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString() }
    ];
    
    const selected = getRandomQuote(allRecent);
    expect(['q1', 'q2']).toContain(selected.id);
  });

  it('returns null if quote list is empty', () => {
    expect(getRandomQuote([])).toBeNull();
  });
});
