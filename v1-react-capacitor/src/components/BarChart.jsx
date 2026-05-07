/**
 * BarChart — Vertical bar chart for history visualization.
 * Shows daily jap counts with target line overlay.
 * Uses only CSS variables from tokens.css.
 */

import { useMemo } from 'react';
import { formatNumber } from '../logic/numberFormat';

/**
 * @param {{ data: Array<{ date: string, did: number, target: number }>, days?: number }} props
 */
export default function BarChart({ data = [], days = 7 }) {
  const sliced = useMemo(() => data.slice(-days), [data, days]);

  const maxVal = useMemo(() => {
    if (sliced.length === 0) return 1;
    return Math.max(...sliced.map((d) => Math.max(d.did || 0, d.target || 0)), 1);
  }, [sliced]);

  if (sliced.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: 'var(--space-6)' }}>
        <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)' }}>
          अभी कोई डेटा नहीं
        </p>
      </div>
    );
  }

  return (
    <div style={{ width: '100%' }}>
      {/* Chart area */}
      <div style={{
        display: 'flex',
        alignItems: 'flex-end',
        gap: '4px',
        height: '160px',
        padding: '0 var(--space-1)',
      }}>
        {sliced.map((entry, i) => {
          const barH = maxVal > 0 ? (entry.did / maxVal) * 100 : 0;
          const targetH = maxVal > 0 ? (entry.target / maxVal) * 100 : 0;
          const met = entry.did >= entry.target;
          const dateLabel = entry.date ? entry.date.slice(-2) : '';

          return (
            <div key={entry.date || i} style={{
              flex: 1,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              height: '100%',
              position: 'relative',
            }}>
              {/* Target marker line */}
              <div style={{
                position: 'absolute',
                bottom: `${targetH}%`,
                left: 0,
                right: 0,
                height: '2px',
                background: 'var(--accent-secondary)',
                opacity: 0.5,
                borderRadius: '1px',
              }} />

              {/* Bar */}
              <div style={{
                width: '100%',
                maxWidth: '32px',
                marginTop: 'auto',
                height: `${Math.max(barH, 2)}%`,
                borderRadius: 'var(--radius-small) var(--radius-small) 0 0',
                background: met
                  ? 'var(--gradient-accent)'
                  : entry.did > 0
                    ? 'var(--state-partial)'
                    : 'var(--state-missed)',
                transition: 'height var(--duration-normal) var(--ease-out)',
                minHeight: '4px',
              }} />

              {/* Date label */}
              <span className="font-numbers text-tertiary" style={{
                fontSize: '10px',
                marginTop: 'var(--space-1)',
                lineHeight: 1,
              }}>
                {dateLabel}
              </span>
            </div>
          );
        })}
      </div>

      {/* Legend */}
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        gap: 'var(--space-4)',
        marginTop: 'var(--space-3)',
      }}>
        <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', display: 'flex', alignItems: 'center', gap: '4px' }}>
          <span style={{ width: 8, height: 8, borderRadius: 2, background: 'var(--gradient-accent)', display: 'inline-block' }} />
          पूर्ण
        </span>
        <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', display: 'flex', alignItems: 'center', gap: '4px' }}>
          <span style={{ width: 8, height: 8, borderRadius: 2, background: 'var(--state-partial)', display: 'inline-block' }} />
          आंशिक
        </span>
        <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', display: 'flex', alignItems: 'center', gap: '4px' }}>
          <span style={{ width: 8, height: 2, background: 'var(--accent-secondary)', display: 'inline-block', opacity: 0.5 }} />
          लक्ष्य
        </span>
      </div>
    </div>
  );
}
