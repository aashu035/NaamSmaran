import { Suspense, useCallback } from 'react';
import { Canvas } from '@react-three/fiber';
import * as THREE from 'three';
import NikunjWorld from './BackgroundPlane';
import OrbitText from './OrbitText';
import ParticleSystem from './ParticleSystem';
import CameraRig from './CameraRig';
import { useWorldStore } from './useWorldStore';

/**
 * ThreeScene — Master R3F Canvas wrapper.
 * 
 * Composes all 3D world components into a single WebGL context:
 *   - NikunjWorld (True 3D environment: tree, meadow, lighting)
 *   - OrbitText (3 orbiting sacred name instances)
 *   - ParticleSystem (petals + sparkles + tap burst)
 *   - CameraRig (gyro parallax + mouse fallback)
 *
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// Canvas GL configuration — optimized for Android WebView
const GL_CONFIG = {
  antialias: true,
  alpha: false,
  powerPreference: 'default',
  preserveDrawingBuffer: false,
  failIfMajorPerformanceCaveat: false,
};

// Camera configuration — FOV matches refernece image depth
const CAMERA_CONFIG = {
  fov: 45,
  near: 0.1,
  far: 1000,
  position: [0, 1, 12],
};

export default function ThreeScene({ sacredName, themeColors }) {
  // Handle tap events for particle burst & world interaction
  const handlePointerDown = useCallback((event) => {
    if (event.point) {
      useWorldStore.getState().triggerTapBurst({
        x: event.point.x,
        y: event.point.y,
        z: event.point.z,
      });
    }
  }, []);

  return (
    <Canvas
      gl={GL_CONFIG}
      camera={CAMERA_CONFIG}
      dpr={[1, 2]}
      shadows
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        zIndex: 0,
        touchAction: 'none',
        background: '#070010'
      }}
      onPointerDown={handlePointerDown}
    >
      {/* Dynamic 3D Nikunj World */}
      <Suspense fallback={null}>
        <NikunjWorld />
      </Suspense>

      {/* Orbiting sacred text */}
      <Suspense fallback={null}>
        <OrbitText
          sacredName={sacredName || 'राधा'}
          color={themeColors?.primary || '#E8A0BF'}
        />
      </Suspense>

      {/* Particle systems */}
      <ParticleSystem
        petalColor={themeColors?.primary || '#E8A0BF'}
        goldColor={themeColors?.gold || '#F5CBA7'}
      />

      {/* Camera parallax controller */}
      <CameraRig />
    </Canvas>
  );
}

