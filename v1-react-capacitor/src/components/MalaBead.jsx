/**
 * MalaBead — 108-bead circular japa mala visualization.
 * Each tap = +1 bead. 108 beads = 1 complete mala.
 * Sumeru bead (109th position) at the top triggers celebration.
 *
 * Props:
 *   currentBead   — 0–108, current position in this mala
 *   totalMalas     — number of complete malas today
 *   totalCount     — total jap count today (from all input modes)
 *   onTap          — callback when user taps to advance a bead
 */

import { useMemo } from 'react';

const TOTAL_BEADS = 108;
const SVG_SIZE = 300;
const CENTER = SVG_SIZE / 2;
const RADIUS = 120;
const BEAD_RADIUS = 5;
const SUMERU_RADIUS = 8;

export default function MalaBead({
  currentBead = 0,
  totalMalas = 0,
  totalCount = 0,
  onTap,
}) {
  // Pre-compute bead positions around the circle
  const beadPositions = useMemo(() => {
    const positions = [];
    for (let i = 0; i < TOTAL_BEADS; i++) {
      // Start from top (12 o'clock), go clockwise
      // Leave a gap at the top for the sumeru bead
      const angle = ((i + 1) / (TOTAL_BEADS + 1)) * 2 * Math.PI - Math.PI / 2;
      positions.push({
        x: CENTER + RADIUS * Math.cos(angle),
        y: CENTER + RADIUS * Math.sin(angle),
        done: i < currentBead,
      });
    }
    return positions;
  }, [currentBead]);

  // Sumeru bead at 12 o'clock (top center)
  const sumeruX = CENTER;
  const sumeruY = CENTER - RADIUS;

  const remaining = TOTAL_BEADS - currentBead;

  return (
    <div
      className="mala-container"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 'var(--space-4)',
      }}
    >
      {/* Tap area wrapping the SVG mala */}
      <div
        className="tappable"
        onClick={onTap}
        role="button"
        aria-label={`माला पर टैप करें — शेष ${remaining}`}
        style={{
          position: 'relative',
          width: SVG_SIZE,
          height: SVG_SIZE,
          cursor: 'pointer',
        }}
      >
        <svg
          width={SVG_SIZE}
          height={SVG_SIZE}
          viewBox={`0 0 ${SVG_SIZE} ${SVG_SIZE}`}
        >
          {/* Thread circle (behind beads) */}
          <circle
            cx={CENTER}
            cy={CENTER}
            r={RADIUS}
            fill="none"
            stroke="var(--mala-bead-thread)"
            strokeWidth={1.5}
          />

          {/* 108 beads */}
          {beadPositions.map((bead, i) => (
            <circle
              key={i}
              cx={bead.x}
              cy={bead.y}
              r={BEAD_RADIUS}
              fill={bead.done ? 'var(--mala-bead-done)' : 'var(--mala-bead-normal)'}
              style={{
                transition: `fill var(--duration-bead) var(--ease-out)`,
              }}
            />
          ))}

          {/* Sumeru bead — larger, golden */}
          <circle
            cx={sumeruX}
            cy={sumeruY}
            r={SUMERU_RADIUS}
            fill={currentBead >= TOTAL_BEADS ? 'var(--mala-bead-sumeru)' : 'var(--mala-bead-normal)'}
            stroke={currentBead >= TOTAL_BEADS ? 'var(--accent-gold)' : 'var(--mala-bead-thread)'}
            strokeWidth={1.5}
            style={{
              transition: `fill var(--duration-mala) var(--ease-out)`,
            }}
          />
        </svg>

        {/* Center text — mala count & remaining */}
        <div
          style={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            textAlign: 'center',
            pointerEvents: 'none',
          }}
        >
          <p
            className="font-ui text-accent"
            style={{
              fontSize: 'var(--text-secondary)',
              lineHeight: 'var(--leading-tight)',
            }}
          >
            माला: {totalMalas}
          </p>
          <p
            className="font-ui text-tertiary"
            style={{
              fontSize: 'var(--text-tertiary)',
              marginTop: 'var(--space-1)',
            }}
          >
            शेष: {remaining}
          </p>
          <p
            className="font-numbers text-gradient-numbers font-bold"
            style={{
              fontSize: 'var(--text-large)',
              marginTop: 'var(--space-2)',
            }}
          >
            {totalCount.toLocaleString('en-IN')}
          </p>
        </div>
      </div>
    </div>
  );
}
