/**
 * HeatMap — GitHub-style contribution grid for sadhana history.
 * Shows past N weeks of daily completion intensity.
 * Uses state tokens for color coding.
 */

import { useMemo } from 'react';

/**
 * @param {{ data: Array<{ date: string, did: number, target: number }>, weeks?: number }} props
 */
export default function HeatMap({ data = [], weeks = 12 }) {
  // Build grid: 7 rows (Sun–Sat) × N columns (weeks)
  const grid = useMemo(() => {
    const totalDays = weeks * 7;
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // Create lookup map from data
    const lookup = {};
    data.forEach((d) => {
      lookup[d.date] = d;
    });

    // Build cells from (today - totalDays) to today
    const cells = [];
    for (let i = totalDays - 1; i >= 0; i--) {
      const d = new Date(today);
      d.setDate(d.getDate() - i);
      const dateStr = d.toISOString().split('T')[0];
      const entry = lookup[dateStr];

      let intensity = 0; // 0 = no data, 1 = missed, 2 = partial, 3 = met, 4 = exceeded
      if (entry) {
        if (entry.did <= 0) intensity = 1;
        else if (entry.did < entry.target) intensity = 2;
        else if (entry.did >= entry.target * 1.2) intensity = 4;
        else intensity = 3;
      }

      cells.push({ date: dateStr, intensity, dayOfWeek: d.getDay() });
    }

    return cells;
  }, [data, weeks]);

  const intensityColors = {
    0: 'var(--state-future)',        // no data — faint
    1: 'var(--state-missed)',        // missed
    2: 'var(--state-partial)',       // partial
    3: 'var(--state-exceeded)',      // met
    4: 'var(--accent-primary)',      // exceeded significantly
  };

  const dayLabels = ['र', 'सो', 'मं', 'बु', 'गु', 'शु', 'श'];

  return (
    <div style={{ width: '100%' }}>
      <div style={{ display: 'flex', gap: '2px' }}>
        {/* Day labels column */}
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '2px',
          marginRight: '4px',
          justifyContent: 'flex-start',
        }}>
          {dayLabels.map((label, i) => (
            <span key={i} className="font-ui text-tertiary" style={{
              fontSize: '9px',
              height: '14px',
              lineHeight: '14px',
              textAlign: 'right',
              width: '14px',
            }}>
              {i % 2 === 0 ? label : ''}
            </span>
          ))}
        </div>

        {/* Grid cells */}
        <div style={{
          display: 'grid',
          gridTemplateRows: 'repeat(7, 14px)',
          gridAutoFlow: 'column',
          gap: '2px',
          flex: 1,
          overflowX: 'auto',
        }}>
          {grid.map((cell) => (
            <div
              key={cell.date}
              title={`${cell.date}: ${cell.intensity > 0 ? ['', 'छूटा', 'आंशिक', 'पूर्ण', 'उत्कृष्ट'][cell.intensity] : 'कोई डेटा नहीं'}`}
              style={{
                width: '14px',
                height: '14px',
                borderRadius: '3px',
                background: intensityColors[cell.intensity],
                transition: 'background var(--duration-fast) var(--ease-out)',
              }}
            />
          ))}
        </div>
      </div>

      {/* Legend */}
      <div style={{
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        gap: '4px',
        marginTop: 'var(--space-3)',
      }}>
        <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>कम</span>
        {[0, 1, 2, 3, 4].map((level) => (
          <div key={level} style={{
            width: '12px',
            height: '12px',
            borderRadius: '2px',
            background: intensityColors[level],
          }} />
        ))}
        <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>अधिक</span>
      </div>
    </div>
  );
}
