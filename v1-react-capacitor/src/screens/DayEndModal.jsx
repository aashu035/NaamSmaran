/**
 * DayEndModal — End-of-day analysis overlay.
 * Shows daily summary: target vs did, streak, sections completed, quote.
 * Uses elevated glass variant with heavy scrim per tokens.css.
 */

import { useEffect, useState } from 'react';
import { useAppStore } from '../store/useAppStore';
import { formatNumber } from '../logic/numberFormat';
import GlassCard from '../components/GlassCard';
import ProgressRing from '../components/ProgressRing';

export default function DayEndModal({ onClose }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const settings = useAppStore((s) => s.settings);
  const [visible, setVisible] = useState(false);

  // Animate in
  useEffect(() => {
    requestAnimationFrame(() => setVisible(true));
  }, []);

  const handleClose = () => {
    setVisible(false);
    setTimeout(onClose, 300);
  };

  if (!todayRecord || !settings) return null;

  const target = todayRecord.target || settings.initialTarget;
  const did = todayRecord.did || 0;
  const progress = target > 0 ? did / target : 0;
  const met = did >= target;
  const streak = todayRecord.streakCount || 0;

  // Section completion summary
  const sections = [
    { key: 'checkNaamJap',      label: 'नाम जप' },
    { key: 'checkChaturasi',    label: 'श्री हित चौरासी' },
    { key: 'checkMaansikSeva',  label: 'मानसिक सेवा' },
    { key: 'checkPrarthana',    label: 'प्रार्थना' },
    { key: 'checkBhaktMaal',    label: 'भक्त माल' },
    { key: 'checkCharnamrit',   label: 'चरणामृत' },
  ];
  const completedSections = sections.filter((s) => todayRecord[s.key]).length;

  // Motivational messages
  const getMessage = () => {
    if (did === 0) return 'कल नई शुरुआत होगी 🙏';
    if (met && streak > 7) return `🔥 ${streak} दिन की लय! अद्भुत!`;
    if (met) return '🙏 लक्ष्य पूर्ण! बहुत सुन्दर!';
    if (progress > 0.8) return 'बहुत करीब! कल पूरा होगा 🌸';
    if (progress > 0.5) return 'अच्छी साधना! और बढ़ते रहें 🌺';
    return 'हर नाम जप का फल मिलता है 🪷';
  };

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 'var(--z-modal)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 'var(--space-6)',
        background: `rgba(0, 0, 0, var(--opacity-scrim))`,
        backdropFilter: 'blur(10px)',
        WebkitBackdropFilter: 'blur(10px)',
        opacity: visible ? 1 : 0,
        transition: `opacity var(--duration-normal) var(--ease-out)`,
      }}
      onClick={handleClose}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          width: '100%',
          maxWidth: '380px',
          transform: visible ? 'scale(1) translateY(0)' : 'scale(0.95) translateY(20px)',
          transition: `transform var(--duration-normal) var(--ease-out)`,
        }}
      >
        <GlassCard variant="elevated" style={{ textAlign: 'center', padding: 'var(--space-8) var(--space-6)' }}>
          {/* Header */}
          <p className="font-deco text-accent" style={{ fontSize: 'var(--text-medium)', marginBottom: 'var(--space-2)' }}>
            दिन का विश्लेषण
          </p>
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginBottom: 'var(--space-6)' }}>
            {todayRecord.dayOfWeek} • {todayRecord.date}
          </p>

          {/* Progress ring */}
          <div style={{ marginBottom: 'var(--space-5)' }}>
            <ProgressRing progress={progress} size="large">
              <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-large)' }}>
                {Math.round(progress * 100)}%
              </p>
            </ProgressRing>
          </div>

          {/* Did vs Target */}
          <div style={{ display: 'flex', justifyContent: 'center', gap: 'var(--space-8)', marginBottom: 'var(--space-5)' }}>
            <div>
              <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-body)' }}>
                {formatNumber(did)}
              </p>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>जप किया</p>
            </div>
            <div>
              <p className="font-numbers font-bold text-secondary" style={{ fontSize: 'var(--text-body)' }}>
                {formatNumber(target)}
              </p>
              <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)' }}>लक्ष्य</p>
            </div>
          </div>

          {/* Status badge */}
          <div style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: 'var(--space-2)',
            padding: 'var(--space-2) var(--space-4)',
            borderRadius: 'var(--radius-chip)',
            background: met ? 'var(--state-exceeded-bg)' : 'var(--state-partial-bg)',
            border: met ? '1px solid var(--state-exceeded-border)' : '1px solid rgba(245, 215, 110, 0.25)',
            marginBottom: 'var(--space-4)',
          }}>
            <span className={`font-ui font-bold ${met ? 'state-exceeded' : ''}`} style={{
              fontSize: 'var(--text-secondary)',
              color: met ? 'var(--state-exceeded)' : 'var(--state-partial)',
            }}>
              {met ? '✅ लक्ष्य पूर्ण' : `${formatNumber(Math.max(0, target - did))} शेष`}
            </span>
          </div>

          {/* Streak */}
          {streak > 0 && (
            <p className="font-numbers font-bold" style={{
              fontSize: 'var(--text-secondary)',
              color: 'var(--state-streak-fire)',
              marginBottom: 'var(--space-4)',
            }}>
              🔥 {streak} दिन की लय
            </p>
          )}

          {/* Sections completed */}
          <div style={{
            background: 'var(--surface-glass)',
            borderRadius: 'var(--radius-button)',
            padding: 'var(--space-3) var(--space-4)',
            marginBottom: 'var(--space-4)',
          }}>
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-tertiary)', marginBottom: 'var(--space-2)' }}>
              साधना विभाग: {completedSections}/6
            </p>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)', justifyContent: 'center' }}>
              {sections.map((sec) => (
                <span key={sec.key} className="font-ui" style={{
                  fontSize: 'var(--text-label)',
                  color: todayRecord[sec.key] ? 'var(--state-exceeded)' : 'rgba(255,255,255,0.25)',
                }}>
                  {todayRecord[sec.key] ? '✅' : '○'} {sec.label}
                </span>
              ))}
            </div>
          </div>

          {/* Motivational message */}
          <p className="font-deco text-accent" style={{
            fontSize: 'var(--text-secondary)',
            fontStyle: 'italic',
            lineHeight: 'var(--leading-relaxed)',
            marginBottom: 'var(--space-6)',
          }}>
            {getMessage()}
          </p>

          {/* Close button */}
          <button
            className="btn btn--primary tappable"
            onClick={handleClose}
            style={{ width: '100%', padding: 'var(--space-4)' }}
          >
            बंद करें
          </button>
        </GlassCard>
      </div>
    </div>
  );
}
