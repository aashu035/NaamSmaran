import React, { useEffect, useState } from 'react';
import { useWorldStore } from '../engine/useWorldStore';
import { useAppStore } from '../store/useAppStore';
import './Splash.css';

const IMAGES = Array.from({ length: 17 }, (_, i) => `/splash/splash_${i.toString().padStart(2, '0')}.jpg`);

export default function Splash({ onComplete }) {
  const { activeTheme } = useAppStore();
  const transitionToKunj = useWorldStore((s) => s.transitionToKunj);
  const [fadingOut, setFadingOut] = useState(false);
  const [imgIndex, setImgIndex] = useState(0);

  useEffect(() => {
    // Force splash theme immediately
    useWorldStore.setState({ currentKunj: 'splash' });

    // Showreel rapid flash interval (500ms per image)
    const interval = setInterval(() => {
      setImgIndex((prev) => {
        if (prev < IMAGES.length - 1) return prev + 1;
        // Stop at the last image instead of looping, so it settles before fadeout
        return prev;
      });
    }, 500);

    // Let the splash play for 8.5 seconds
    const timer = setTimeout(() => {
      setFadingOut(true);
      
      // Start transitioning the background to the actual theme
      transitionToKunj(activeTheme || 'sharad-moon');

      // Unmount after CSS fade-out (800ms)
      setTimeout(() => {
        onComplete();
      }, 800);
    }, 8500);

    return () => {
      clearInterval(interval);
      clearTimeout(timer);
    };
  }, [activeTheme, transitionToKunj, onComplete]);

  return (
    <div className={`splash-screen ${fadingOut ? 'fading-out' : ''}`}>
      {/* Showreel Background */}
      <div className="showreel-container">
        {IMAGES.map((src, i) => (
          <div 
            key={src}
            className={`showreel-image ${i === imgIndex ? 'active' : ''}`}
            style={{ backgroundImage: `url(${src})` }}
          />
        ))}
        <div className="showreel-overlay" />
      </div>

      <div className="splash-content">
        <h1 className="splash-title">राधावल्लभ श्री हरिवंश</h1>
        <div className="splash-subtitle">नाम स्मरण</div>
      </div>
    </div>
  );
}
