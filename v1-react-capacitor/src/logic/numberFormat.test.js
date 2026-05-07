import { describe, it, expect } from 'vitest';
import { formatNumber } from './numberFormat';

describe('numberFormat', () => {
  it('formats numbers using the Indian numbering system', () => {
    expect(formatNumber(1000)).toBe('1,000');
    expect(formatNumber(10000)).toBe('10,000');
    expect(formatNumber(100000)).toBe('1,00,000');
    expect(formatNumber(999999)).toBe('9,99,999');
    expect(formatNumber(10000000)).toBe('1,00,00,000');
  });

  it('handles 0 correctly', () => {
    expect(formatNumber(0)).toBe('0');
  });

  it('handles strings that are numbers', () => {
    expect(formatNumber('100000')).toBe('1,00,000');
  });

  it('handles invalid inputs gracefully', () => {
    expect(formatNumber(null)).toBe('0');
    expect(formatNumber(undefined)).toBe('0');
    expect(formatNumber(NaN)).toBe('0');
  });
});
