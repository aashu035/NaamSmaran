/**
 * BhaktMaal — Reading tracker section.
 */

import { useState, useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';

export default function BhaktMaal({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const updateTodayRecord = useAppStore((s) => s.updateTodayRecord);
  const isDone = todayRecord?.checkBhaktMaal || false;
  const [chapter, setChapter] = useState(1);
  const [page, setPage] = useState(1);

  const toggle = useCallback(() => {
    updateTodayRecord({ checkBhaktMaal: !isDone });
  }, [isDone, updateTodayRecord]);

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="भक्त माल" subtitle={isDone ? '✅ पठन सम्पन्न' : 'पठन शेष'} onBack={onBack} />
      <div className="container" style={{ paddingBottom: '100px' }}>
        <GlassCard className="animate-in" style={{ textAlign: 'center' }}>
          <button className={`btn ${isDone ? 'btn--ghost' : 'btn--primary'} tappable`} onClick={toggle}
            style={{ width: '100%', padding: 'var(--space-4)', fontSize: 'var(--text-body)' }}>
            {isDone ? '📚 पढ़ा — रद्द करें' : '📖 पठन सम्पन्न'}
          </button>
        </GlassCard>
        <GlassCard className="animate-stagger" style={{ '--stagger': 1, marginTop: 'var(--space-4)' }}>
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-4)' }}>पठन प्रगति</p>
          <div style={{ display: 'flex', gap: 'var(--space-4)' }}>
            <div style={{ flex: 1, textAlign: 'center' }}>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginBottom: 'var(--space-1)' }}>अध्याय</p>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 'var(--space-2)' }}>
                <button className="btn btn--ghost" onClick={() => setChapter(Math.max(1, chapter - 1))} style={{ padding: '4px 8px' }}>−</button>
                <span className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>{chapter}</span>
                <button className="btn btn--ghost" onClick={() => setChapter(chapter + 1)} style={{ padding: '4px 8px' }}>+</button>
              </div>
            </div>
            <div style={{ flex: 1, textAlign: 'center' }}>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginBottom: 'var(--space-1)' }}>पृष्ठ</p>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 'var(--space-2)' }}>
                <button className="btn btn--ghost" onClick={() => setPage(Math.max(1, page - 1))} style={{ padding: '4px 8px' }}>−</button>
                <span className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>{page}</span>
                <button className="btn btn--ghost" onClick={() => setPage(page + 1)} style={{ padding: '4px 8px' }}>+</button>
              </div>
            </div>
          </div>
        </GlassCard>
      </div>
    </div>
  );
}
