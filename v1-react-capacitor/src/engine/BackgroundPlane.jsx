import { useRef, useMemo, useEffect, useState } from 'react';
import { useFrame, useThree } from '@react-three/fiber';
import * as THREE from 'three';
import { useWorldStore } from './useWorldStore';

/**
 * BackgroundPlane — Full-screen background rendered as a Three.js textured quad.
 * 
 * Architecture:
 * 1. On mount, loads frame images for the current theme from /frames/{theme}/
 * 2. Plays frames in sequence at ~8fps for a smooth, cinematic loop
 * 3. Falls back to animated radial gradient if no frames are found
 * 4. Cross-fades between themes during transition
 * 
 * Frame naming convention: frame_0001.jpg, frame_0002.jpg, ...
 * Frames are subsampled (every 10th from source) for APK size control.
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// Frame count per theme (based on copied subsets)
const FRAME_COUNTS = {
  'sharad-moon': 40,
  'vrindavan-dawn': 26,
  'nikunj': 40,
  'van-vihaar': 26,
  'shayan': 40,
  'splash': 51,
};

// Per-theme gradient color palettes — fallback when frames not loaded
const THEME_GRADIENTS = {
  'sharad-moon': {
    bg: '#070010',
    stops: [
      { color: 'rgba(184, 200, 255, 0.35)', cx: 0.3, cy: 0.2, r: 0.7 },
      { color: 'rgba(232, 160, 191, 0.25)', cx: 0.7, cy: 0.8, r: 0.6 },
      { color: 'rgba(245, 203, 167, 0.15)', cx: 0.5, cy: 0.5, r: 0.4 },
    ],
  },
  'vrindavan-dawn': {
    bg: '#0A0408',
    stops: [
      { color: 'rgba(255, 212, 163, 0.40)', cx: 0.4, cy: 0.15, r: 0.65 },
      { color: 'rgba(255, 170, 181, 0.28)', cx: 0.6, cy: 0.75, r: 0.55 },
      { color: 'rgba(255, 215, 0, 0.12)',   cx: 0.2, cy: 0.5,  r: 0.35 },
    ],
  },
  'nikunj': {
    bg: '#030A04',
    stops: [
      { color: 'rgba(168, 230, 207, 0.35)', cx: 0.35, cy: 0.25, r: 0.7 },
      { color: 'rgba(255, 212, 163, 0.22)', cx: 0.65, cy: 0.7,  r: 0.5 },
      { color: 'rgba(200, 230, 160, 0.14)', cx: 0.5,  cy: 0.5,  r: 0.4 },
    ],
  },
  'van-vihaar': {
    bg: '#040806',
    stops: [
      { color: 'rgba(232, 197, 71, 0.35)',  cx: 0.45, cy: 0.2,  r: 0.65 },
      { color: 'rgba(137, 196, 160, 0.25)', cx: 0.55, cy: 0.75, r: 0.55 },
      { color: 'rgba(245, 215, 110, 0.14)', cx: 0.3,  cy: 0.5,  r: 0.35 },
    ],
  },
  'shayan': {
    bg: '#020209',
    stops: [
      { color: 'rgba(200, 216, 240, 0.30)', cx: 0.4, cy: 0.25, r: 0.65 },
      { color: 'rgba(255, 208, 128, 0.20)', cx: 0.6, cy: 0.7,  r: 0.5 },
      { color: 'rgba(200, 216, 240, 0.12)', cx: 0.5, cy: 0.5,  r: 0.35 },
    ],
  },
  'splash': {
    bg: '#070010',
    stops: [
      { color: 'rgba(232, 160, 191, 0.40)', cx: 0.5, cy: 0.5, r: 0.8 },
      { color: 'rgba(184, 200, 255, 0.20)', cx: 0.5, cy: 0.2, r: 0.6 },
      { color: 'rgba(245, 203, 167, 0.10)', cx: 0.5, cy: 0.8, r: 0.5 },
    ],
  },
};

// Offscreen canvas resolution
const CANVAS_WIDTH = 512;
const CANVAS_HEIGHT = 1024;

// Frame playback rate (frames per second)
const FRAME_FPS = 8;
const FRAME_INTERVAL = 1000 / FRAME_FPS; // ~125ms per frame

/**
 * Draw a per-theme gradient onto an offscreen canvas context.
 */
function drawGradient(ctx, themeName, time) {
  const theme = THEME_GRADIENTS[themeName] || THEME_GRADIENTS['sharad-moon'];

  ctx.fillStyle = theme.bg;
  ctx.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

  theme.stops.forEach((stop, i) => {
    const drift = Math.sin(time * 0.0003 + i * 2.1) * 0.03;
    const cx = (stop.cx + drift) * CANVAS_WIDTH;
    const cy = (stop.cy + Math.cos(time * 0.0002 + i * 1.7) * 0.02) * CANVAS_HEIGHT;
    const r = stop.r * Math.max(CANVAS_WIDTH, CANVAS_HEIGHT);

    const gradient = ctx.createRadialGradient(cx, cy, 0, cx, cy, r);
    gradient.addColorStop(0, stop.color);
    gradient.addColorStop(1, 'transparent');

    ctx.fillStyle = gradient;
    ctx.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
  });
}

/**
 * Preload all frame images for a theme.
 * Returns array of loaded HTMLImageElement or empty array on failure.
 */
function preloadFrames(themeName) {
  const count = FRAME_COUNTS[themeName];
  if (!count) return Promise.resolve([]);

  const promises = [];
  for (let i = 1; i <= count; i++) {
    const num = String(i).padStart(4, '0');
    const src = `/frames/${themeName}/frame_${num}.jpg`;

    promises.push(
      new Promise((resolve) => {
        const img = new Image();
        img.crossOrigin = 'anonymous';
        img.onload = () => resolve(img);
        img.onerror = () => resolve(null); // Skip failed frames
        img.src = src;
      })
    );
  }

  return Promise.all(promises).then((imgs) => imgs.filter(Boolean));
}

export default function BackgroundPlane() {
  const meshRef = useRef();
  const currentKunj = useWorldStore((s) => s.currentKunj);
  const { camera } = useThree();

  // Frame state
  const [loadedFrames, setLoadedFrames] = useState([]);
  const frameIndexRef = useRef(0);
  const lastFrameTimeRef = useRef(0);
  const isLoadingRef = useRef(false);

  // Create the offscreen canvas and texture once
  const { offCanvas, offCtx, texture } = useMemo(() => {
    const canvas = document.createElement('canvas');
    canvas.width = CANVAS_WIDTH;
    canvas.height = CANVAS_HEIGHT;
    const ctx = canvas.getContext('2d');

    // Draw initial gradient so texture isn't blank
    drawGradient(ctx, 'sharad-moon', 0);

    const tex = new THREE.CanvasTexture(canvas);
    tex.minFilter = THREE.LinearFilter;
    tex.magFilter = THREE.LinearFilter;
    tex.colorSpace = THREE.SRGBColorSpace;
    tex.needsUpdate = true;

    return { offCanvas: canvas, offCtx: ctx, texture: tex };
  }, []);

  // Load frames when theme/kunj changes
  useEffect(() => {
    if (isLoadingRef.current) return;
    isLoadingRef.current = true;
    frameIndexRef.current = 0;

    // Show gradient immediately while frames load
    drawGradient(offCtx, currentKunj, 0);
    texture.needsUpdate = true;

    preloadFrames(currentKunj).then((frames) => {
      setLoadedFrames(frames);
      isLoadingRef.current = false;
      if (frames.length > 0) {
        console.log(`[BackgroundPlane] Loaded ${frames.length} frames for ${currentKunj}`);
      } else {
        console.log(`[BackgroundPlane] No frames for ${currentKunj}, using gradient`);
      }
    });
  }, [currentKunj, offCtx, texture]);

  // Animate: either frame sequence or gradient fallback
  useFrame((state) => {
    const now = state.clock.getElapsedTime() * 1000;

    if (loadedFrames.length > 0) {
      // ─── Frame Playback Mode ───
      if (now - lastFrameTimeRef.current >= FRAME_INTERVAL) {
        lastFrameTimeRef.current = now;
        frameIndexRef.current = (frameIndexRef.current + 1) % loadedFrames.length;

        const img = loadedFrames[frameIndexRef.current];
        if (img) {
          // Draw frame to fill the canvas, maintaining aspect ratio coverage
          const imgAspect = img.width / img.height;
          const canvasAspect = CANVAS_WIDTH / CANVAS_HEIGHT;

          let drawW, drawH, drawX, drawY;
          if (imgAspect > canvasAspect) {
            // Image is wider — fit height, crop sides
            drawH = CANVAS_HEIGHT;
            drawW = drawH * imgAspect;
            drawX = (CANVAS_WIDTH - drawW) / 2;
            drawY = 0;
          } else {
            // Image is taller — fit width, crop top/bottom
            drawW = CANVAS_WIDTH;
            drawH = drawW / imgAspect;
            drawX = 0;
            drawY = (CANVAS_HEIGHT - drawH) / 2;
          }

          offCtx.drawImage(img, drawX, drawY, drawW, drawH);
          texture.needsUpdate = true;
        }
      }
    } else {
      // ─── Gradient Fallback Mode ───
      // Throttle to every 3 frames for performance
      if (state.clock.elapsedTime % 0.05 < 0.017) {
        drawGradient(offCtx, currentKunj, now);
        texture.needsUpdate = true;
      }
    }
  });

  // Dynamically scale to fill frustum
  useFrame(() => {
    if (!meshRef.current) return;
    const dist = camera.position.z + 10;
    const vFov = (camera.fov * Math.PI) / 180;
    const height = 2 * Math.tan(vFov / 2) * dist;
    const width = height * (camera.aspect || 1);
    meshRef.current.scale.set(width * 1.2, height * 1.2, 1);
  });

  return (
    <mesh ref={meshRef} position={[0, 0, -10]} renderOrder={-1}>
      <planeGeometry args={[1, 1]} />
      <meshBasicMaterial
        map={texture}
        depthWrite={false}
        depthTest={false}
        toneMapped={false}
      />
    </mesh>
  );
}
