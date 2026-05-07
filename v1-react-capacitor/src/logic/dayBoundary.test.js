import { describe, it, expect } from 'vitest';
import { getSpiritualDate } from './dayBoundary';

describe('dayBoundary', () => {
  it('treats 2:30 AM as the previous day', () => {
    // Target date: Jan 2, 2026, 02:30:00 (local time)
    const testDate = new Date(2026, 0, 2, 2, 30, 0); // Month is 0-indexed
    const result = getSpiritualDate(testDate, 3);
    
    // Should be Jan 1
    expect(result).toBe('2026-01-01');
  });

  it('treats 3:00 AM as the current day', () => {
    const testDate = new Date(2026, 0, 2, 3, 0, 0);
    const result = getSpiritualDate(testDate, 3);
    
    // Should be Jan 2
    expect(result).toBe('2026-01-02');
  });

  it('treats 11:59 PM as the current day', () => {
    const testDate = new Date(2026, 0, 2, 23, 59, 59);
    const result = getSpiritualDate(testDate, 3);
    
    // Should be Jan 2
    expect(result).toBe('2026-01-02');
  });

  it('handles custom boundary hour (e.g., 5 AM)', () => {
    const testDate = new Date(2026, 0, 2, 4, 59, 59);
    const result = getSpiritualDate(testDate, 5);
    
    // Should be Jan 1
    expect(result).toBe('2026-01-01');
  });
  
  it('handles month boundary correctly', () => {
    const testDate = new Date(2026, 2, 1, 1, 0, 0); // March 1, 1 AM
    const result = getSpiritualDate(testDate, 3);
    
    // Should be Feb 28, 2026
    expect(result).toBe('2026-02-28');
  });
  
  it('handles leap year correctly', () => {
    const testDate = new Date(2024, 2, 1, 1, 0, 0); // March 1, 2024, 1 AM
    const result = getSpiritualDate(testDate, 3);
    
    // Should be Feb 29, 2024
    expect(result).toBe('2024-02-29');
  });
});
