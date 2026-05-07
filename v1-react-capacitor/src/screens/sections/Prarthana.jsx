/**
 * Prarthana — Daily prayer toggle section.
 * Simple done/not-done toggle + 7-day log.
 */

import { useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';

export default function Prarthana({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const updateTodayRecord = useAppStore((s) => s.updateTodayRecord);

  const isDone = todayRecord?.checkPrarthana || false;

  const togglePrarthana = useCallback(() => {
    updateTodayRecord({ checkPrarthana: !isDone });
  }, [isDone, updateTodayRecord]);

  // Mock 7-day log (will be populated from IndexedDB in full implementation)
  const weekDays = ['सोम', 'मंगल', 'बुध', 'गुरु', 'शुक्र', 'शनि', 'रवि'];
  const weekLog = [true, true, false, true, true, true, isDone]; // last = today

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader
        title="प्रार्थना"
        subtitle={isDone ? '✅ आज की प्रार्थना सम्पन्न' : 'आज प्रार्थना शेष'}
        onBack={onBack}
      />

      <div className="container" style={{ paddingBottom: '100px' }}>
        {/* Toggle button */}
        <GlassCard className="animate-in" style={{ textAlign: 'center' }}>
          <p className="font-deco text-accent" style={{
            fontSize: 'var(--text-medium)',
            lineHeight: 'var(--leading-relaxed)',
            marginBottom: 'var(--space-6)',
          }}>
            "हे प्रिया प्रियतम, कृपा करो"
          </p>

          <button
            className={`btn ${isDone ? 'btn--ghost' : 'btn--primary'} tappable`}
            onClick={togglePrarthana}
            style={{
              width: '100%',
              padding: 'var(--space-4)',
              fontSize: 'var(--text-body)',
            }}
          >
            {isDone ? 'पूर्ण ✅ — टैप करें रद्द करने के लिए' : '🙏 प्रार्थना सम्पन्न'}
          </button>
        </GlassCard>

        {/* 7-day log */}
        <GlassCard className="animate-stagger" style={{ '--stagger': 1, marginTop: 'var(--space-4)' }}>
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-3)' }}>
            पिछले 7 दिन
          </p>
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(7, 1fr)',
            gap: 'var(--space-2)',
            textAlign: 'center',
          }}>
            {weekDays.map((day, i) => (
              <div key={day}>
                <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginBottom: 'var(--space-1)' }}>
                  {day}
                </p>
                <div style={{
                  width: 32,
                  height: 32,
                  borderRadius: 'var(--radius-circle)',
                  background: weekLog[i] ? 'var(--state-exceeded-bg)' : 'var(--surface-glass)',
                  border: `1px solid ${weekLog[i] ? 'var(--state-exceeded-border)' : 'var(--border-glass)'}`,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto',
                  fontSize: '14px',
                }}>
                  {weekLog[i] ? '✓' : ''}
                </div>
              </div>
            ))}
          </div>
        </GlassCard>
      </div>
    </div>
  );
}
