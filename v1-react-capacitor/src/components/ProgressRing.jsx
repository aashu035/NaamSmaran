/**
 * ProgressRing — SVG circular progress indicator
 * Used on Dashboard for naam jap progress.
 * 
 * Props:
 *   progress  — 0 to 1 (clamped)
 *   size      — 'large' (140px) | 'medium' (80px)
 *   children  — center content (number, label, etc.)
 */

import { useMemo } from 'react';

const SIZES = {
  large: {
    dimension: 140,
    stroke: 8,
  },
  medium: {
    dimension: 80,
    stroke: 5,
  },
};

export default function ProgressRing({
  progress = 0,
  size = 'large',
  children,
  className = '',
}) {
  const config = SIZES[size] || SIZES.large;
  const { dimension, stroke } = config;
  const radius = (dimension - stroke) / 2;
  const circumference = 2 * Math.PI * radius;

  const clampedProgress = Math.max(0, Math.min(1, progress));
  const offset = circumference - clampedProgress * circumference;

  // Determine fill color based on progress
  const fillStyle = useMemo(() => {
    if (clampedProgress >= 1) return 'var(--state-exceeded)';
    if (clampedProgress > 0) return 'url(#ringGradient)';
    return 'var(--ring-track)';
  }, [clampedProgress]);

  return (
    <div
      className={`progress-ring${className ? ` ${className}` : ''}`}
      style={{
        position: 'relative',
        width: dimension,
        height: dimension,
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <svg
        width={dimension}
        height={dimension}
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          transform: 'rotate(-90deg)',
        }}
      >
        <defs>
          <linearGradient id="ringGradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="var(--accent-primary)" />
            <stop offset="100%" stopColor="var(--accent-secondary)" />
          </linearGradient>
        </defs>

        {/* Track */}
        <circle
          cx={dimension / 2}
          cy={dimension / 2}
          r={radius}
          fill="none"
          stroke="var(--ring-track)"
          strokeWidth={stroke}
        />

        {/* Fill */}
        <circle
          cx={dimension / 2}
          cy={dimension / 2}
          r={radius}
          fill="none"
          stroke={fillStyle}
          strokeWidth={stroke}
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          style={{
            transition: `stroke-dashoffset var(--duration-normal) var(--ease-out)`,
          }}
        />
      </svg>

      {/* Center content */}
      <div style={{ position: 'relative', textAlign: 'center' }}>
        {children}
      </div>
    </div>
  );
}
