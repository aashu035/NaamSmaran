import { create } from 'zustand';

/**
 * World Engine State — manages the 3D Kunj world independently of app data.
 * 
 * This store is consumed by all src/engine/ components.
 * It does NOT persist to IndexedDB — world state is ephemeral per session.
 * 
 * State machine:
 *   IDLE ──(theme_change)──→ TRANSITIONING ──(anim_complete)──→ IDLE
 *   IDLE ──(tap_jap)──→ INTERACTIVE ──(release)──→ IDLE
 *   IDLE ──(mala_complete)──→ CELEBRATING ──(anim_done)──→ IDLE
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

export const useWorldStore = create((set, get) => ({
  // ─── Kunj state ───────────────────────────────────────────
  currentKunj: 'sharad-moon',
  previousKunj: null,
  kunjState: 'IDLE',           // 'IDLE' | 'TRANSITIONING' | 'REVEALING' | 'INTERACTIVE' | 'CELEBRATING'
  transitionProgress: 0,       // 0–1 during transitions

  // ─── Frame player ────────────────────────────────────────
  frameIndex: 0,
  totalFrames: 0,
  framesLoaded: false,

  // ─── Particles ────────────────────────────────────────────
  particleIntensity: 1.0,      // 0–1, reduced during heavy UI interactions
  tapBurstPosition: null,      // { x, y, z } for tap particle burst origin
  tapBurstActive: false,

  // ─── Camera ───────────────────────────────────────────────
  gyroEnabled: false,
  gyroOffset: { x: 0, y: 0 },  // ±5° offset from gyroscope / mouse

  // ─── Actions ──────────────────────────────────────────────

  /**
   * Initiate a Kunj transition (triggered by theme change).
   * Sets previousKunj for crossfade, advances state machine.
   */
  transitionToKunj: (kunjName) => {
    const { currentKunj, kunjState } = get();
    if (kunjName === currentKunj || kunjState === 'TRANSITIONING') return;

    set({
      previousKunj: currentKunj,
      currentKunj: kunjName,
      kunjState: 'TRANSITIONING',
      transitionProgress: 0,
    });
  },

  /**
   * Update transition progress (0–1). Called from animation loop.
   */
  setTransitionProgress: (progress) => {
    set({ transitionProgress: Math.min(1, Math.max(0, progress)) });

    // Auto-complete transition when progress reaches 1
    if (progress >= 1) {
      set({
        kunjState: 'IDLE',
        previousKunj: null,
        transitionProgress: 1,
      });
    }
  },

  /**
   * Directly set kunjState for non-transition state changes.
   */
  setKunjState: (state) => set({ kunjState: state }),

  /**
   * Trigger a golden particle burst at a world position.
   */
  triggerTapBurst: (position) => {
    set({
      tapBurstPosition: position,
      tapBurstActive: true,
    });

    // Auto-deactivate after burst animation completes (600ms)
    setTimeout(() => {
      set({ tapBurstActive: false });
    }, 600);
  },

  /**
   * Update gyroscope/mouse parallax offset. Clamped to ±5°.
   */
  updateGyroOffset: (offset) => {
    const clamp = (v) => Math.max(-5, Math.min(5, v));
    set({
      gyroOffset: {
        x: clamp(offset.x),
        y: clamp(offset.y),
      },
    });
  },

  /**
   * Toggle gyroscope on/off.
   */
  setGyroEnabled: (enabled) => set({ gyroEnabled: enabled }),

  /**
   * Frame player controls.
   */
  setFrameIndex: (index) => set({ frameIndex: index }),
  setTotalFrames: (total) => set({ totalFrames: total }),
  setFramesLoaded: (loaded) => set({ framesLoaded: loaded }),

  /**
   * Adjust particle intensity (0–1). Used to reduce particle load during heavy UI.
   */
  setParticleIntensity: (intensity) => {
    set({ particleIntensity: Math.min(1, Math.max(0, intensity)) });
  },
}));
