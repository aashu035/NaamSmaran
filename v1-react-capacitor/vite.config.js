import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  server: {
    port: 5173,
    host: true, // LAN access for phone testing
  },

  build: {
    target: 'es2020', // Android WebView (Chromium 95+) compatibility
    cssMinify: true,
    rollupOptions: {
      output: {
        // Split Three.js into its own chunk (~650KB) so it doesn't block first paint
        // Vite 8 uses Rolldown which requires manualChunks as a function
        manualChunks(id) {
          if (id.includes('node_modules/three/')) return 'three';
          if (id.includes('node_modules/@react-three/')) return 'r3f';
        },
      },
    },
  },

  define: {
    // Safety shim for Capacitor plugins that reference process.env
    'process.env': {},
  },
});
