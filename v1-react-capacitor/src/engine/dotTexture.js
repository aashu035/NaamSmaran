import * as THREE from 'three';

/**
 * createDotTexture — Generates a soft, circular dot texture for particles.
 * 
 * Uses an offscreen canvas to draw a radial gradient circle,
 * creating a soft-edged round particle instead of the default square.
 * 
 * Cached as a singleton — only created once for the entire app.
 * 
 * श्री राधावल्लभ लाल जु की जय 🙏
 */

let _cachedTexture = null;

export function createDotTexture(size = 64) {
  if (_cachedTexture) return _cachedTexture;

  const canvas = document.createElement('canvas');
  canvas.width = size;
  canvas.height = size;
  const ctx = canvas.getContext('2d');

  const half = size / 2;
  const gradient = ctx.createRadialGradient(half, half, 0, half, half, half);
  gradient.addColorStop(0, 'rgba(255, 255, 255, 1)');
  gradient.addColorStop(0.4, 'rgba(255, 255, 255, 0.8)');
  gradient.addColorStop(0.7, 'rgba(255, 255, 255, 0.2)');
  gradient.addColorStop(1, 'rgba(255, 255, 255, 0)');

  ctx.fillStyle = gradient;
  ctx.fillRect(0, 0, size, size);

  const texture = new THREE.CanvasTexture(canvas);
  texture.needsUpdate = true;

  _cachedTexture = texture;
  return texture;
}
