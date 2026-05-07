/**
 * ThemeSelector — Visual theme picker with live preview swatches.
 * Shows all 5 themes as clickable cards with color palette preview.
 */

import { useTheme } from '../components/ThemeProvider';
import SectionHeader from '../components/SectionHeader';
import GlassCard from '../components/GlassCard';

const THEMES = [
  {
    id: 'sharad-moon',
    name: 'शरद पूर्णिमा',
    nameEn: 'Sharad Moon',
    description: 'गहरा नील, गुलाबी कमल, चाँदनी',
    colors: ['#070010', '#E8A0BF', '#B8C8FF', '#F5CBA7'],
    gradient: 'linear-gradient(135deg, #E8A0BF, #B8C8FF)',
  },
  {
    id: 'vrindavan-dawn',
    name: 'वृन्दावन प्रभात',
    nameEn: 'Vrindavan Dawn',
    description: 'चम्पक स्वर्ण, गुलाबी मोती',
    colors: ['#0A0408', '#FFD4A3', '#FFAAB5', '#FFD700'],
    gradient: 'linear-gradient(135deg, #FFD4A3, #FFAAB5)',
  },
  {
    id: 'nikunj',
    name: 'निकुंज',
    nameEn: 'Nikunj',
    description: 'पन्ना हरा, सुनहरी रोशनी',
    colors: ['#030A04', '#A8E6CF', '#FFD4A3', '#C8E6A0'],
    gradient: 'linear-gradient(135deg, #A8E6CF, #FFD4A3)',
  },
  {
    id: 'van-vihaar',
    name: 'वन विहार',
    nameEn: 'Van Vihaar',
    description: 'कदम्ब सुनहरा, वन हरा',
    colors: ['#040806', '#E8C547', '#89C4A0', '#F5D76E'],
    gradient: 'linear-gradient(135deg, #E8C547, #89C4A0)',
  },
  {
    id: 'shayan',
    name: 'शयन',
    nameEn: 'Shayan',
    description: 'चाँदनी नीला, दीपक स्वर्ण',
    colors: ['#020209', '#C8D8F0', '#FFD080', '#FFD080'],
    gradient: 'linear-gradient(135deg, #C8D8F0, #FFD080)',
  },
];

export default function ThemeSelector({ onBack }) {
  const { theme, setTheme } = useTheme();

  return (
    <div className="screen" style={{ overflowY: 'auto' }}>
      <SectionHeader title="विषय चयन" subtitle="Theme" onBack={onBack} />
      <div className="container" style={{ paddingBottom: '100px' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
          {THEMES.map((t, i) => {
            const isActive = theme === t.id;
            return (
              <GlassCard
                key={t.id}
                variant="interactive"
                className={i < 5 ? 'animate-stagger' : ''}
                style={{
                  '--stagger': i,
                  borderColor: isActive ? 'var(--accent-primary)' : undefined,
                  borderWidth: isActive ? '2px' : undefined,
                }}
                onClick={() => setTheme(t.id)}
              >
                <div style={{ display: 'flex', gap: 'var(--space-4)', alignItems: 'center' }}>
                  {/* Color swatches */}
                  <div style={{
                    width: '56px',
                    height: '56px',
                    borderRadius: 'var(--radius-button)',
                    background: t.gradient,
                    flexShrink: 0,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}>
                    {isActive && (
                      <span style={{ fontSize: '20px' }}>✓</span>
                    )}
                  </div>

                  {/* Info */}
                  <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
                      <p className="font-deco text-primary font-bold" style={{ fontSize: 'var(--text-body)' }}>
                        {t.name}
                      </p>
                      {isActive && (
                        <span className="font-ui state-exceeded" style={{ fontSize: 'var(--text-label)' }}>
                          सक्रिय
                        </span>
                      )}
                    </div>
                    <p className="font-ui text-tertiary" style={{ fontSize: 'var(--text-tertiary)', marginTop: '2px' }}>
                      {t.description}
                    </p>

                    {/* Color dots */}
                    <div style={{ display: 'flex', gap: '6px', marginTop: 'var(--space-2)' }}>
                      {t.colors.map((c, j) => (
                        <span key={j} style={{
                          width: '14px',
                          height: '14px',
                          borderRadius: '50%',
                          background: c,
                          border: '1px solid rgba(255,255,255,0.15)',
                          display: 'inline-block',
                        }} />
                      ))}
                    </div>
                  </div>
                </div>
              </GlassCard>
            );
          })}
        </div>

        <p className="font-ui text-tertiary text-xs" style={{ textAlign: 'center', marginTop: 'var(--space-6)' }}>
          विषय परिवर्तन तुरंत लागू होगा
        </p>
      </div>
    </div>
  );
}
