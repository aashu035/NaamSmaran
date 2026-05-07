/**
 * Calendar — Full monthly calendar view with daily sadhana status.
 * Each day cell shows met/missed/partial state.
 * Tapping a day opens that day's detail in History.
 */

import { useState, useEffect, useMemo } from 'react';
import { getAllRecords } from '../store/db';
import { formatNumber } from '../logic/numberFormat';
import SectionHeader from '../components/SectionHeader';
import GlassCard from '../components/GlassCard';

const MONTH_NAMES = [
  'जनवरी', 'फरवरी', 'मार्च', 'अप्रैल', 'मई', 'जून',
  'जुलाई', 'अगस्त', 'सितम्बर', 'अक्तूबर', 'नवम्बर', 'दिसम्बर'
];

const DAY_HEADERS = ['र', 'सो', 'मं', 'बु', 'गु', 'शु', 'श'];

export default function Calendar({ onBack, onSelectDay }) {
  const [records, setRecords] = useState([]);
  const [viewDate, setViewDate] = useState(new Date());

  useEffect(() => {
    getAllRecords('DailyRecord').then((all) => {
      setRecords(all || []);
    });
  }, []);

  const year = viewDate.getFullYear();
  const month = viewDate.getMonth();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const firstDayOfWeek = new Date(year, month, 1).getDay(); // 0=Sun

  const todayStr = new Date().toISOString().split('T')[0];

  // Build lookup map
  const recordMap = useMemo(() => {
    const map = {};
    records.forEach((r) => { map[r.date || r.id] = r; });
    return map;
  }, [records]);

  // Month stats
  const monthStats = useMemo(() => {
    let met = 0, total = 0, totalDid = 0;
    for (let d = 1; d <= daysInMonth; d++) {
      const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      const rec = recordMap[dateStr];
      if (rec) {
        total++;
        totalDid += rec.did || 0;
        if (rec.did >= rec.target) met++;
      }
    }
    return { met, total, totalDid };
  }, [recordMap, year, month, daysInMonth]);

  const prevMonth = () => {
    const d = new Date(year, month - 1, 1);
    setViewDate(d);
  };

  const nextMonth = () => {
    const d = new Date(year, month + 1, 1);
    setViewDate(d);
  };

  // Build calendar grid cells
  const cells = [];
  // Empty cells for days before the 1st
  for (let i = 0; i < firstDayOfWeek; i++) {
    cells.push({ key: `empty-${i}`, empty: true });
  }
  // Actual day cells
  for (let d = 1; d <= daysInMonth; d++) {
    const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
    const rec = recordMap[dateStr];
    const isToday = dateStr === todayStr;
    const isFuture = dateStr > todayStr;

    let status = 'none'; // none | missed | partial | met | future
    if (isFuture) {
      status = 'future';
    } else if (rec) {
      if (rec.did >= rec.target) status = 'met';
      else if (rec.did > 0) status = 'partial';
      else status = 'missed';
    }

    cells.push({ key: dateStr, day: d, dateStr, status, isToday, rec });
  }

  const statusStyles = {
    met:     { background: 'var(--state-exceeded-bg)', border: '1px solid var(--state-exceeded-border)', color: 'var(--state-exceeded)' },
    partial: { background: 'var(--state-partial-bg)', border: '1px solid rgba(245, 215, 110, 0.25)', color: 'var(--state-partial)' },
    missed:  { background: 'var(--state-missed-bg)', border: '1px solid transparent', color: 'rgba(255,255,255,0.38)' },
    future:  { background: 'transparent', border: '1px solid transparent', color: 'rgba(255,255,255,0.15)' },
    none:    { background: 'transparent', border: '1px solid transparent', color: 'rgba(255,255,255,0.38)' },
  };

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="कैलेंडर" subtitle={`${MONTH_NAMES[month]} ${year}`} onBack={onBack} />
      <div className="container" style={{ paddingBottom: '100px' }}>

        {/* Month navigation */}
        <GlassCard className="animate-in">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 'var(--space-4)' }}>
            <button className="btn btn--ghost tappable" onClick={prevMonth} style={{ fontSize: 'var(--text-body)' }}>‹</button>
            <p className="font-ui text-primary font-bold" style={{ fontSize: 'var(--text-body)' }}>
              {MONTH_NAMES[month]} {year}
            </p>
            <button className="btn btn--ghost tappable" onClick={nextMonth} style={{ fontSize: 'var(--text-body)' }}>›</button>
          </div>

          {/* Day headers */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: '4px', marginBottom: '4px' }}>
            {DAY_HEADERS.map((h) => (
              <div key={h} style={{ textAlign: 'center' }}>
                <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>{h}</span>
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: '4px' }}>
            {cells.map((cell) => {
              if (cell.empty) {
                return <div key={cell.key} />;
              }

              const style = statusStyles[cell.status] || statusStyles.none;
              return (
                <button
                  key={cell.key}
                  className="tappable"
                  onClick={() => cell.rec && onSelectDay?.(cell.dateStr)}
                  style={{
                    width: '100%',
                    aspectRatio: '1',
                    borderRadius: 'var(--radius-small)',
                    ...style,
                    outline: cell.isToday ? '2px solid var(--accent-primary)' : 'none',
                    outlineOffset: '-1px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: 'var(--text-label)',
                    fontFamily: 'var(--font-numbers)',
                    cursor: cell.rec ? 'pointer' : 'default',
                    padding: 0,
                  }}
                >
                  {cell.day}
                </button>
              );
            })}
          </div>
        </GlassCard>

        {/* Month summary */}
        <GlassCard className="animate-stagger" style={{ '--stagger': 1, marginTop: 'var(--space-4)' }}>
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-3)' }}>
            मासिक सारांश
          </p>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 'var(--space-3)', textAlign: 'center' }}>
            <div>
              <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>
                {monthStats.met}
              </p>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>लक्ष्य पूर्ण</p>
            </div>
            <div>
              <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>
                {monthStats.total}
              </p>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>कुल दिन</p>
            </div>
            <div>
              <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>
                {formatNumber(monthStats.totalDid)}
              </p>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>कुल जप</p>
            </div>
          </div>
        </GlassCard>

        {/* Legend */}
        <div className="animate-stagger" style={{ '--stagger': 2, display: 'flex', justifyContent: 'center', gap: 'var(--space-4)', marginTop: 'var(--space-4)' }}>
          {[
            { label: 'पूर्ण', color: 'var(--state-exceeded)' },
            { label: 'आंशिक', color: 'var(--state-partial)' },
            { label: 'छूटा', color: 'rgba(255,255,255,0.20)' },
          ].map((item) => (
            <span key={item.label} className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', display: 'flex', alignItems: 'center', gap: '4px' }}>
              <span style={{ width: 10, height: 10, borderRadius: 3, background: item.color, display: 'inline-block' }} />
              {item.label}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}
