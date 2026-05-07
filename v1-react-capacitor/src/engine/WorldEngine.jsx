import { useEffect, useMemo, useRef } from 'react';
import { useTheme } from '../components/ThemeProvider';
import { useWorldStore } from './useWorldStore';
import { useAppStore } from '../store/useAppStore';
import ThreeScene from './ThreeScene';
import FramePlayer from './FramePlayer';

/**
 * WorldEngine — Top-level 3D world orchestrator.
 * 
 * Responsibilities:
 * 1. Bridges CSS theme system → Three.js color values
 * 2. Manages Kunj state machine transitions
 * 3. Wraps ThreeScene with correct props
 * 4. Handles graceful degradation if WebGL fails
 * 
 * State machine:
 *   IDLE ──(theme_change)──→ TRANSITIONING ──(anim_complete)──→ IDLE
 *   IDLE ──(tap_jap)──→ INTERACTIVE ──(release)──→ IDLE
 *   IDLE ──(mala_complete)──→ CELEBRATING ──(anim_done)──→ IDLE
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// CSS → Three.js color bridge
// These are hardcoded from tokens.css to avoid runtime getComputedStyle calls per frame
const THEME_COLORS = {
  'sharad-moon': {
    primary: '#E8A0BF',
    secondary: '#B8C8FF',
    gold: '#F5CBA7',
    bg: '#070010',
  },
  'vrindavan-dawn': {
    primary: '#FFD4A3',
    secondary: '#FFAAB5',
    gold: '#FFD700',
    bg: '#0A0408',
  },
  'nikunj': {
    primary: '#A8E6CF',
    secondary: '#FFD4A3',
    gold: '#C8E6A0',
    bg: '#030A04',
  },
  'van-vihaar': {
    primary: '#E8C547',
    secondary: '#89C4A0',
    gold: '#F5D76E',
    bg: '#040806',
  },
  'shayan': {
    primary: '#C8D8F0',
    secondary: '#FFD080',
    gold: '#FFD080',
    bg: '#020209',
  },
};

export default function WorldEngine() {
  const { theme } = useTheme();
  const transitionToKunj = useWorldStore((s) => s.transitionToKunj);
  const settings = useAppStore((s) => s.settings);
  const prevThemeRef = useRef(theme);

  // Get current theme colors
  const themeColors = useMemo(() => {
    return THEME_COLORS[theme] || THEME_COLORS['sharad-moon'];
  }, [theme]);

  // React to theme changes → trigger Kunj transition
  useEffect(() => {
    if (prevThemeRef.current !== theme) {
      transitionToKunj(theme);
      prevThemeRef.current = theme;
    }
  }, [theme, transitionToKunj]);

  // TODO: Read sacredName from AppStore settings when available
  // For now, default to "राधा"
  const sacredName = 'राधा';
  const backgroundMode = settings?.backgroundMode || '3d';

  return (
    <div
      className="world-engine"
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 0,
        pointerEvents: 'none', // Let UI events pass through
      }}
    >
      <div style={{ pointerEvents: 'auto', width: '100%', height: '100%' }}>
        {backgroundMode === 'images' ? (
          <FramePlayer />
        ) : (
          <ThreeScene
            sacredName={sacredName}
            themeColors={themeColors}
            enableBloom={false}
          />
        )}
      </div>
    </div>
  );
}
