import { describe, it, expect } from 'vitest';
import { calculateStreak } from './streakEngine';

describe('streakEngine', () => {
  it('increments streak if target is met exactly', () => {
    // S(n-1) = 5, T(n) = 26,000, D(n) = 26,000 -> S(n) = 6
    expect(calculateStreak(5, 26000, 26000)).toBe(6);
  });

  it('increments streak if target is exceeded', () => {
    // S(n-1) = 10, T(n) = 21,600, D(n) = 25,000 -> S(n) = 11
    expect(calculateStreak(10, 21600, 25000)).toBe(11);
  });

  it('resets streak to 0 if target is missed', () => {
    // S(n-1) = 5, T(n) = 27,000, D(n) = 20,000 -> S(n) = 0
    expect(calculateStreak(5, 27000, 20000)).toBe(0);
  });

  it('resets streak to 0 if count is 0', () => {
    // S(n-1) = 3, T(n) = 34,000, D(n) = 0 -> S(n) = 0
    expect(calculateStreak(3, 34000, 0)).toBe(0);
  });
  
  it('starts streak at 1 on first successful day', () => {
    // S(n-1) = 0, T(n) = 21,600, D(n) = 22,000 -> S(n) = 1
    expect(calculateStreak(0, 21600, 22000)).toBe(1);
  });
});
