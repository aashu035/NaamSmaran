/**
 * App.jsx — Root application shell.
 * Manages navigation state for all screens:
 *   - Dashboard (home)
 *   - 6 Sadhana sections (Layer 4)
 *   - Settings, ThemeSelector, Calendar, History, DayEndModal (Layer 5)
 * Renders WorldEngine as background layer.
 * 
 * Layer 6: Initializes Capacitor platform services on native.
 */

import { useState, useCallback, useEffect } from 'react';
import { Capacitor } from '@capacitor/core';
import { StatusBar, Style } from '@capacitor/status-bar';
import { App as CapApp } from '@capacitor/app';
import { ThemeProvider } from './components/ThemeProvider';
import WorldEngine from './engine/WorldEngine';
import BottomNav from './components/BottomNav';
import Splash from './screens/Splash';
import { useAppStore } from './store/useAppStore';

// Platform services (Layer 6)
import { createNotificationChannels, requestNotificationPermission } from './services/notifications';
import { updateWidgetData } from './services/widget';

// Layer 4 — Core screens
import Dashboard from './screens/Dashboard';
import NaamJap from './screens/sections/NaamJap';
import HitChaturasi from './screens/sections/HitChaturasi';
import MaansikSeva from './screens/sections/MaansikSeva';
import Prarthana from './screens/sections/Prarthana';
import BhaktMaal from './screens/sections/BhaktMaal';
import Charnamrit from './screens/sections/Charnamrit';

// Layer 5 — Secondary screens
import Calendar from './screens/Calendar';
import History from './screens/History';
import Settings from './screens/Settings';
import ThemeSelector from './screens/ThemeSelector';
import DayEndModal from './screens/DayEndModal';

function AppContent() {
  const [activeSection, setActiveSection] = useState('dashboard');
  const [showDayEndModal, setShowDayEndModal] = useState(false);
  const [hasSeenSplash, setHasSeenSplash] = useState(false);

  // ─── Layer 6: Platform Initialization ───
  useEffect(() => {
    async function initPlatform() {
      if (!Capacitor.isNativePlatform()) return;

      // Status bar: dark content, deep indigo background
      try {
        await StatusBar.setStyle({ style: Style.Dark });
        await StatusBar.setBackgroundColor({ color: '#070010' });
      } catch (err) {
        console.warn('[Platform] StatusBar init failed:', err);
      }

      // Notification channels (required for Android 8+)
      await createNotificationChannels();
      await requestNotificationPermission();
    }

    initPlatform();

    // Android hardware back button
    const backHandler = CapApp.addListener('backButton', () => {
      if (activeSection !== 'dashboard') {
        setActiveSection('dashboard');
      } else {
        CapApp.exitApp();
      }
    });

    // Handle App resume to refresh the date/time and daily record
    const appStateHandler = CapApp.addListener('appStateChange', ({ isActive }) => {
      if (isActive) {
        // App came to foreground, check if day boundary crossed
        useAppStore.getState().loadTodayRecord();
      }
    });

    return () => {
      backHandler.then(h => h.remove());
      appStateHandler.then(h => h.remove());
    };
  }, [activeSection]);

  const handleNavigate = useCallback((sectionId) => {
    setActiveSection(sectionId);
  }, []);

  const handleBack = useCallback(() => {
    setActiveSection('dashboard');
  }, []);

  // Calendar day selection → opens History with that date
  const handleSelectDay = useCallback((dateStr) => {
    setActiveSection('history');
  }, []);

  // Render active section
  const renderSection = () => {
    switch (activeSection) {
      // Layer 4 — Sadhana sections
      case 'naam-jap':
        return <NaamJap onBack={handleBack} />;
      case 'chaturasi':
        return <HitChaturasi onBack={handleBack} />;
      case 'maansik-seva':
        return <MaansikSeva onBack={handleBack} />;
      case 'prarthana':
        return <Prarthana onBack={handleBack} />;
      case 'bhakt-maal':
        return <BhaktMaal onBack={handleBack} />;
      case 'charnamrit':
        return <Charnamrit onBack={handleBack} />;

      // Layer 5 — Secondary screens
      case 'calendar':
        return <Calendar onBack={handleBack} onSelectDay={handleSelectDay} />;
      case 'history':
        return <History onBack={handleBack} />;
      case 'settings':
        return (
          <Settings
            onBack={handleBack}
            onOpenThemeSelector={() => setActiveSection('theme-selector')}
          />
        );
      case 'theme-selector':
        return <ThemeSelector onBack={() => setActiveSection('settings')} />;

      // Default — Dashboard
      default:
        return (
          <Dashboard
            onNavigate={handleNavigate}
            onOpenCalendar={() => setActiveSection('calendar')}
            onOpenHistory={() => setActiveSection('history')}
            onOpenSettings={() => setActiveSection('settings')}
            onShowDayEnd={() => setShowDayEndModal(true)}
          />
        );
    }
  };

  // Hide bottom nav on overlay screens
  const hideNav = activeSection === 'theme-selector';

  if (!hasSeenSplash) {
    return (
      <div className="app-content" style={{ position: 'relative', zIndex: 1 }}>
        <Splash onComplete={() => setHasSeenSplash(true)} />
      </div>
    );
  }

  return (
    <>
      <div className="app-content" style={{ position: 'relative', zIndex: 1 }}>
        {renderSection()}
      </div>
      {!hideNav && (
        <BottomNav activeSection={activeSection} onNavigate={handleNavigate} />
      )}
      {showDayEndModal && (
        <DayEndModal onClose={() => setShowDayEndModal(false)} />
      )}
    </>
  );
}

function App() {
  return (
    <ThemeProvider>
      <WorldEngine />
      <AppContent />
    </ThemeProvider>
  );
}

export default App;
