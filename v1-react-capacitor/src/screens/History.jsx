/**
 * History — Past days list with edit capability.
 * Shows recent records with bar chart and heatmap visualizations.
 * Supports editing past D(n) values which triggers full recomputation.
 */

import { useState, useEffect, useCallback } from 'react';
import { getAllRecords, putRecord } from '../store/db';
import { useAppStore } from '../store/useAppStore';
import { formatNumber } from '../logic/numberFormat';
import { calculateNextTarget } from '../logic/targetEngine';
import { calculateStreak } from '../logic/streakEngine';
import SectionHeader from '../components/SectionHeader';
import GlassCard from '../components/GlassCard';
import BarChart from '../components/BarChart';
import HeatMap from '../components/HeatMap';

export default function History({ onBack, initialDate }) {
  const [records, setRecords] = useState([]);
  const [editingDate, setEditingDate] = useState(null);
  const [editValue, setEditValue] = useState('');
  const [viewMode, setViewMode] = useState('list'); // list | chart | heatmap
  const settings = useAppStore((s) => s.settings);
  const loadTodayRecord = useAppStore((s) => s.loadTodayRecord);

  const loadRecords = useCallback(async () => {
    const all = await getAllRecords('DailyRecord');
    all.sort((a, b) => b.id.localeCompare(a.id)); // newest first
    setRecords(all || []);
  }, []);

  useEffect(() => {
    loadRecords();
  }, [loadRecords]);

  // Past day edit → full recomputation (AGENTS.md §3)
  const handleEditSave = useCallback(async () => {
    if (!editingDate || !settings) return;
    const newDid = parseInt(editValue, 10);
    if (isNaN(newDid) || newDid < 0) return;

    // 1. Get all records sorted chronologically
    const all = await getAllRecords('DailyRecord');
    all.sort((a, b) => a.id.localeCompare(b.id));

    // 2. Find the edited record index
    const editIdx = all.findIndex((r) => r.id === editingDate);
    if (editIdx < 0) return;

    // 3. Update the edited record
    all[editIdx].did = newDid;
    all[editIdx].checkNaamJap = newDid > 0;
    all[editIdx].updatedAt = Date.now();

    // 4. Recompute chain from editIdx forward
    for (let i = editIdx; i < all.length; i++) {
      const rec = all[i];
      const prev = i > 0 ? all[i - 1] : null;

      // Recalculate target for this day (except first ever record)
      if (i === editIdx && prev) {
        // Target for edited day comes from previous day's computation
        rec.target = calculateNextTarget(prev.target, prev.did, settings.targetIncrement);
      }

      // Recompute streak
      const prevStreak = prev ? prev.streakCount : 0;
      rec.streakCount = calculateStreak(prevStreak, rec.target, rec.did);

      // Recompute next day target
      rec.nextDayTarget = calculateNextTarget(rec.target, rec.did, settings.targetIncrement);

      // If there's a next record, update its target
      if (i + 1 < all.length) {
        all[i + 1].target = rec.nextDayTarget;
      }

      await putRecord('DailyRecord', rec);
    }

    // 5. Reload
    await loadRecords();
    await loadTodayRecord();
    setEditingDate(null);
    setEditValue('');
  }, [editingDate, editValue, settings, loadRecords, loadTodayRecord]);

  const todayStr = new Date().toISOString().split('T')[0];

  // View mode pills
  const modes = [
    { id: 'list', label: 'सूची' },
    { id: 'chart', label: 'चार्ट' },
    { id: 'heatmap', label: 'हीटमैप' },
  ];

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="इतिहास" subtitle={`${records.length} दिन`} onBack={onBack} />
      <div className="container" style={{ paddingBottom: '100px' }}>

        {/* View mode pills */}
        <div style={{
          display: 'flex',
          gap: 'var(--space-2)',
          marginBottom: 'var(--space-4)',
          justifyContent: 'center',
        }}>
          {modes.map((mode) => (
            <button
              key={mode.id}
              className={viewMode === mode.id ? 'pill pill--active' : 'pill pill--inactive'}
              onClick={() => setViewMode(mode.id)}
            >
              {mode.label}
            </button>
          ))}
        </div>

        {/* Chart view */}
        {viewMode === 'chart' && (
          <GlassCard className="animate-in">
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-3)' }}>
              पिछले 14 दिन
            </p>
            <BarChart
              data={[...records].reverse().map((r) => ({ date: r.date || r.id, did: r.did || 0, target: r.target || 0 }))}
              days={14}
            />
          </GlassCard>
        )}

        {/* Heatmap view */}
        {viewMode === 'heatmap' && (
          <GlassCard className="animate-in">
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-3)' }}>
              12 सप्ताह का अवलोकन
            </p>
            <HeatMap
              data={records.map((r) => ({ date: r.date || r.id, did: r.did || 0, target: r.target || 0 }))}
              weeks={12}
            />
          </GlassCard>
        )}

        {/* List view */}
        {viewMode === 'list' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
            {records.length === 0 && (
              <GlassCard style={{ textAlign: 'center' }}>
                <p className="font-ui text-tertiary">अभी कोई इतिहास नहीं</p>
              </GlassCard>
            )}
            {records.map((rec, i) => {
              const dateStr = rec.date || rec.id;
              const met = rec.did >= rec.target;
              const isToday = dateStr === todayStr;
              const isEditing = editingDate === dateStr;
              const progress = rec.target > 0 ? Math.min(1, rec.did / rec.target) : 0;

              return (
                <GlassCard
                  key={dateStr}
                  className={i < 5 ? 'animate-stagger' : ''}
                  style={{ '--stagger': i }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <p className="font-ui text-primary" style={{ fontSize: 'var(--text-secondary)' }}>
                        {rec.dayOfWeek} {isToday && <span className="text-accent" style={{ fontSize: 'var(--text-label)' }}>(आज)</span>}
                      </p>
                      <p className="font-numbers text-tertiary" style={{ fontSize: 'var(--text-tertiary)' }}>
                        {dateStr}
                      </p>
                    </div>
                    <div style={{ textAlign: 'right' }}>
                      <p className={`font-numbers font-bold ${met ? 'state-exceeded' : ''}`} style={{ fontSize: 'var(--text-body)' }}>
                        {formatNumber(rec.did)}
                      </p>
                      <p className="font-numbers text-tertiary" style={{ fontSize: 'var(--text-label)' }}>
                        / {formatNumber(rec.target)}
                      </p>
                    </div>
                  </div>

                  {/* Progress bar */}
                  <div style={{
                    width: '100%',
                    height: 'var(--bar-height)',
                    background: 'var(--bar-track)',
                    borderRadius: 'var(--bar-radius)',
                    marginTop: 'var(--space-2)',
                    overflow: 'hidden',
                  }}>
                    <div style={{
                      width: `${Math.round(progress * 100)}%`,
                      height: '100%',
                      background: met ? 'var(--gradient-accent)' : 'var(--state-partial)',
                      borderRadius: 'var(--bar-radius)',
                      transition: 'width var(--duration-normal) var(--ease-out)',
                    }} />
                  </div>

                  {/* Streak + edit row */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 'var(--space-2)' }}>
                    <span className="font-numbers text-tertiary" style={{ fontSize: 'var(--text-label)' }}>
                      {rec.streakCount > 0 ? `🔥 ${rec.streakCount} दिन` : ''}
                    </span>

                    {!isToday && !isEditing && (
                      <button
                        className="btn btn--ghost tappable"
                        onClick={() => { setEditingDate(dateStr); setEditValue(String(rec.did)); }}
                        style={{ fontSize: 'var(--text-label)', padding: 'var(--space-1) var(--space-2)' }}
                      >
                        ✏️ संपादन
                      </button>
                    )}
                  </div>

                  {/* Inline edit */}
                  {isEditing && (
                    <div style={{ display: 'flex', gap: 'var(--space-2)', marginTop: 'var(--space-3)', alignItems: 'center' }}>
                      <input
                        type="number"
                        className="glass-input font-numbers"
                        value={editValue}
                        onChange={(e) => setEditValue(e.target.value)}
                        min="0"
                        inputMode="numeric"
                        style={{ flex: 1 }}
                        autoFocus
                      />
                      <button className="btn btn--primary tappable" onClick={handleEditSave} style={{ fontSize: 'var(--text-tertiary)' }}>
                        सहेजें
                      </button>
                      <button className="btn btn--ghost tappable" onClick={() => setEditingDate(null)} style={{ fontSize: 'var(--text-tertiary)' }}>
                        रद्द
                      </button>
                    </div>
                  )}
                </GlassCard>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
