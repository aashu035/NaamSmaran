import { useRef, useEffect, useCallback } from 'react';
import { useFrame, useThree } from '@react-three/fiber';
import { useWorldStore } from './useWorldStore';

/**
 * CameraRig — Gyroscope parallax + mouse/touch fallback.
 * 
 * Per AGENTS.md §8:
 * - Phone tilt → subtle camera parallax (±5° max offset)
 * - Mouse fallback for desktop testing
 * - Damping: lerp(current, target, 0.05) per frame
 * - Double-tap → reset camera to default position over 280ms
 * 
 * Camera base position: [0, 0, 8], FOV: 60
 * Parallax is applied as position.x/y shifts, not rotation.
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

const BASE_POSITION = { x: 0, y: 0, z: 8 };
const MAX_OFFSET = 0.5;       // Max camera position shift in world units
const DAMPING = 0.05;          // Lerp factor per frame — smooth motion
const DOUBLE_TAP_TIMEOUT = 300; // ms between taps for double-tap detection

// Simple lerp utility
function lerp(current, target, factor) {
  return current + (target - current) * factor;
}

export default function CameraRig() {
  const { camera, gl } = useThree();
  const targetOffset = useRef({ x: 0, y: 0 });
  const currentOffset = useRef({ x: 0, y: 0 });
  const lastTapTime = useRef(0);
  const isResetting = useRef(false);
  const resetStartTime = useRef(0);
  const resetFrom = useRef({ x: 0, y: 0 });

  // ─── Gyroscope setup ─────────────────────────────────────

  useEffect(() => {
    let gyroActive = false;

    const handleOrientation = (event) => {
      if (!gyroActive) {
        gyroActive = true;
        useWorldStore.getState().setGyroEnabled(true);
      }

      // beta: front-back tilt (-180..180), gamma: left-right tilt (-90..90)
      const beta = event.beta || 0;
      const gamma = event.gamma || 0;

      // Normalize to ±5° range, map to ±MAX_OFFSET
      const normalizedX = Math.max(-5, Math.min(5, gamma)) / 5;
      const normalizedY = Math.max(-5, Math.min(5, beta - 45)) / 5; // 45° = holding phone naturally

      targetOffset.current = {
        x: normalizedX * MAX_OFFSET,
        y: -normalizedY * MAX_OFFSET, // Invert Y so tilting up moves camera up
      };
    };

    // Try to request permission (iOS 13+)
    if (typeof DeviceOrientationEvent !== 'undefined' &&
        typeof DeviceOrientationEvent.requestPermission === 'function') {
      // iOS — permission must be triggered by user gesture (handled in UI layer)
      // For now, just add the listener and it will work on Android
    }

    window.addEventListener('deviceorientation', handleOrientation, { passive: true });

    return () => {
      window.removeEventListener('deviceorientation', handleOrientation);
    };
  }, []);

  // ─── Mouse fallback (desktop) ─────────────────────────────

  useEffect(() => {
    const handleMouseMove = (event) => {
      // Only use mouse if gyro is not active
      if (useWorldStore.getState().gyroEnabled) return;

      const cx = window.innerWidth / 2;
      const cy = window.innerHeight / 2;

      // Map mouse position to ±MAX_OFFSET
      const normalizedX = (event.clientX - cx) / cx; // -1..1
      const normalizedY = (event.clientY - cy) / cy; // -1..1

      targetOffset.current = {
        x: normalizedX * MAX_OFFSET,
        y: -normalizedY * MAX_OFFSET,
      };
    };

    window.addEventListener('mousemove', handleMouseMove, { passive: true });

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
    };
  }, []);

  // ─── Double-tap reset ─────────────────────────────────────

  useEffect(() => {
    const canvas = gl.domElement;

    const handleDoubleTap = () => {
      const now = Date.now();
      if (now - lastTapTime.current < DOUBLE_TAP_TIMEOUT) {
        // Double-tap detected — reset camera
        isResetting.current = true;
        resetStartTime.current = now;
        resetFrom.current = { ...currentOffset.current };
        targetOffset.current = { x: 0, y: 0 };
      }
      lastTapTime.current = now;
    };

    canvas.addEventListener('touchend', handleDoubleTap, { passive: true });
    canvas.addEventListener('dblclick', () => {
      isResetting.current = true;
      resetStartTime.current = Date.now();
      resetFrom.current = { ...currentOffset.current };
      targetOffset.current = { x: 0, y: 0 };
    });

    return () => {
      canvas.removeEventListener('touchend', handleDoubleTap);
    };
  }, [gl]);

  // ─── Per-frame camera update ──────────────────────────────

  useFrame(() => {
    if (isResetting.current) {
      // Animate reset over 280ms (--duration-normal)
      const elapsed = Date.now() - resetStartTime.current;
      const progress = Math.min(1, elapsed / 280);
      // Ease-out cubic-bezier approximation
      const eased = 1 - Math.pow(1 - progress, 3);

      currentOffset.current.x = lerp(resetFrom.current.x, 0, eased);
      currentOffset.current.y = lerp(resetFrom.current.y, 0, eased);

      if (progress >= 1) {
        isResetting.current = false;
      }
    } else {
      // Normal damped follow
      currentOffset.current.x = lerp(currentOffset.current.x, targetOffset.current.x, DAMPING);
      currentOffset.current.y = lerp(currentOffset.current.y, targetOffset.current.y, DAMPING);
    }

    // Apply offset to camera position
    camera.position.x = BASE_POSITION.x + currentOffset.current.x;
    camera.position.y = BASE_POSITION.y + currentOffset.current.y;
    camera.position.z = BASE_POSITION.z;
  });

  // This component renders nothing — it only controls the camera
  return null;
}
