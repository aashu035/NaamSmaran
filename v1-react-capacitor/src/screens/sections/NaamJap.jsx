/**
 * NaamJap — Primary naam jap counter section.
 * 4 input modes as specified in AGENTS.md §4:
 *   1. Quick-Add buttons (+108, +1000, +5000)
 *   2. Custom number input
 *   3. Tap-per-Jap (each tap = +1)
 *   4. Mala tracker (108-bead circle)
 */

import { useState, useCallback } from 'react';
import { useAppStore } from '../../store/useAppStore';
import { formatNumber } from '../../logic/numberFormat';
import SectionHeader from '../../components/SectionHeader';
import GlassCard from '../../components/GlassCard';
import ProgressRing from '../../components/ProgressRing';
import MalaBead from '../../components/MalaBead';

const MODES = [
  { id: 'quick',  label: 'त्वरित' },
  { id: 'custom', label: 'कस्टम' },
  { id: 'tap',    label: 'टैप' },
  { id: 'mala',   label: 'माला' },
];

const QUICK_ADD = [108, 1000, 5000];

export default function NaamJap({ onBack }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const settings = useAppStore((s) => s.settings);
  const addJapCount = useAppStore((s) => s.addJapCount);

  const [activeMode, setActiveMode] = useState('quick');
  const [customValue, setCustomValue] = useState('');

  // Mala state — computed from today's did count
  const did = todayRecord?.did || 0;
  const target = todayRecord?.target || settings?.initialTarget || 21600;
  const progress = target > 0 ? did / target : 0;

  // Mala computation
  const currentBead = did % 108;
  const totalMalas = Math.floor(did / 108);

  const handleCustomAdd = useCallback(() => {
    const val = parseInt(customValue, 10);
    if (val > 0 && val <= 100000) {
      addJapCount(val);
      setCustomValue('');
    }
  }, [customValue, addJapCount]);

  const handleTap = useCallback(() => {
    addJapCount(1);
  }, [addJapCount]);

  const handleMalaTap = useCallback(() => {
    addJapCount(1);
  }, [addJapCount]);

  if (!todayRecord) return null;

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader
        title="नाम जप"
        subtitle={`${formatNumber(did)} / ${formatNumber(target)}`}
        onBack={onBack}
      />

      <div className="container" style={{ paddingBottom: '100px' }}>

        {/* Progress summary */}
        <GlassCard className="animate-in" style={{ textAlign: 'center', marginBottom: 'var(--space-4)' }}>
          <ProgressRing progress={progress} size="medium">
            <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-medium)' }}>
              {Math.min(100, Math.round(progress * 100))}%
            </p>
          </ProgressRing>
          <p className="font-numbers font-bold text-gradient-numbers" style={{ fontSize: 'var(--text-hero)', marginTop: 'var(--space-3)' }}>
            {formatNumber(did)}
          </p>
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginTop: 'var(--space-1)' }}>
            आज का लक्ष्य: {formatNumber(target)}
          </p>
        </GlassCard>

        {/* Mode pills */}
        <div
          className="hide-scrollbar"
          style={{
            display: 'flex',
            gap: 'var(--space-2)',
            overflowX: 'auto',
            marginBottom: 'var(--space-4)',
            paddingBottom: 'var(--space-1)',
          }}
        >
          {MODES.map((mode) => (
            <button
              key={mode.id}
              className={activeMode === mode.id ? 'pill pill--active' : 'pill pill--inactive'}
              onClick={() => setActiveMode(mode.id)}
            >
              {mode.label}
            </button>
          ))}
        </div>

        {/* Mode content */}
        {activeMode === 'quick' && (
          <GlassCard className="animate-in">
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-4)' }}>
              जल्दी जोड़ें
            </p>
            <div style={{ display: 'flex', gap: 'var(--space-3)', flexWrap: 'wrap', justifyContent: 'center' }}>
              {QUICK_ADD.map((amount) => (
                <button
                  key={amount}
                  className="btn btn--primary tappable"
                  onClick={() => addJapCount(amount)}
                  style={{ flex: '1 1 auto', minWidth: '80px' }}
                >
                  +{formatNumber(amount)}
                </button>
              ))}
            </div>
          </GlassCard>
        )}

        {activeMode === 'custom' && (
          <GlassCard className="animate-in">
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)', marginBottom: 'var(--space-4)' }}>
              संख्या दर्ज करें
            </p>
            <div style={{ display: 'flex', gap: 'var(--space-3)' }}>
              <input
                type="number"
                className="glass-input font-numbers"
                value={customValue}
                onChange={(e) => setCustomValue(e.target.value)}
                placeholder="संख्या..."
                min="1"
                max="100000"
                inputMode="numeric"
                style={{ flex: 1 }}
              />
              <button
                className="btn btn--primary tappable"
                onClick={handleCustomAdd}
                disabled={!customValue || parseInt(customValue, 10) <= 0}
              >
                जोड़ें
              </button>
            </div>
          </GlassCard>
        )}

        {activeMode === 'tap' && (
          <div className="animate-in" style={{ textAlign: 'center' }}>
            <button
              className="tappable"
              onClick={handleTap}
              style={{
                width: 200,
                height: 200,
                borderRadius: 'var(--radius-circle)',
                background: 'var(--surface-glass)',
                border: '2px solid var(--border-glass)',
                backdropFilter: 'var(--glass-blur)',
                WebkitBackdropFilter: 'var(--glass-blur)',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                margin: '0 auto',
                cursor: 'pointer',
              }}
            >
              <span className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-hero)' }}>
                {formatNumber(did)}
              </span>
              <span className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginTop: 'var(--space-2)' }}>
                टैप करें
              </span>
            </button>
            <p className="font-ui text-tertiary text-xs" style={{ marginTop: 'var(--space-4)' }}>
              हर टैप = +1 नाम जप
            </p>
          </div>
        )}

        {activeMode === 'mala' && (
          <div className="animate-in">
            <MalaBead
              currentBead={currentBead}
              totalMalas={totalMalas}
              totalCount={did}
              onTap={handleMalaTap}
            />
          </div>
        )}
      </div>
    </div>
  );
}
