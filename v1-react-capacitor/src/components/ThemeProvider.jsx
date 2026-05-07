import { createContext, useContext, useState, useEffect } from 'react';

/**
 * Theme system for Naam Smaran.
 * 
 * Usage in any component:
 *   import { useTheme } from '../components/ThemeProvider';
 *   const { theme, setTheme } = useTheme();
 */

// localStorage key — must match comment in tokens.css
const STORAGE_KEY = 'naamjap_theme';
const DEFAULT_THEME = 'sharad-moon';

// Theme → Android status bar hex mapping
// Matches --bg-primary values from each theme file
const STATUS_BAR_COLORS = {
  'sharad-moon':    '#070010',
  'vrindavan-dawn': '#0A0408',
  'nikunj':         '#030A04',
  'van-vihaar':     '#040806',
  'shayan':         '#020209',
};

const VALID_THEMES = Object.keys(STATUS_BAR_COLORS);

// Exported context — available for advanced use cases
export const ThemeContext = createContext(null);

// Convenience hook — the primary API for components
export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }
  return context;
}

/**
 * Applies the theme to the DOM:
 * 1. Sets data-theme attribute on <html>
 * 2. Updates meta[name="theme-color"] for Android status bar
 */
function applyThemeToDom(theme) {
  document.documentElement.setAttribute('data-theme', theme);

  // Safe meta update — null check for Capacitor WebView timing edge case
  const metaThemeColor = document.querySelector('meta[name="theme-color"]');
  if (metaThemeColor) {
    metaThemeColor.setAttribute('content', STATUS_BAR_COLORS[theme] || STATUS_BAR_COLORS[DEFAULT_THEME]);
  }
}

export function ThemeProvider({ children }) {
  const [theme, setThemeState] = useState(() => {
    // Read persisted theme on first render
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored && VALID_THEMES.includes(stored)) {
        return stored;
      }
    } catch {
      // localStorage may be unavailable in some WebView contexts
    }
    return DEFAULT_THEME;
  });

  // Apply theme to DOM on mount and on every change
  useEffect(() => {
    applyThemeToDom(theme);
  }, [theme]);

  // Public setter — validates, persists, and updates state
  function setTheme(newTheme) {
    if (!VALID_THEMES.includes(newTheme)) {
      console.warn(`[ThemeProvider] Invalid theme: "${newTheme}". Valid themes: ${VALID_THEMES.join(', ')}`);
      return;
    }

    try {
      localStorage.setItem(STORAGE_KEY, newTheme);
    } catch {
      // Silently fail — theme will still work for this session
    }

    setThemeState(newTheme);
  }

  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}
