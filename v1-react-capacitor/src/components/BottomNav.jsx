/**
 * BottomNav — Fixed bottom navigation bar.
 * 7 sections: Dashboard + 6 sadhana items from AGENTS.md §4.
 * Uses nav state tokens from tokens.css (Addition 3).
 * 
 * Props:
 *   activeSection  — current active section ID
 *   onNavigate     — callback(sectionId)
 */

const NAV_ITEMS = [
  { id: 'dashboard',    icon: '🏠', label: 'होम' },
  { id: 'naam-jap',     icon: '📿', label: 'नाम जप' },
  { id: 'chaturasi',    icon: '📖', label: 'चौरासी' },
  { id: 'maansik-seva',  icon: '🙏', label: 'सेवा' },
  { id: 'prarthana',    icon: '🙏', label: 'प्रार्थना' },
  { id: 'bhakt-maal',   icon: '📚', label: 'भक्तमाल' },
  { id: 'charnamrit',   icon: '🙏', label: 'चरणामृत' },
];

export default function BottomNav({ activeSection = 'dashboard', onNavigate }) {
  return (
    <nav
      className="bottom-nav"
      style={{
        position: 'fixed',
        bottom: 0,
        left: 0,
        right: 0,
        zIndex: 'var(--z-bottom-nav)',
        height: 'var(--bottom-nav-height)',
        paddingBottom: 'var(--safe-area-bottom)',
        background: 'rgba(7, 0, 16, 0.85)',
        backdropFilter: 'var(--glass-blur)',
        WebkitBackdropFilter: 'var(--glass-blur)',
        borderTop: '1px solid var(--border-glass)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-around',
      }}
    >
      {NAV_ITEMS.map((item) => {
        const isActive = activeSection === item.id;
        return (
          <button
            key={item.id}
            className="tappable"
            onClick={() => onNavigate(item.id)}
            aria-label={item.label}
            aria-current={isActive ? 'page' : undefined}
            style={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              gap: '2px',
              background: 'none',
              border: 'none',
              padding: 'var(--space-1) var(--space-2)',
              minWidth: 0,
              flex: 1,
            }}
          >
            <span
              style={{
                fontSize: '20px',
                opacity: isActive ? 1 : 0.45,
                transition: 'opacity var(--duration-fast) var(--ease-out)',
              }}
            >
              {item.icon}
            </span>
            <span
              className="font-ui"
              style={{
                fontSize: 'var(--text-label)',
                color: isActive ? 'var(--nav-label-active)' : 'var(--nav-label-inactive)',
                transition: 'color var(--duration-fast) var(--ease-out)',
                letterSpacing: 0, /* Devanagari — no tracking */
                textTransform: 'none',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                maxWidth: '48px',
              }}
            >
              {item.label}
            </span>
          </button>
        );
      })}
    </nav>
  );
}
