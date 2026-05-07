import { describe, it, expect } from 'vitest';
import { calculateNextTarget } from './targetEngine';

describe('targetEngine', () => {
  it('case 1: exceeded target', () => {
    // T(n) = 21,600, D(n) = 25,000 -> T(n+1) = 26,000
    expect(calculateNextTarget(21600, 25000)).toBe(26000);
  });

  it('case 2: exact target met', () => {
    // T(n) = 26,000, D(n) = 26,000 -> T(n+1) = 27,000
    expect(calculateNextTarget(26000, 26000)).toBe(27000);
  });

  it('case 3: deficit 7k', () => {
    // T(n) = 27,000, D(n) = 20,000 -> T(n+1) = 34,000
    expect(calculateNextTarget(27000, 20000)).toBe(34000);
  });

  it('case 4: missed day completely (0 count)', () => {
    // T(n) = 34,000, D(n) = 0 -> T(n+1) = 68,000
    expect(calculateNextTarget(34000, 0)).toBe(68000);
  });

  it('case 5: missed day completely again (0 count)', () => {
    // T(n) = 68,000, D(n) = 0 -> T(n+1) = 1,36,000
    expect(calculateNextTarget(68000, 0)).toBe(136000);
  });

  it('case 6: exceeded after large missed target', () => {
    // T(n) = 1,36,000, D(n) = 1,50,000 -> T(n+1) = 1,51,000
    expect(calculateNextTarget(136000, 150000)).toBe(151000);
  });

  it('supports custom increment', () => {
    // With 500 increment: T(n) = 20,000, D(n) = 25,000 -> T(n+1) = 25,500
    expect(calculateNextTarget(20000, 25000, 500)).toBe(25500);
  });
});
