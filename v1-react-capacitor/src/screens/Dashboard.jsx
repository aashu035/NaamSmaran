/**
 * Dashboard — Main screen of Naam Smaran.
 * Shows: greeting, progress ring, target/did, quick-add buttons,
 * section grid (6 sections), streak count, and daily quote.
 * 
 * This is the "home" state — 3D world visible behind everything.
 */

import { useEffect } from 'react';
import { useAppStore } from '../store/useAppStore';
import { formatNumber } from '../logic/numberFormat';
import GlassCard from '../components/GlassCard';
import ProgressRing from '../components/ProgressRing';

const QUICK_ADD = [108, 1000, 5000];

const SECTION_GRID = [
  { id: 'naam-jap',      icon: '📿', label: 'नाम जप',          color: 'var(--accent-primary)' },
  { id: 'chaturasi',     icon: '📖', label: 'श्री हित चौरासी', color: 'var(--accent-secondary)' },
  { id: 'maansik-seva',  icon: '🙏', label: 'मानसिक सेवा',     color: 'var(--accent-gold)' },
  { id: 'prarthana',     icon: '🙏', label: 'प्रार्थना',       color: 'var(--accent-primary)' },
  { id: 'bhakt-maal',    icon: '📚', label: 'भक्त माल',        color: 'var(--accent-secondary)' },
  { id: 'charnamrit',    icon: '🙏', label: 'चरणामृत',          color: 'var(--accent-gold)' },
];

export default function Dashboard({ onNavigate, onOpenCalendar, onOpenHistory, onOpenSettings, onShowDayEnd }) {
  const todayRecord = useAppStore((s) => s.todayRecord);
  const settings = useAppStore((s) => s.settings);
  const addJapCount = useAppStore((s) => s.addJapCount);
  const initStore = useAppStore((s) => s.initStore);
  const isLoaded = useAppStore((s) => s.isLoaded);

  // Initialize store on mount
  useEffect(() => {
    if (!isLoaded) {
      initStore();
    }
  }, [isLoaded, initStore]);

  if (!isLoaded || !todayRecord || !settings) {
    return (
      <div className="screen container safe-top" style={{ paddingTop: '80px', textAlign: 'center' }}>
        <p className="font-deco text-accent text-large">राधे राधे</p>
        <p className="font-ui text-tertiary text-small" style={{ marginTop: 'var(--space-4)' }}>
          लोड हो रहा है...
        </p>
      </div>
    );
  }

  const target = todayRecord.target || settings.initialTarget;
  const did = todayRecord.did || 0;
  const progress = target > 0 ? did / target : 0;
  const remaining = Math.max(0, target - did);
  const streak = todayRecord.streakCount || 0;

  // Section completion checks
  const sectionChecks = {
    'naam-jap':     todayRecord.checkNaamJap,
    'chaturasi':    todayRecord.checkChaturasi,
    'maansik-seva': todayRecord.checkMaansikSeva,
    'prarthana':    todayRecord.checkPrarthana,
    'bhakt-maal':   todayRecord.checkBhaktMaal,
    'charnamrit':   todayRecord.checkCharnamrit,
  };

  const completedSections = Object.values(sectionChecks).filter(Boolean).length;

  const months = ['जनवरी', 'फरवरी', 'मार्च', 'अप्रैल', 'मई', 'जून', 'जुलाई', 'अगस्त', 'सितंबर', 'अक्टूबर', 'नवंबर', 'दिसंबर'];
  let displayDate = todayRecord.date;
  if (todayRecord.date) {
    const [y, m, d] = todayRecord.date.split('-');
    displayDate = `${parseInt(d)} ${months[parseInt(m) - 1]} ${y}`;
  }

  return (
    <div className="screen container safe-top" style={{ paddingTop: '60px', paddingBottom: '100px' }}>

      {/* Greeting */}
      <div className="animate-in" style={{ textAlign: 'center', marginBottom: 'var(--space-6)' }}>
        <h1
          className="font-deco text-accent"
          style={{
            fontSize: 'var(--text-large)',
            lineHeight: 'var(--leading-tight)',
          }}
        >
          {settings.deityDisplayName}
        </h1>
        <p className="font-ui text-tertiary text-xs" style={{ marginTop: 'var(--space-1)' }}>
          {todayRecord.dayOfWeek} • {displayDate}
        </p>
      </div>

      {/* Progress Ring + Stats */}
      <GlassCard
        className="animate-stagger"
        style={{ '--stagger': 1, textAlign: 'center' }}
      >
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: 'var(--space-4)',
        }}>
          <ProgressRing progress={progress}>
            <p className="font-numbers text-gradient-numbers font-bold" style={{ fontSize: 'var(--text-large)' }}>
              {formatNumber(did)}
            </p>
            <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)' }}>
              / {formatNumber(target)}
            </p>
          </ProgressRing>

          {/* Remaining count */}
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
            {remaining > 0
              ? `${formatNumber(remaining)} शेष`
              : '🙏 लक्ष्य पूर्ण!'
            }
          </p>

          {/* Streak badge */}
          {streak > 0 && (
            <span
              className="state-streak font-numbers font-bold"
              style={{
                fontSize: 'var(--text-secondary)',
                display: 'flex',
                alignItems: 'center',
                gap: 'var(--space-1)',
              }}
            >
              🔥 {streak} दिन लय
            </span>
          )}
        </div>
      </GlassCard>

      {/* Quick-Add Buttons */}
      <div
        className="animate-stagger"
        style={{
          '--stagger': 2,
          display: 'flex',
          gap: 'var(--space-3)',
          justifyContent: 'center',
          marginTop: 'var(--space-4)',
        }}
      >
        {QUICK_ADD.map((amount) => (
          <button
            key={amount}
            className="btn btn--primary tappable"
            onClick={() => addJapCount(amount)}
          >
            +{formatNumber(amount)}
          </button>
        ))}
      </div>

      {/* Section Grid */}
      <div
        className="animate-stagger"
        style={{
          '--stagger': 3,
          marginTop: 'var(--space-6)',
        }}
      >
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          marginBottom: 'var(--space-3)',
        }}>
          <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
            आज की साधना
          </p>
          <p className="font-numbers text-tertiary" style={{ fontSize: 'var(--text-tertiary)' }}>
            {completedSections}/6
          </p>
        </div>

        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(3, 1fr)',
          gap: 'var(--space-3)',
        }}>
          {SECTION_GRID.map((section) => {
            const isChecked = sectionChecks[section.id];
            return (
              <GlassCard
                key={section.id}
                variant="interactive"
                onClick={() => onNavigate(section.id)}
                style={{
                  textAlign: 'center',
                  padding: 'var(--space-4) var(--space-2)',
                  borderColor: isChecked ? 'var(--state-exceeded)' : undefined,
                }}
              >
                <span style={{ fontSize: '24px', display: 'block' }}>
                  {isChecked ? '✅' : section.icon}
                </span>
                <p
                  className="font-ui"
                  style={{
                    fontSize: 'var(--text-tertiary)',
                    color: isChecked ? 'var(--state-exceeded)' : 'var(--text-secondary)',
                    marginTop: 'var(--space-1)',
                    lineHeight: 'var(--leading-tight)',
                  }}
                >
                  {section.label}
                </p>
              </GlassCard>
            );
          })}
        </div>
      </div>

      {/* Quick Actions Row */}
      <div
        className="animate-stagger"
        style={{
          '--stagger': 4,
          marginTop: 'var(--space-4)',
          display: 'grid',
          gridTemplateColumns: 'repeat(4, 1fr)',
          gap: 'var(--space-3)',
        }}
      >
        {[
          { icon: '📅', label: 'कैलेंडर', action: onOpenCalendar },
          { icon: '📊', label: 'इतिहास', action: onOpenHistory },
          { icon: '🌙', label: 'विश्लेषण', action: onShowDayEnd },
          { icon: '⚙️', label: 'सेटिंग्स', action: onOpenSettings },
        ].map((item) => (
          <GlassCard
            key={item.label}
            variant="interactive"
            onClick={item.action}
            style={{ textAlign: 'center', padding: 'var(--space-3) var(--space-2)' }}
          >
            <span style={{ fontSize: '20px', display: 'block' }}>{item.icon}</span>
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-label)', marginTop: 'var(--space-1)' }}>
              {item.label}
            </p>
          </GlassCard>
        ))}
      </div>

      {/* Daily Quote */}
      <GlassCard
        className="animate-stagger"
        style={{
          '--stagger': 5,
          marginTop: 'var(--space-6)',
          textAlign: 'center',
        }}
      >
        <p
          className="font-deco text-accent"
          style={{
            fontSize: 'var(--text-body)',
            lineHeight: 'var(--leading-relaxed)',
            fontStyle: 'italic',
          }}
        >
          "गुरु कृपा केवलं"
        </p>
        <p className="font-ui text-tertiary text-xs" style={{ marginTop: 'var(--space-2)' }}>
          — श्री हित हरिवंश महाप्रभु जी
        </p>
      </GlassCard>

      {/* Footer */}
      <p
        className="font-deco text-tertiary text-xs animate-stagger"
        style={{ '--stagger': 6, textAlign: 'center', marginTop: 'var(--space-10)' }}
      >
        श्री राधावल्लभ लाल जु की जय
      </p>
    </div>
  );
}
