import { describe, it, expect } from 'vitest';
import { recomputeChain } from './recomputeEngine';

describe('recomputeEngine', () => {
  it('recomputes a chain of days correctly', () => {
    // Starting data representing a few days
    const records = [
      { id: '2023-10-01', did: 25000, target: 21600 },
      { id: '2023-10-02', did: 26000 }, // target should become 26000
      { id: '2023-10-03', did: 20000 }, // target should become 27000
      { id: '2023-10-04', did: 0 },     // target should become 34000
    ];

    // initialStreak is 5 from before this chain
    const updated = recomputeChain(records, 21600, 5, 1000);

    expect(updated[0].streakCount).toBe(6);
    expect(updated[0].nextDayTarget).toBe(26000);

    expect(updated[1].target).toBe(26000);
    expect(updated[1].streakCount).toBe(7);
    expect(updated[1].nextDayTarget).toBe(27000);

    expect(updated[2].target).toBe(27000);
    expect(updated[2].streakCount).toBe(0); // Missed
    expect(updated[2].nextDayTarget).toBe(34000);

    expect(updated[3].target).toBe(34000);
    expect(updated[3].streakCount).toBe(0); // Missed
    expect(updated[3].nextDayTarget).toBe(68000);
  });
});
