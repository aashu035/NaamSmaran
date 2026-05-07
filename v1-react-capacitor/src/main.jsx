import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';

// Design system — import order matters:
// 1. tokens.css defines ALL variables with Sharad Moon defaults
// 2. Theme files override color variables via [data-theme] selectors
// 3. global.css contains utility classes that consume token variables
import './styles/tokens.css';
import './styles/themes/sharad-moon.css';
import './styles/themes/vrindavan-dawn.css';
import './styles/themes/nikunj.css';
import './styles/themes/van-vihaar.css';
import './styles/themes/shayan.css';
import './styles/global.css';

import App from './App.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>
);
