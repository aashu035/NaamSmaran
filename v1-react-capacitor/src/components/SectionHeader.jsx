/**
 * SectionHeader — Top header for section overlay panels.
 * Shows section title (Hindi) + back arrow to return to Dashboard.
 * 
 * Props:
 *   title     — section name in Hindi
 *   onBack    — callback to close section / return to Dashboard
 *   subtitle  — optional secondary text (progress, etc.)
 */

export default function SectionHeader({ title, onBack, subtitle }) {
  return (
    <div
      className="section-header"
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 'var(--space-3)',
        padding: 'var(--space-4) var(--padding-section)',
        paddingTop: 'calc(var(--safe-top) + var(--space-4))',
      }}
    >
      {/* Back button */}
      <button
        className="tappable"
        onClick={onBack}
        aria-label="वापस"
        style={{
          background: 'var(--surface-glass)',
          border: '1px solid var(--border-glass)',
          borderRadius: 'var(--radius-circle)',
          width: 40,
          height: 40,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'var(--accent-primary)',
          fontSize: 'var(--text-medium)',
          flexShrink: 0,
        }}
      >
        ←
      </button>

      {/* Title + subtitle */}
      <div style={{ flex: 1, minWidth: 0 }}>
        <h2
          className="font-deco text-accent"
          style={{
            fontSize: 'var(--text-medium)',
            lineHeight: 'var(--leading-tight)',
          }}
        >
          {title}
        </h2>
        {subtitle && (
          <p
            className="font-ui text-tertiary"
            style={{
              fontSize: 'var(--text-tertiary)',
              marginTop: 'var(--space-1)',
            }}
          >
            {subtitle}
          </p>
        )}
      </div>
    </div>
  );
}
