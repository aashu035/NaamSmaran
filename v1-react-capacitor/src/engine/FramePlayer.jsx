import { useState, useEffect } from 'react';

const NUM_FRAMES = 17; // splash_00.jpg to splash_16.jpg

export default function FramePlayer() {
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    // 6 seconds per frame to allow for a smooth, slow slideshow (showreel)
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % NUM_FRAMES);
    }, 6000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div
      style={{
        position: 'absolute',
        inset: 0,
        width: '100%',
        height: '100%',
        overflow: 'hidden',
        background: '#000',
      }}
    >
      {Array.from({ length: NUM_FRAMES }).map((_, idx) => {
        const imageName = `splash_${idx.toString().padStart(2, '0')}.jpg`;
        return (
          <div
            key={idx}
            style={{
              position: 'absolute',
              inset: 0,
              width: '100%',
              height: '100%',
              backgroundImage: `url(/splash/${imageName})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              opacity: idx === currentIndex ? 0.35 : 0, // Lower opacity to make the UI pop out clearly
              transition: 'opacity 2.5s ease-in-out',
            }}
          />
        );
      })}
    </div>
  );
}
