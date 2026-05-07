/**
 * Settings — Configuration screen for Naam Smaran.
 * Sections: Target settings, Day boundary, Reminders, Audio, Display, Backup.
 * All settings persisted via useAppStore.updateSetting → IndexedDB.
 * 
 * Per AGENTS.md §6 AppSettings schema.
 */

import { useState } from 'react';
import { useAppStore } from '../store/useAppStore';
import { formatNumber } from '../logic/numberFormat';
import SectionHeader from '../components/SectionHeader';
import GlassCard from '../components/GlassCard';

/* ─── Toggle row component ─── */
function SettingToggle({ label, value, onChange }) {
  return (
    <div style={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: 'var(--space-3) 0',
      borderBottom: '1px solid var(--border-glass)',
    }}>
      <span className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
        {label}
      </span>
      <button
        className="tappable"
        onClick={() => onChange(!value)}
        style={{
          width: '48px',
          height: '28px',
          borderRadius: 'var(--radius-chip)',
          border: 'none',
          background: value ? 'var(--gradient-accent)' : 'var(--surface-glass-active)',
          position: 'relative',
          transition: 'background var(--duration-fast) var(--ease-out)',
        }}
      >
        <span style={{
          position: 'absolute',
          top: '3px',
          left: value ? '23px' : '3px',
          width: '22px',
          height: '22px',
          borderRadius: '50%',
          background: 'var(--text-primary)',
          transition: 'left var(--duration-fast) var(--ease-out)',
        }} />
      </button>
    </div>
  );
}

/* ─── Number stepper component ─── */
function SettingStepper({ label, hint, value, onChange, step = 1000, min = 0, max = Infinity }) {
  const handleDecrease = () => {
    const newVal = Math.max(min, value - step);
    onChange(newVal);
  };
  const handleIncrease = () => {
    const newVal = Math.min(max, value + step);
    onChange(newVal);
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: 'var(--space-3) 0',
      borderBottom: '1px solid var(--border-glass)',
    }}>
      <div>
        <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
          {label}
        </p>
        {hint && (
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginTop: '2px' }}>
            {hint}
          </p>
        )}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
        <button
          className="btn btn--ghost tappable"
          onClick={handleDecrease}
          disabled={value <= min}
          style={{ fontSize: 'var(--text-body)', padding: 'var(--space-1) var(--space-2)', opacity: value <= min ? 0.3 : 1 }}
        >
          −
        </button>
        <span className="font-numbers text-primary font-bold" style={{ fontSize: 'var(--text-secondary)', minWidth: '60px', textAlign: 'center' }}>
          {formatNumber(value)}
        </span>
        <button
          className="btn btn--ghost tappable"
          onClick={handleIncrease}
          disabled={value >= max}
          style={{ fontSize: 'var(--text-body)', padding: 'var(--space-1) var(--space-2)', opacity: value >= max ? 0.3 : 1 }}
        >
          +
        </button>
      </div>
    </div>
  );
}

/* ─── Section divider ─── */
function SectionTitle({ title, icon }) {
  return (
    <p className="font-ui text-accent font-bold" style={{
      fontSize: 'var(--text-secondary)',
      marginTop: 'var(--space-4)',
      marginBottom: 'var(--space-2)',
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-2)',
    }}>
      {icon && <span>{icon}</span>}
      {title}
    </p>
  );
}

export default function Settings({ onBack, onOpenThemeSelector }) {
  const settings = useAppStore((s) => s.settings);
  const updateSetting = useAppStore((s) => s.updateSetting);
  const [showResetConfirm, setShowResetConfirm] = useState(false);

  if (!settings) {
    return (
      <div className="screen container safe-top" style={{ paddingTop: '80px', textAlign: 'center' }}>
        <p className="font-ui text-tertiary">लोड हो रहा है...</p>
      </div>
    );
  }

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="सेटिंग्स" subtitle="Settings" onBack={onBack} />
      <div className="container" style={{ paddingBottom: '120px' }}>

        {/* ═══ Target Settings ═══ */}
        <SectionTitle title="लक्ष्य सेटिंग्स" icon="🎯" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 0 }}>
          <SettingStepper
            label="प्रारंभिक लक्ष्य"
            hint="नए दिन का शुरुआती लक्ष्य"
            value={settings.initialTarget}
            onChange={(v) => updateSetting('initialTarget', v)}
            step={1000}
            min={1000}
          />
          <SettingStepper
            label="वृद्धि मात्रा"
            hint="लक्ष्य पूर्ण होने पर अगला वृद्धि"
            value={settings.targetIncrement}
            onChange={(v) => updateSetting('targetIncrement', v)}
            step={100}
            min={100}
            max={10000}
          />
        </GlassCard>

        {/* ═══ Day Boundary ═══ */}
        <SectionTitle title="दिन की सीमा" icon="🌙" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 1 }}>
          <SettingStepper
            label="दिन बदलने का समय"
            hint={`रात ${settings.dayBoundaryHour}:00 बजे के बाद नया दिन`}
            value={settings.dayBoundaryHour}
            onChange={(v) => updateSetting('dayBoundaryHour', v)}
            step={1}
            min={1}
            max={5}
          />
        </GlassCard>

        {/* ═══ Display ═══ */}
        <SectionTitle title="प्रदर्शन" icon="✨" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 2 }}>
          {/* Background Mode */}
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: 'var(--space-3) 0',
            borderBottom: '1px solid var(--border-glass)',
          }}>
            <span className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              पृष्ठभूमि प्रकार (Background)
            </span>
            <select
              className="glass-input font-ui"
              value={settings.backgroundMode || '3d'}
              onChange={(e) => updateSetting('backgroundMode', e.target.value)}
              style={{ padding: '4px 8px', width: 'auto', backgroundColor: 'transparent', color: 'var(--accent-primary)', border: 'none', textAlign: 'right', outline: 'none' }}
            >
              <option value="3d" style={{ color: '#000' }}>3D Particles</option>
              <option value="images" style={{ color: '#000' }}>Custom Images</option>
            </select>
          </div>

          {/* Theme button */}
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: 'var(--space-3) 0',
            borderBottom: '1px solid var(--border-glass)',
          }}>
            <span className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              विषय (Theme)
            </span>
            <button
              className="btn btn--ghost tappable"
              onClick={onOpenThemeSelector}
              style={{
                fontSize: 'var(--text-secondary)',
                color: 'var(--accent-primary)',
                padding: 'var(--space-1) var(--space-3)',
              }}
            >
              बदलें →
            </button>
          </div>

          {/* Deity display name */}
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: 'var(--space-3) 0',
            borderBottom: '1px solid var(--border-glass)',
          }}>
            <span className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              अभिवादन
            </span>
            <span className="font-deco text-accent" style={{ fontSize: 'var(--text-secondary)' }}>
              {settings.deityDisplayName}
            </span>
          </div>

          <SettingToggle
            label="भारतीय अंक प्रणाली"
            value={settings.useIndianNumbering}
            onChange={(v) => updateSetting('useIndianNumbering', v)}
          />
        </GlassCard>

        {/* ═══ Reminders ═══ */}
        <SectionTitle title="स्मारक (Reminders)" icon="🔔" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 3 }}>
          <SettingToggle
            label={`प्रातःकालीन (${settings.reminderMorningTime})`}
            value={settings.reminderMorningEnabled}
            onChange={(v) => updateSetting('reminderMorningEnabled', v)}
          />
          <SettingToggle
            label={`सायंकालीन (${settings.reminderEveningTime})`}
            value={settings.reminderEveningEnabled}
            onChange={(v) => updateSetting('reminderEveningEnabled', v)}
          />
          <SettingToggle
            label={`दिन का अंत (${settings.reminderDayEndTime})`}
            value={settings.reminderDayEndEnabled}
            onChange={(v) => updateSetting('reminderDayEndEnabled', v)}
          />
          <SettingToggle
            label={`प्रगति (${settings.reminderProgressTime})`}
            value={settings.reminderProgressEnabled}
            onChange={(v) => updateSetting('reminderProgressEnabled', v)}
          />
          <SettingToggle
            label="प्रोत्साहन"
            value={settings.reminderEncouragementEnabled}
            onChange={(v) => updateSetting('reminderEncouragementEnabled', v)}
          />
        </GlassCard>

        {/* ═══ Audio ═══ */}
        <SectionTitle title="ऑडियो" icon="🎵" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 4 }}>
          <div style={{
            padding: 'var(--space-3) 0',
            borderBottom: '1px solid var(--border-glass)',
          }}>
            <p className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              ऑडियो फ़ाइल
            </p>
            <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginTop: '2px' }}>
              {settings.audioFileName || 'कोई फ़ाइल नहीं — documents/audio/ में mp3 रखें'}
            </p>
          </div>
          <SettingToggle
            label="दोहराएँ (Loop)"
            value={settings.audioLoop}
            onChange={(v) => updateSetting('audioLoop', v)}
          />
        </GlassCard>

        {/* ═══ Backup ═══ */}
        <SectionTitle title="बैकअप" icon="☁️" />
        <GlassCard className="animate-stagger" style={{ '--stagger': 5 }}>
          <SettingToggle
            label="स्वचालित बैकअप"
            value={settings.autoBackupEnabled}
            onChange={(v) => updateSetting('autoBackupEnabled', v)}
          />
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: 'var(--space-3) 0',
            borderBottom: '1px solid var(--border-glass)',
          }}>
            <span className="font-ui text-secondary" style={{ fontSize: 'var(--text-secondary)' }}>
              अंतिम बैकअप
            </span>
            <span className="font-numbers text-tertiary" style={{ fontSize: 'var(--text-label)' }}>
              {settings.lastBackupTimestamp
                ? new Date(settings.lastBackupTimestamp).toLocaleDateString('hi-IN')
                : 'कभी नहीं'}
            </span>
          </div>
          <div style={{ padding: 'var(--space-3) 0', display: 'flex', gap: 'var(--space-3)' }}>
            <button className="btn btn--primary tappable" style={{ flex: 1, fontSize: 'var(--text-tertiary)' }}>
              अभी बैकअप लें
            </button>
            <button className="btn btn--ghost tappable" style={{ flex: 1, fontSize: 'var(--text-tertiary)' }}>
              पुनर्स्थापित करें
            </button>
          </div>
          <div style={{ padding: 'var(--space-3) 0', display: 'flex', justifyContent: 'center' }}>
            {!showResetConfirm ? (
              <button 
                className="btn btn--ghost tappable" 
                onClick={() => setShowResetConfirm(true)}
                style={{ color: '#ff6b6b', fontSize: 'var(--text-tertiary)' }}
              >
                सभी डेटा मिटाएं (Reset Data)
              </button>
            ) : (
              <div style={{ display: 'flex', gap: 'var(--space-2)' }}>
                <button 
                  className="btn btn--primary tappable" 
                  onClick={async () => {
                    await useAppStore.getState().resetAllData();
                    setShowResetConfirm(false);
                    window.location.reload();
                  }}
                  style={{ background: '#ff6b6b', color: 'white', fontSize: 'var(--text-tertiary)' }}
                >
                  हाँ, सब मिटाएं
                </button>
                <button 
                  className="btn btn--ghost tappable" 
                  onClick={() => setShowResetConfirm(false)}
                  style={{ fontSize: 'var(--text-tertiary)' }}
                >
                  रद्द
                </button>
              </div>
            )}
          </div>
        </GlassCard>

        {/* ═══ About ═══ */}
        <GlassCard className="animate-stagger" style={{ '--stagger': 6, marginTop: 'var(--space-6)', textAlign: 'center' }}>
          <p className="font-deco text-accent" style={{ fontSize: 'var(--text-body)' }}>
            नाम स्मरण
          </p>
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginTop: 'var(--space-1)' }}>
            व्यक्तिगत साधना साथी
          </p>
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginTop: 'var(--space-2)' }}>
            श्री हित हरिवंश महाप्रभु जी की जय
          </p>
          <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-label)', marginTop: 'var(--space-1)' }}>
            राधावल्लभ सम्प्रदाय
          </p>
        </GlassCard>

      </div>
    </div>
  );
}
