/**
 * MaansikSeva — Ashtayam seva checklist.
 * Each item has a checkbox + optional timestamp.
 * Notes field for "आज की अनुभूति" (today's experience journal).
 */

import { useState, useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';

const SEVA_ITEMS = [
  { id: 'mangala',   label: 'मंगला',    time: '04:00 - 05:30' },
  { id: 'shringar',  label: 'श्रृंगार',  time: '05:30 - 07:00' },
  { id: 'gval',      label: 'ग्वाल',     time: '07:00 - 08:30' },
  { id: 'rajbhog',   label: 'राजभोग',   time: '08:30 - 12:00' },
  { id: 'utthapan',  label: 'उत्थापन',  time: '12:00 - 15:00' },
  { id: 'bhog',      label: 'भोग',       time: '15:00 - 17:00' },
  { id: 'sandhya',   label: 'सन्ध्या',   time: '17:00 - 19:00' },
  { id: 'shayan',    label: 'शयन',      time: '19:00 - 21:00' },
];

export default function MaansikSeva({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const updateTodayRecord = useAppStore((s) => s.updateTodayRecord);

  const [checkedItems, setCheckedItems] = useState(new Set());
  const [notes, setNotes] = useState(todayRecord?.notes || '');

  const toggleItem = useCallback((itemId) => {
    setCheckedItems((prev) => {
      const next = new Set(prev);
      if (next.has(itemId)) {
        next.delete(itemId);
      } else {
        next.add(itemId);
      }

      // Mark section as done if any item checked
      const shouldCheck = next.size > 0;
      if (shouldCheck !== todayRecord?.checkMaansikSeva) {
        updateTodayRecord({ checkMaansikSeva: shouldCheck });
      }

      return next;
    });
  }, [todayRecord, updateTodayRecord]);

  const handleNotesBlur = useCallback(() => {
    if (notes !== todayRecord?.notes) {
      updateTodayRecord({ notes: notes.slice(0, 500) });
    }
  }, [notes, todayRecord, updateTodayRecord]);

  const completedCount = checkedItems.size;

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader
        title="मानसिक सेवा"
        subtitle={`${completedCount} / ${SEVA_ITEMS.length} सेवा`}
        onBack={onBack}
      />

      <div className="container" style={{ paddingBottom: '100px' }}>
        {/* Seva checklist */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {SEVA_ITEMS.map((item) => {
            const isChecked = checkedItems.has(item.id);
            return (
              <GlassCard
                key={item.id}
                variant="interactive"
                onClick={() => toggleItem(item.id)}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 'var(--space-3)',
                  padding: 'var(--space-3) var(--space-4)',
                  borderColor: isChecked ? 'var(--state-exceeded-border)' : undefined,
                  background: isChecked ? 'var(--state-exceeded-bg)' : undefined,
                }}
              >
                <span style={{
                  width: 24,
                  height: 24,
                  borderRadius: 'var(--radius-small)',
                  border: `2px solid ${isChecked ? 'var(--state-exceeded)' : 'rgba(255,255,255,0.20)'}`,
                  background: isChecked ? 'var(--state-exceeded)' : 'transparent',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '14px',
                  flexShrink: 0,
                  transition: 'all var(--duration-fast) var(--ease-out)',
                }}>
                  {isChecked && '✓'}
                </span>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <p className="font-ui" style={{
                    fontSize: 'var(--text-body)',
                    color: isChecked ? 'var(--state-exceeded)' : 'rgba(255,255,255, var(--opacity-secondary))',
                  }}>
                    {item.label}
                  </p>
                  <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)' }}>
                    {item.time}
                  </p>
                </div>
              </GlassCard>
            );
          })}
        </div>

        {/* Experience journal */}
        <GlassCard style={{ marginTop: 'var(--space-6)' }}>
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-3)' }}>
            आज की अनुभूति
          </p>
          <textarea
            className="glass-input font-ui"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            onBlur={handleNotesBlur}
            placeholder="आज की साधना कैसी रही..."
            maxLength={500}
            rows={4}
            style={{
              resize: 'vertical',
              minHeight: 80,
            }}
          />
          <p className="font-ui text-tertiary text-xs" style={{ marginTop: 'var(--space-1)', textAlign: 'right' }}>
            {notes.length}/500
          </p>
        </GlassCard>
      </div>
    </div>
  );
}
