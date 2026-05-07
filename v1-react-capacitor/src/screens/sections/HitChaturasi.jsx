/**
 * HitChaturasi — Shri Hit Chaturasi Ji reading tracker.
 * Tracks daily reading of 84 pads.
 * Shows today's pad, progress bar, and checkable pad list.
 */

import { useState, useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';

// Pad titles (abbreviated — full content loaded from quotes.js in future)
const PADS = Array.from({ length: 84 }, (_, i) => ({
  id: i + 1,
  label: `पद ${i + 1}`,
}));

export default function HitChaturasi({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const updateTodayRecord = useAppStore((s) => s.updateTodayRecord);

  // Local state for which pads are read today (stored as a Set in the notes or separate field)
  // For now, simple toggle on checkChaturasi
  const [readPads, setReadPads] = useState(new Set());

  const togglePad = useCallback((padId) => {
    setReadPads((prev) => {
      const next = new Set(prev);
      if (next.has(padId)) {
        next.delete(padId);
      } else {
        next.add(padId);
      }

      // Mark section as done if any pad read
      if (next.size > 0 && !todayRecord?.checkChaturasi) {
        updateTodayRecord({ checkChaturasi: true });
      } else if (next.size === 0 && todayRecord?.checkChaturasi) {
        updateTodayRecord({ checkChaturasi: false });
      }

      return next;
    });
  }, [todayRecord, updateTodayRecord]);

  const readCount = readPads.size;
  const progressPct = Math.round((readCount / 84) * 100);

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader
        title="श्री हित चौरासी जी"
        subtitle={`${readCount} / 84 पद पढ़े`}
        onBack={onBack}
      />

      <div className="container" style={{ paddingBottom: '100px' }}>
        {/* Progress bar */}
        <GlassCard className="animate-in" style={{ marginBottom: 'var(--space-4)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 'var(--space-2)' }}>
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              आज की प्रगति
            </p>
            <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-secondary)' }}>
              {progressPct}%
            </p>
          </div>
          <div className="progress-bar-track progress-bar-track--thick">
            <div className="progress-bar-fill" style={{ width: `${progressPct}%` }} />
          </div>
        </GlassCard>

        {/* Pad list */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {PADS.map((pad) => {
            const isRead = readPads.has(pad.id);
            return (
              <GlassCard
                key={pad.id}
                variant="interactive"
                onClick={() => togglePad(pad.id)}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 'var(--space-3)',
                  padding: 'var(--space-3) var(--space-4)',
                  borderColor: isRead ? 'var(--state-exceeded-border)' : undefined,
                  background: isRead ? 'var(--state-exceeded-bg)' : undefined,
                }}
              >
                <span style={{
                  width: 24,
                  height: 24,
                  borderRadius: 'var(--radius-small)',
                  border: `2px solid ${isRead ? 'var(--state-exceeded)' : 'rgba(255,255,255,0.20)'}`,
                  background: isRead ? 'var(--state-exceeded)' : 'transparent',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '14px',
                  flexShrink: 0,
                  transition: 'all var(--duration-fast) var(--ease-out)',
                }}>
                  {isRead && '✓'}
                </span>
                <p className="font-ui" style={{
                  fontSize: 'var(--text-body)',
                  color: isRead ? 'var(--state-exceeded)' : 'rgba(255,255,255, var(--opacity-secondary))',
                }}>
                  {pad.label}
                </p>
              </GlassCard>
            );
          })}
        </div>
      </div>
    </div>
  );
}
