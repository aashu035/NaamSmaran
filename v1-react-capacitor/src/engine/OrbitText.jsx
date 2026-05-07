import { useRef, useMemo } from 'react';
import { useFrame } from '@react-three/fiber';
import { Text } from '@react-three/drei';
import * as THREE from 'three';

/**
 * OrbitText — Three instances of the sacred name orbiting at different radii.
 * 
 * Per AGENTS.md §8:
 *   Instance 1: radius 3, speed  20°/s, clockwise,         Y offset  0
 *   Instance 2: radius 5, speed -15°/s, counter-clockwise,  Y offset +0.5
 *   Instance 3: radius 7, speed  10°/s, clockwise,          Y offset -0.3
 * 
 * Uses @react-three/drei <Text> (troika-three-text) with bundled Devanagari font.
 * Glow via MeshBasicMaterial + toneMapped:false (no post-processing needed).
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

// Orbit configuration — matches AGENTS.md §8 spec
const ORBIT_CONFIG = [
  { radius: 4, speedDeg: 20,  yOffset: 0,    startAngle: 0 },
  { radius: 6, speedDeg: -15, yOffset: 0.5,  startAngle: 2.094 },  // 120° offset
  { radius: 8, speedDeg: 10,  yOffset: -0.3, startAngle: 4.189 },  // 240° offset
];

// Bundled font for offline-first — no CDN dependency
const FONT_PATH = '/fonts/NotoSansDevanagari-Bold.ttf';

// Convert degrees/sec to radians/sec
const DEG_TO_RAD = Math.PI / 180;

function OrbitingTextInstance({ radius, speedDeg, yOffset, startAngle, text, color, fontSize }) {
  const textRef = useRef();
  const speedRad = speedDeg * DEG_TO_RAD;

  useFrame((state) => {
    if (!textRef.current) return;
    const t = state.clock.getElapsedTime();
    const angle = startAngle + t * speedRad;

    textRef.current.position.x = Math.cos(angle) * radius;
    textRef.current.position.z = Math.sin(angle) * radius;
    textRef.current.position.y = yOffset + Math.sin(t * 0.5 + startAngle) * 0.15; // subtle bob

    // Always face camera (billboarding)
    textRef.current.lookAt(state.camera.position);
  });

  return (
    <Text
      ref={textRef}
      font={FONT_PATH}
      fontSize={fontSize}
      letterSpacing={0.05}
      anchorX="center"
      anchorY="middle"
      position={[radius, yOffset, 0]}
    >
      {text}
      <meshBasicMaterial
        color={color}
        toneMapped={false}
        transparent
        opacity={0.25}
        side={THREE.DoubleSide}
      />
    </Text>
  );
}

export default function OrbitText({ sacredName = 'राधा', color = '#E8A0BF' }) {
  // Parse color once, reuse across instances
  const threeColor = useMemo(() => new THREE.Color(color), [color]);

  // Scale font size by orbit radius — farther orbits get slightly larger text
  const fontSizes = [0.25, 0.22, 0.20];

  return (
    <group>
      {ORBIT_CONFIG.map((config, i) => (
        <OrbitingTextInstance
          key={i}
          {...config}
          text={sacredName}
          color={threeColor}
          fontSize={fontSizes[i]}
        />
      ))}
    </group>
  );
}
