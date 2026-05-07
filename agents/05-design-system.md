# 05 — Design System (Compose-Native)

> **Parent:** [AGENTS.md](../AGENTS.md)

---

## The Golden Rules

> [!CAUTION]
> Violating ANY of these rules produces a broken, inconsistent UI.

1. **Never hardcode a color or spacing value in a composable.**
   All values come from `NaamSmaranTheme` / `Color.kt` / `Dimens.kt`.
2. **No `shadow()` with elevation > 0.** Use `RenderEffect.createBlurEffect` for depth.
3. **No Material default palette colors.** Reference only your own theme tokens.
4. **No `RoundedCornerShape` below 12.dp** on cards. Buttons 14.dp. Chips 999.dp.
5. **Opacity hierarchy — exactly 3 levels:**
   - Primary text: 100% (`TextPrimary = Color(0xFFFFFFFF)`)
   - Secondary text: 65% (`TextSecondary = Color(0xA6FFFFFF)`)
   - Tertiary text: 38% (`TextTertiary = Color(0x61FFFFFF)`)
   - Nothing in between. No exceptions.
6. **Motion must communicate state change, never exist for decoration.**
   Tap feedback: haptic + `scale(0.96f)`. Not a color flash.
7. **All animations:** `spring(dampingRatio = 0.75f, stiffness = 300f)`, max 400ms.
   Exception: background particles and 3D orbit (continuous, slow).

---

## Glassmorphism Card Standard

```kotlin
GlassCard = Modifier
    .background(SurfaceGlass)           // Color(0x0AFFFFFF) — 4% white
    .border(1.dp, BorderGlass, shape)   // Color(0x14FFFFFF) — 8% white
    .clip(RoundedCornerShape(24.dp))
    // + RenderEffect blur where supported
```

---

## Theme: Sharad Moon (DEFAULT)

```kotlin
bgPrimary        = Color(0xFF070010)    // deep indigo
accentPrimary    = Color(0xFFE8A0BF)    // rose-pink lotus
accentSecondary  = Color(0xFFB8C8FF)    // moonlight blue
accentGold       = Color(0xFFF5CBA7)    // soft champak gold
accentGlow       = Color(0x26E8A0BF)    // 15% alpha glow
```

---

## Theme: Vrindavan Dawn

```kotlin
bgPrimary        = Color(0xFF0A0408)
accentPrimary    = Color(0xFFFFD4A3)    // champak dawn gold
accentSecondary  = Color(0xFFFFAAB5)    // rose-pearl
accentGold       = Color(0xFFFFD700)
accentGlow       = Color(0x1FFFD4A3)    // 12% alpha
```

---

## Theme: Nikunj

```kotlin
bgPrimary        = Color(0xFF030A04)
accentPrimary    = Color(0xFFA8E6CF)    // bower green
accentSecondary  = Color(0xFFFFD4A3)    // golden light through leaves
accentGold       = Color(0xFFC8E6A0)
accentGlow       = Color(0x1AA8E6CF)    // 10% alpha
```

---

## Theme: Van Vihaar

```kotlin
bgPrimary        = Color(0xFF040806)
accentPrimary    = Color(0xFFE8C547)    // kadamba amber
accentSecondary  = Color(0xFF89C4A0)    // forest green
accentGold       = Color(0xFFF5D76E)
accentGlow       = Color(0x1AE8C547)    // 10% alpha
```

---

## Theme: Shayan

```kotlin
bgPrimary        = Color(0xFF020209)
accentPrimary    = Color(0xFFC8D8F0)    // moonlight silver-blue
accentSecondary  = Color(0xFFFFD080)    // diya warm gold
accentGold       = Color(0xFFFFD080)
accentGlow       = Color(0x14C8D8F0)    // 8% alpha
```

---

## Typography Scale

| Level | Size | Usage |
|:---|:---|:---|
| Display | 44sp | Hero numbers (counter display) |
| H1 | 28sp | Screen titles |
| H2 | 20sp | Section headers |
| Body | 16sp | Regular text |
| Label | 14sp | Buttons, chips |
| Caption | 12sp | Hints, timestamps |
| Overline | 10sp | Tiny labels |

Font: Noto Sans Devanagari (or system default with Devanagari support)

---

## Spacing Grid

Base-8 grid system: 4, 8, 12, 16, 24, 32, 48, 64, 96 dp
All spacing values come from `Dimens.kt` — never hardcode `padding(16.dp)`.
