/**
 * GlassCard — The One True Glass Component
 * All glass panels in the app MUST use this component.
 * Variants: 'default' | 'interactive' | 'elevated'
 * 
 * CSS classes consumed from global.css:
 *   .glass-card, .glass-card--interactive, .glass-card--elevated
 */

import { forwardRef } from 'react';

const VARIANT_CLASSES = {
  default: 'glass-card',
  interactive: 'glass-card--interactive',
  elevated: 'glass-card--elevated',
};

const GlassCard = forwardRef(function GlassCard(
  { variant = 'default', className = '', style, onClick, children, ...rest },
  ref
) {
  const baseClass = VARIANT_CLASSES[variant] || VARIANT_CLASSES.default;

  return (
    <div
      ref={ref}
      className={`${baseClass}${className ? ` ${className}` : ''}`}
      style={style}
      onClick={onClick}
      role={variant === 'interactive' ? 'button' : undefined}
      tabIndex={variant === 'interactive' ? 0 : undefined}
      {...rest}
    >
      {children}
    </div>
  );
});

export default GlassCard;
