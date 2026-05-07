import { useRef, useMemo, useCallback } from 'react';
import { useFrame } from '@react-three/fiber';
import * as THREE from 'three';
import { useWorldStore } from './useWorldStore';
import { createDotTexture } from './dotTexture';

/**
 * ParticleSystem — Three particle subsystems in one component.
 * 
 * 1. Petals:   30 particles, accent-primary, slow drift downward
 * 2. Sparkles: 60 particles, white/accent-gold, random Brownian scatter  
 * 3. Tap burst: 10 particles, gold, radial outward burst (on-demand)
 * 
 * All use THREE.Points + BufferGeometry for minimal draw calls.
 * Positions updated in useFrame — zero React re-renders.
 * 
 * Performance budget: ≤ 3 draw calls total for all particles.
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

const PETAL_COUNT = 30;
const SPARKLE_COUNT = 60;
const BURST_COUNT = 10;

// Spatial bounds — calibrated for camera at z=8, FOV 60
const BOUNDS = { x: 6, y: 5, z: 4 };

/**
 * Initialize random positions within bounds.
 */
function createPositions(count, boundsMultiplier = 1) {
  const positions = new Float32Array(count * 3);
  for (let i = 0; i < count; i++) {
    positions[i * 3]     = (Math.random() - 0.5) * BOUNDS.x * 2 * boundsMultiplier;
    positions[i * 3 + 1] = (Math.random() - 0.5) * BOUNDS.y * 2 * boundsMultiplier;
    positions[i * 3 + 2] = (Math.random() - 0.5) * BOUNDS.z * 2 * boundsMultiplier;
  }
  return positions;
}

// ─── Petals ─────────────────────────────────────────────────

function PetalParticles({ color }) {
  const pointsRef = useRef();
  const dotMap = useMemo(() => createDotTexture(), []);
  const { positions, velocities, phases } = useMemo(() => {
    const pos = createPositions(PETAL_COUNT);
    const vel = new Float32Array(PETAL_COUNT);
    const ph = new Float32Array(PETAL_COUNT);
    for (let i = 0; i < PETAL_COUNT; i++) {
      vel[i] = 0.005 + Math.random() * 0.01;   // fall speed
      ph[i] = Math.random() * Math.PI * 2;       // sway phase
    }
    return { positions: pos, velocities: vel, phases: ph };
  }, []);

  useFrame((state) => {
    if (!pointsRef.current) return;
    const posArr = pointsRef.current.geometry.attributes.position.array;
    const time = state.clock.getElapsedTime();
    const intensity = useWorldStore.getState().particleIntensity;

    for (let i = 0; i < PETAL_COUNT; i++) {
      const i3 = i * 3;
      // Drift downward
      posArr[i3 + 1] -= velocities[i] * intensity;
      // Horizontal sway
      posArr[i3] += Math.sin(time * 0.5 + phases[i]) * 0.003;

      // Respawn at top when below bounds
      if (posArr[i3 + 1] < -BOUNDS.y) {
        posArr[i3]     = (Math.random() - 0.5) * BOUNDS.x * 2;
        posArr[i3 + 1] = BOUNDS.y + Math.random() * 2;
        posArr[i3 + 2] = (Math.random() - 0.5) * BOUNDS.z * 2;
      }
    }
    pointsRef.current.geometry.attributes.position.needsUpdate = true;
  });

  return (
    <points ref={pointsRef}>
      <bufferGeometry>
        <bufferAttribute
          attach="attributes-position"
          count={PETAL_COUNT}
          array={positions}
          itemSize={3}
        />
      </bufferGeometry>
      <pointsMaterial
        color={color}
        size={0.08}
        sizeAttenuation
        transparent
        opacity={0.5}
        depthWrite={false}
        blending={THREE.AdditiveBlending}
        map={dotMap}
      />
    </points>
  );
}

// ─── Sparkles ───────────────────────────────────────────────

function SparkleParticles({ color }) {
  const pointsRef = useRef();
  const dotMap = useMemo(() => createDotTexture(), []);
  const { positions, phases, speeds } = useMemo(() => {
    const pos = createPositions(SPARKLE_COUNT, 1.2);
    const ph = new Float32Array(SPARKLE_COUNT);
    const sp = new Float32Array(SPARKLE_COUNT);
    for (let i = 0; i < SPARKLE_COUNT; i++) {
      ph[i] = Math.random() * Math.PI * 2;
      sp[i] = 0.5 + Math.random() * 2;
    }
    return { positions: pos, phases: ph, speeds: sp };
  }, []);

  useFrame((state) => {
    if (!pointsRef.current) return;
    const posArr = pointsRef.current.geometry.attributes.position.array;
    const time = state.clock.getElapsedTime();
    const intensity = useWorldStore.getState().particleIntensity;

    // Pulse opacity via material
    const mat = pointsRef.current.material;
    mat.opacity = 0.15 + Math.sin(time * 0.8) * 0.1 * intensity;

    for (let i = 0; i < SPARKLE_COUNT; i++) {
      const i3 = i * 3;
      // Slow Brownian drift
      posArr[i3]     += Math.sin(time * speeds[i] + phases[i]) * 0.002;
      posArr[i3 + 1] += Math.cos(time * speeds[i] * 0.7 + phases[i]) * 0.001;
      posArr[i3 + 2] += Math.sin(time * speeds[i] * 0.5 + phases[i] * 1.3) * 0.001;
    }
    pointsRef.current.geometry.attributes.position.needsUpdate = true;
  });

  return (
    <points ref={pointsRef}>
      <bufferGeometry>
        <bufferAttribute
          attach="attributes-position"
          count={SPARKLE_COUNT}
          array={positions}
          itemSize={3}
        />
      </bufferGeometry>
      <pointsMaterial
        color={color}
        size={0.04}
        sizeAttenuation
        transparent
        opacity={0.3}
        depthWrite={false}
        blending={THREE.AdditiveBlending}
        map={dotMap}
      />
    </points>
  );
}

// ─── Tap Burst ──────────────────────────────────────────────

function TapBurstParticles({ color }) {
  const pointsRef = useRef();
  const burstTimeRef = useRef(0);
  const activeRef = useRef(false);

  const { positions, velocities } = useMemo(() => {
    const pos = new Float32Array(BURST_COUNT * 3);
    const vel = new Float32Array(BURST_COUNT * 3);
    return { positions: pos, velocities: vel };
  }, []);

  // Initialize burst from origin point
  const initBurst = useCallback((origin) => {
    for (let i = 0; i < BURST_COUNT; i++) {
      const i3 = i * 3;
      // Start at origin
      positions[i3]     = origin.x;
      positions[i3 + 1] = origin.y;
      positions[i3 + 2] = origin.z;
      // Random outward velocity
      const theta = Math.random() * Math.PI * 2;
      const phi = Math.random() * Math.PI;
      const speed = 0.03 + Math.random() * 0.05;
      velocities[i3]     = Math.sin(phi) * Math.cos(theta) * speed;
      velocities[i3 + 1] = Math.sin(phi) * Math.sin(theta) * speed + 0.02; // upward bias
      velocities[i3 + 2] = Math.cos(phi) * speed;
    }
    burstTimeRef.current = 0;
    activeRef.current = true;
  }, [positions, velocities]);

  useFrame((state, delta) => {
    if (!pointsRef.current) return;

    // Check for new burst trigger
    const { tapBurstActive, tapBurstPosition } = useWorldStore.getState();
    if (tapBurstActive && !activeRef.current && tapBurstPosition) {
      initBurst(tapBurstPosition);
    }

    if (!activeRef.current) {
      pointsRef.current.visible = false;
      return;
    }

    pointsRef.current.visible = true;
    burstTimeRef.current += delta * 1000;

    const posArr = pointsRef.current.geometry.attributes.position.array;
    const progress = burstTimeRef.current / 600; // 600ms burst duration

    for (let i = 0; i < BURST_COUNT; i++) {
      const i3 = i * 3;
      posArr[i3]     += velocities[i3];
      posArr[i3 + 1] += velocities[i3 + 1] - 0.001; // gravity
      posArr[i3 + 2] += velocities[i3 + 2];
    }
    pointsRef.current.geometry.attributes.position.needsUpdate = true;

    // Fade out
    pointsRef.current.material.opacity = Math.max(0, 1 - progress);

    // Deactivate when complete
    if (progress >= 1) {
      activeRef.current = false;
    }
  });

  return (
    <points ref={pointsRef} visible={false}>
      <bufferGeometry>
        <bufferAttribute
          attach="attributes-position"
          count={BURST_COUNT}
          array={positions}
          itemSize={3}
        />
      </bufferGeometry>
      <pointsMaterial
        color={color}
        size={0.12}
        sizeAttenuation
        transparent
        opacity={1}
        depthWrite={false}
        blending={THREE.AdditiveBlending}
        map={createDotTexture()}
      />
    </points>
  );
}

// ─── Combined System ────────────────────────────────────────

export default function ParticleSystem({ petalColor = '#E8A0BF', goldColor = '#F5CBA7' }) {
  return (
    <group>
      <PetalParticles color={petalColor} />
      <SparkleParticles color={'#FFFFFF'} />
      <TapBurstParticles color={goldColor} />
    </group>
  );
}
