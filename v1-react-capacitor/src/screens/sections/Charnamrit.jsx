/**
 * Charnamrit — Daily toggle + monthly calendar grid.
 */

import { useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';

export default function Charnamrit({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const updateTodayRecord = useAppStore((s) => s.updateTodayRecord);
  const isDone = todayRecord?.checkCharnamrit || false;

  const toggle = useCallback(() => {
    updateTodayRecord({ checkCharnamrit: !isDone });
  }, [isDone, updateTodayRecord]);

  // Mock monthly data (will be populated from IndexedDB)
  const now = new Date();
  const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
  const today = now.getDate();
  const mockDone = new Set([1,2,3,5,6,7,8,10,12,14,15,16,18,19,20,22,23,25,26,27,28]);
  if (isDone) mockDone.add(today);

  const monthNames = ['जनवरी','फरवरी','मार्च','अप्रैल','मई','जून','जुलाई','अगस्त','सितम्बर','अक्तूबर','नवम्बर','दिसम्बर'];

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="चरणामृत" subtitle={isDone ? '✅ आज लिया' : 'आज शेष'} onBack={onBack} />
      <div className="container" style={{ paddingBottom: '100px' }}>
        <GlassCard className="animate-in" style={{ textAlign: 'center' }}>
          <button className={`btn ${isDone ? 'btn--ghost' : 'btn--primary'} tappable`} onClick={toggle}
            style={{ width: '100%', padding: 'var(--space-4)', fontSize: 'var(--text-body)' }}>
            {isDone ? '🙏 लिया — रद्द करें' : '🙏 चरणामृत लिया'}
          </button>
        </GlassCard>

        {/* Monthly calendar */}
        <GlassCard className="animate-stagger" style={{ '--stagger': 1, marginTop: 'var(--space-4)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 'var(--space-3)' }}>
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              {monthNames[now.getMonth()]} {now.getFullYear()}
            </p>
            <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-secondary)' }}>
              {mockDone.size}/{daysInMonth}
            </p>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: '4px' }}>
            {Array.from({ length: daysInMonth }, (_, i) => {
              const day = i + 1;
              const done = mockDone.has(day);
              const isToday = day === today;
              return (
                <div key={day} style={{
                  width: '100%', aspectRatio: '1', borderRadius: 'var(--radius-small)',
                  background: done ? 'var(--state-exceeded-bg)' : day > today ? 'transparent' : 'var(--surface-glass)',
                  border: isToday ? '1px solid var(--accent-primary)' : done ? '1px solid var(--state-exceeded-border)' : '1px solid transparent',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: 'var(--text-label)',
                  color: done ? 'var(--state-exceeded)' : day > today ? 'rgba(255,255,255,0.15)' : 'rgba(255,255,255, var(--opacity-tertiary))',
                }}>
                  {day}
                </div>
              );
            })}
          </div>
        </GlassCard>
      </div>
    </div>
  );
}
