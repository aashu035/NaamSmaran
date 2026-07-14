# Design System: Naam Smaran (नाम स्मरण)

> **जय श्री हित हरिवंश महाप्रभु 🙏**
>
> Sacred devotional tracker for the Shri Hit Harivansh Mahaprabhu — Radhavallabh Sampraday.
> Personal-use, offline-only Android app. Hindi (Devanagari) UI language.

---

## 1. Visual Theme & Atmosphere

### The Emotional Core

This is not a productivity dashboard. It is a **digital Nikunj** — a sacred bower where the practitioner sits alone with Swamini Shri Radha's name on their lips. Every pixel must whisper *quiet intimacy*, never shout.

The atmosphere is **nocturnal, velvet-deep, and luminous from within** — like moonlight falling through dense creeper-woven bowers onto the banks of the Yamuna on Sharad Purnima night. The UI breathes gently: slow-drifting particles of golden light, soft neon halos behind active elements, and glass surfaces that feel like frozen moonbeams.

**Density:** Spacious. Generous negative space. The screen is mostly dark canvas — content floats, it never crowds.

**Mood keywords:** Sacred intimacy · Moonlit forest · Velvet depth · Devotional luxury · Quiet radiance · Sahchari Bhav (the mood of a divine companion)

### The Sacred Visual World

Every graphic element, background image, icon metaphor, and decorative detail must draw exclusively from the forest world of Vrindavan as described in **Shri Hit Chaturasi Ji**:

**Permitted imagery:**
- Nikunj (hidden bowers woven from creepers — Madhavi Lata, Malati, Juhi)
- Yamuna pulin (the sacred moonlit riverbank)
- Van Vihar (divine forest walks under Kadamba trees)
- Sharad Purnima (the sacred autumn full moon)
- Champak flowers (golden), Bakul, pink and golden lotuses
- Peacock feathers and forms (Mor), parrots (Kir), cuckoos (Pik), swans (Hans)
- Soft glowing diya flames, flowing silk, pearl strings

**Absolutely forbidden — no exceptions:**
- ~~Om symbol (ॐ)~~ — never, anywhere, not even decoratively
- ~~Trishul, Rudraksha, Garuda, Shankh (conch)~~
- ~~Shiva, Durga, or any non-Radhavallabh deity reference~~
- ~~Temple architecture (shikhars, stone gopurams, temple flags)~~
- ~~Gaudiya Vaishnav symbols (different sampraday)~~
- ~~Direct depiction of deity forms~~ — use lotus feet, peacocks, nature instead
- ~~Generic "Hindu" clip-art or stock temple imagery~~

---

## 2. Color Palette & Roles

### 2.1 Theme A — Sharad Moon ✦ DEFAULT

The primary experience. Deep velvet indigo night sky illuminated by the Sharad Purnima moon and soft rose-pink lotus blossoms.

| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `bgPrimary` | Velvet Indigo Night | `#070010` | Full-bleed background canvas. The deepest near-black with a violet-indigo undertone — like the sky above Nikunj at midnight. |
| `accentPrimary` | Rose-Pink Lotus | `#E8A0BF` | Primary interactive accent. Used for active states, completed-section glows, progress rings, and the neon halo behind the main counter. The color of a fresh lotus petal floating on the Yamuna. |
| `accentSecondary` | Moonlight Blue | `#B8C8FF` | Secondary decorative accent. Used for inactive navigation icons transitioning to active, chart lines, and subtle informational highlights. Cold, beautiful, like pure moonlight on water. |
| `accentGold` | Champak Gold | `#F5CBA7` | Warning and partial-completion accent. Used for sections where the user started but did not meet the target. Warm and encouraging, like the golden Champak flowers of Vrindavan. |
| `accentGlow` | Lotus Aura | `#E8A0BF` at 15% | Radial neon glow behind primary interactive elements. Creates a soft halo effect, never harsh. |
| `malaBead` | Bower Mauve | `#6B4D7A` | Color of the 3D mala beads in the rosary tracker. A muted purple-mauve like twilight shadows in the bower. |

### 2.2 Theme B — Vrindavan Dawn

The sacred hour before sunrise when the bower fills with the first golden light.

| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `bgPrimary` | Pre-Dawn Shadow | `#0A0408` | Background. Almost-black with a warm, brownish undertone — the sky 30 minutes before sunrise. |
| `accentPrimary` | Champak Dawn Gold | `#FFD4A3` | Primary accent. The first golden rays touching the Champak trees. Warm, inviting, hopeful. |
| `accentSecondary` | Rose-Pearl Petal | `#FFAAB5` | Secondary accent. The blush on pearl-like petals as dawn light hits them. |
| `accentGold` | Pure Sunrise Gold | `#FFD700` | Strong gold for met-target celebrations. The full golden disc breaking the horizon. |
| `accentGlow` | Dawn Haze | `#FFD4A3` at 12% | Soft golden aura behind active elements. |
| `malaBead` | Sandalwood Brown | `#7A5D4D` | Warm brown like polished sandalwood prayer beads. |

### 2.3 Theme C — Nikunj

Inside the secret bower — emerald darkness with golden rays filtering through dense foliage.

| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `bgPrimary` | Deep Bower Shadow | `#030A04` | Background. The near-black of dense forest shade with an emerald green cast. |
| `accentPrimary` | Fresh Creeper Green | `#A8E6CF` | Primary accent. The vivid green of new Madhavi Lata creeper shoots in spring. |
| `accentSecondary` | Sun Through Leaves | `#FFD4A3` | Secondary accent. Warm golden light filtering through the canopy. |
| `accentGold` | Pale Lime-Gold | `#C8E6A0` | Partial-state gold. A green-gold like new leaves catching sunlight. |
| `accentGlow` | Bower Glow | `#A8E6CF` at 10% | Soft emerald aura. |
| `malaBead` | Moss Stone | `#4D6B5A` | Deep mossy green-grey like forest stones. |

### 2.4 Theme D — Van Vihaar

The divine forest walk under towering Kadamba trees in late afternoon amber light.

| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `bgPrimary` | Forest Floor Shadow | `#040806` | Background. Dark green-grey like the forest floor in deep shade. |
| `accentPrimary` | Kadamba Amber | `#E8C547` | Primary accent. Rich amber-yellow of Kadamba tree blossoms. |
| `accentSecondary` | Forest Canopy Green | `#89C4A0` | Secondary accent. The gentle green of the upper canopy. |
| `accentGold` | Afternoon Gold | `#F5D76E` | Celebration gold. Warm, saturated, like late afternoon forest light. |
| `accentGlow` | Amber Haze | `#E8C547` at 10% | Warm amber glow behind elements. |
| `malaBead` | Dry Bark | `#6B6A4D` | Olive-grey like sun-dried Kadamba bark. |

### 2.5 Theme E — Shayan

Night. The Divine Couple rests. Moonlight silver-blue, a single diya flame burning in the darkness.

| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `bgPrimary` | Deepest Night | `#020209` | Background. The absolute darkest — near-pure black with the faintest blue shimmer. |
| `accentPrimary` | Moonlight Silver-Blue | `#C8D8F0` | Primary accent. Cool, quiet, the color of moonlight on a silk coverlet. |
| `accentSecondary` | Diya Warm Gold | `#FFD080` | Secondary accent. The warm amber of a single diya flame flickering in the still night. |
| `accentGold` | Night Flame | `#FFD080` | Same as secondary — at night, the diya is both guide and companion. |
| `accentGlow` | Moon Haze | `#C8D8F0` at 8% | The softest, most restrained glow — barely there, like moonbeams through curtains. |
| `malaBead` | Twilight Stone | `#4D5D6B` | Cool blue-grey like polished night stones. |

---

### 2.6 Theme-Agnostic Colors (Universal across all 5 themes)

These colors remain constant regardless of which theme is active.

#### Glass Surfaces
| Token | Descriptive Name | Hex / Alpha | Role |
|:------|:-----------------|:------------|:-----|
| `SurfaceGlass` | Frozen Moonbeam | `#FFFFFF` at 4% (`0x0A`) | Default glass card background. Nearly invisible — suggests a surface without showing one. |
| `SurfaceGlassHover` | Warm Moonbeam | `#FFFFFF` at 7% (`0x12`) | Hover/pressed state for glass cards. Slightly brighter than resting. |
| `SurfaceGlassActive` | Bright Moonbeam | `#FFFFFF` at 10% (`0x1A`) | Active/focused glass surface. |
| `SurfaceGlassElevated` | Lifted Moonbeam | `#FFFFFF` at 8% (`0x14`) | Elevated glass cards that sit above other glass layers. |
| `SurfaceGlassSheet` | Deep Frosted Indigo | `#080810` at 60% (`0x99`) | The heavy frosted-glass bottom sheet. Dense enough to read content over any background. |
| `SurfaceGlassInput` | Input Well | `#FFFFFF` at 6% (`0x0F`) | Recessed input fields within glass surfaces. |

#### Glass Borders
| Token | Descriptive Name | Hex / Alpha | Role |
|:------|:-----------------|:------------|:-----|
| `BorderGlass` | Glass Edge | `#FFFFFF` at 8% (`0x14`) | Standard 1dp border on glass cards. Fine, barely-visible, catches light subtly. |
| `BorderGlassFocus` | Focused Edge | `#FFFFFF` at 20% (`0x33`) | Focused/active card border. Clearly visible but never harsh. |
| `BorderGlassSheet` | Sheet Edge Highlight | `#FFFFFF` at 25% (`0x40`) | The prominent border on the bottom sheet. Bright enough to define the sheet edge against any background. |
| `BorderGlassInput` | Input Edge | `#FFFFFF` at 14% (`0x24`) | Input field borders within glass surfaces. |

#### Text Opacity Hierarchy — EXACTLY Three Levels
| Token | Descriptive Name | Hex | Usage |
|:------|:-----------------|:----|:------|
| `TextPrimary` | Full White | `#FFFFFF` at 100% | Counter numbers, active section names, screen titles. Maximum emphasis. |
| `TextSecondary` | Muted Silver | `#FFFFFF` at 65% (`0xA6`) | Sub-labels, units ("माला", "श्लोक"), target descriptions. Supporting information. |
| `TextTertiary` | Whisper White | `#FFFFFF` at 38% (`0x61`) | Timestamps, inactive states, hint text. Information that is present but should not demand attention. |

> **CRITICAL:** No intermediate opacity values exist. If text does not fit one of these three levels, rethink the information hierarchy — do not create a fourth level.

#### Semantic State Colors
| Token | Descriptive Name | Hex | Role |
|:------|:-----------------|:----|:-----|
| `StateExceeded` | Sacred Forest Green | `#A8E6A0` | Target met or exceeded. The devotee has fulfilled today's commitment. |
| `StatePartial` | Champak Warning Gold | `#F5D76E` | Partial completion (`0 < did < target`). Progress happened but the goal is not yet reached. |
| `StateMissed` | Faded Veil White | `#FFFFFF` at 20% | No progress at all (`did == 0`). Quiet absence, never harsh judgment. |
| `StateStreakFire` | Diya Flame Orange | `#FF8C42` | Active streak ≥3 days. The inner fire of consistent practice. |

#### Overlay System
| Token | Descriptive Name | Hex / Alpha | Role |
|:------|:-----------------|:------------|:-----|
| `OverlayWhiteHigh` | Counter Glow | `#FFFFFF` at 90% | Large counter numbers overlaid on background images. |
| `OverlayWhiteMedium` | Label Float | `#FFFFFF` at 55% | Section labels floating over image backgrounds. |
| `OverlayScrimLight` | Top Veil | `#000000` at 25% | Light gradient scrim at the top of image backgrounds. |
| `OverlayScrimMedium` | Counter Backdrop | `#000000` at 45% | Medium scrim behind the floating counter for readability. |
| `OverlayScrimHeavy` | Mid Gradient | `#000000` at 55% | Gradient middle zone between content and background. |
| `OverlayScrimDense` | Bottom Anchor | `#000000` at 88% | Heavy gradient at the bottom of the screen anchoring content. |

---

## 3. Typography Rules

### Font Strategy
The app uses **system fonts exclusively** — no custom-bundled typefaces. Android 8+ ships with Noto Sans Devanagari, which renders Hindi beautifully by default.

| Family | System Mapping | Character | Usage |
|:-------|:---------------|:----------|:------|
| `DevanagariUi` | System Default | Clean, precise, readable | All Hindi interface labels, body text, section names |
| `DevanagariDeco` | System Serif | Elegant, classical, literary | Decorative headers like "राधे राधे", sacred verse displays, the greeting text |
| `NumberFont` | System Sans-Serif | Crisp, high-contrast, modern | All numeric counters, Jap counts, statistics, progress fractions |

### Weight Discipline
Only **two weights** are ever used:
- **Regular (400):** Body text, supporting labels, descriptions
- **Bold (700):** Headers, counter numbers, active states, emphasis

No Medium (500). No Semibold (600). This constraint keeps the hierarchy crystal-clear.

### Type Scale

| Level | Size | Line Height | Font | Weight | Usage |
|:------|:-----|:------------|:-----|:-------|:------|
| **Hero** | 44sp | 1.2× | NumberFont | Bold | The main Naam Jap counter — the single most important number on screen |
| **Sub-Hero** | 32sp | 1.3× | DevanagariDeco | Regular | The resting greeting "जय जय श्री हित हरिवंश" floating above the counter |
| **H1** | 28sp | 1.2× | DevanagariUi | Bold | Screen titles, section headings in the dashboard |
| **H2** | 24sp | 1.2× | NumberFont | Bold | Medium numeric displays, mid-size statistics |
| **H3** | 20sp | 1.5× | DevanagariUi | Bold | Card titles, streak count labels, glass sheet section headers |
| **Title Large** | 22sp | 1.4× | DevanagariUi | Bold | Section screen top headers |
| **Title Medium** | 18sp | 1.4× | DevanagariUi | Bold | Bottom sheet labels, sub-section headers |
| **Title Small** | 15sp | 1.4× | DevanagariUi | Bold | Card sub-headings, compact section titles |
| **Body** | 16sp | 1.5× | DevanagariUi | Regular | Primary body text, pad/verse content, descriptions |
| **Label** | 14sp | 1.5× | DevanagariUi | Regular | Supporting labels, timestamps, units |
| **Caption** | 12sp | 1.5× | DevanagariUi | Regular | Hints, metadata, inactive text |
| **Overline** | 10sp | 1.2× | NumberFont | Bold | Tiny badges, micro-chips, streak fire indicators |

### Number Formatting
All numbers use the **Indian numbering system**: `1,000` · `10,000` · `1,00,000` · `9,99,999`

---

## 4. Component Stylings

### 4.1 Glass Cards
The primary container in the entire UI. Every card, sheet, and surface uses the glassmorphism language.

- **Surface:** Frozen Moonbeam — `#FFFFFF` at 4% opacity
- **Border:** Glass Edge — 1dp stroke, `#FFFFFF` at 8% opacity
- **Corner Radius:** Generously rounded — **24dp** minimum on all cards
- **Blur:** 20dp background blur (`RenderEffect.createBlurEffect` on Android, `backdrop-filter: blur(20px)` in web)
- **Shadow:** ~~None.~~ Absolutely no `shadow()` with elevation > 0. Depth is created exclusively through layered transparency and blur differences. The UI is entirely flat-glass — depth is implied, never painted.
- **Hover/Press:** Surface brightens from 4% → 7% white. Scale compresses to 0.96× with a spring snap-back. Haptic tick feedback.

### 4.2 Buttons

#### Quick-Add Buttons (+108, +1,000, +5,000)
- **Shape:** Pill-shaped capsule — fully rounded corners (`999dp`)
- **Surface:** Glass surface (`#FFFFFF` at 4%) with glass border
- **Text:** NumberFont Bold, `accentPrimary` color
- **Press Feedback:** Scale to 0.96× + haptic tick. Never a color flash.
- **Active Glow:** Faint `accentGlow` radial behind on hover/focus

#### Ghost Navigation Buttons
- **Shape:** Pill-shaped capsule
- **Surface:** Transparent with a visible glass border at 75% (`DashboardAlpha.GhostButtonBorder`)
- **Text:** `OverlayWhiteSubtle` (75% white)

#### Toggle Buttons (Ashtayam Seva, Nitya Path)
- **Shape:** Pill-shaped capsule
- **States:** Off = glass surface. On = filled with `accentPrimary` at 15% + border at `accentPrimary`
- **Transition:** Color fills inward from center with a 300ms spring animation

### 4.3 Bottom Sheet (Glass Dashboard)

The most complex surface in the app. It slides up from the bottom in three stages.

- **Surface:** Deep Frosted Indigo — `#080810` at 60% opacity. Dense enough to blur the background into an unrecognizable, dreamy wash.
- **Border:** Sheet Edge Highlight — `#FFFFFF` at 25%. A clearly visible bright edge along the top curve.
- **Corner Radius:** 28dp on top-left and top-right only. Bottom is flat (extends to screen edge).
- **Drag Handle:** A slim horizontal bar — 36dp wide × 4dp tall, `#FFFFFF` at 20%, centered at the top of the sheet with 12dp padding.
- **Three Stages:**
  - `Hidden` — Parked below the fold at 92% of screen height. Only the faintest hint of the sheet edge is visible.
  - `QuickActions` — Pulled up to ~52% of screen height. Shows the drag handle, section summary, and quick-add buttons.
  - `FullGrid` — Fully expanded to 8% from top. Shows the complete devotional dashboard grid with all 7 sections.
- **Gesture:** The entire sheet surface is draggable — not just the handle. Spring physics with `dampingRatio = 0.7`, `stiffness = 400`.

### 4.4 Section Dashboard Cards

Each of the 7 devotional sections appears as a card in the dashboard grid. The card visually communicates the current completion state:

#### Complete State (did ≥ target)
- **Surface:** `accentPrimary` at 8% opacity — a faint, warm, glowing tint
- **Border:** `accentPrimary` at 22% — clearly marked as "done"
- **Text:** Primary white for the count, secondary white for the label
- **Accent Dot:** A small glowing circle in `accentPrimary` beside the section name

#### Partial State (0 < did < target)
- **Surface:** `StatePartial` (`#F5D76E`) at 12% — a warm amber glow signaling "in progress"
- **Border:** `StatePartial` at 22% — visible gold outline
- **Text:** Count shown in `accentGold` (warm champak gold), label in secondary white
- **Progress Bar:** Filled portion in `accentGold`, track in 8% white

#### Incomplete State (did = 0)
- **Surface:** Standard glass (`#FFFFFF` at 4%)
- **Border:** Standard glass border (`#FFFFFF` at 8%)
- **Text:** All tertiary white (38%) — deliberately quiet, no emphasis
- **Progress Bar:** Empty track in 8% white

### 4.5 Progress Rings & Bars

#### Large Progress Ring (Naam Jap — Home Screen)
- **Size:** 180dp diameter
- **Stroke:** 10dp, rounded caps
- **Track:** `#FFFFFF` at 8%
- **Fill:** `accentPrimary` (or `accentGold` if partial), animated with spring physics
- **Center Content:** Hero counter number (44sp, Bold)

#### Section Progress Bars
- **Height:** 6dp (standard) or 10dp (Chaturasi pad progress)
- **Track:** `#FFFFFF` at 8%
- **Fill:** `accentPrimary` when complete, `accentGold` when partial
- **Corner Radius:** Fully rounded (pill-shaped)

### 4.6 Input Fields
- **Surface:** Input Well — `#FFFFFF` at 6%
- **Border:** Input Edge — `#FFFFFF` at 14%, increasing to 20% on focus
- **Corner Radius:** Generously rounded — 16dp
- **Text Input:** Primary white, placeholder in Tertiary white
- **Cursor:** `accentPrimary`

### 4.7 Notification Pills & Badges

#### Summary Header Pill
- **Surface:** `accentPrimary` at 14%
- **Border:** `accentPrimary` at 32%
- **Shape:** Pill (fully rounded)
- **Text:** "आज: ३ / ७ पूर्ण" in `accentPrimary`

#### Active Section Pill
- **Surface:** `accentPrimary` at 15%
- **Border:** `accentPrimary` at 42%
- **Text:** Bold, `accentPrimary`

#### Streak Fire Badge
- **Color:** Diya Flame Orange (`#FF8C42`)
- **Icon:** 🔥 emoji rendered inline
- **Trigger:** Streak ≥ 3 consecutive days

---

## 5. Layout Principles

### Spatial Grid
The entire app runs on a **base-8 spacing grid**. Every margin, padding, and gap is a multiple of 4dp:

`4 · 8 · 12 · 16 · 20 · 24 · 28 · 32 · 40 · 48 · 64 · 80`

**Key semantic spacings:**
- **Card internal padding:** 24dp — generous breathing room inside glass cards
- **Screen horizontal padding:** 16dp — consistent left/right margins on all screens
- **Card stack gap:** 16dp — vertical space between stacked cards
- **Inline element gap:** 8dp — horizontal space between adjacent elements

### Immersive Fullscreen Canvas
The app runs in **100% immersive fullscreen** mode:
- The OS status bar (clock, battery, signal) is completely hidden
- The OS navigation bar (back, home, recents) is completely hidden
- Users can transiently summon system bars by swiping from the screen edge — bars appear as a translucent overlay and auto-hide after a few seconds
- This gives 100% of the screen to the devotional experience

### Zero-UI Home Screen Philosophy
The home screen follows a **"Zero-UI" philosophy** — it shows almost nothing. The screen is a pure darshan canvas:
- Dark background with an infinite-loop slideshow of user-uploaded sacred images
- A single floating counter number at center, no buttons, no controls
- The greeting "राधे राधे" in decorative serif floating above the counter
- Tap anywhere = +1 count (haptic + subtle scale feedback)
- Swipe up = reveal the glass dashboard from below

### Counter Slide Animation (Chanting Mode)
When the user taps continuously (active chanting), the counter smoothly slides to the bottom-left corner:
- **Resting position:** Dead center of screen, scale 1.0×
- **Chanting position:** Bottom-left corner (bias -0.85, 0.75), scale 0.65×, pivot anchored bottom-left
- **Text fade:** Greeting and target fraction text fade to 0% opacity, leaving the full center screen open for darshan of the background image
- **Reset:** After 5 seconds of inactivity, the counter glides back to center at full size
- **Motion:** Spring animation — `dampingRatio = 0.75`, `stiffness = 300`

### Content-First Hierarchy
On every screen:
1. **The number comes first.** The counter/count is always the largest, most prominent element.
2. **Sacred text comes second.** Scripture verses, pad content, and devotional text are given generous space.
3. **Chrome comes last.** Navigation, controls, settings — all recede into glass surfaces or are hidden entirely until summoned.

---

## 6. Motion & Animation

### The One Animation Rule
> **Motion must communicate state change. It must never exist for decoration.**

### Interaction Feedback
- **Tap press:** Scale to 0.96× with spring snap-back (`dampingRatio = 0.7`, `stiffness = 400`). Accompanied by haptic tick.
- **Never** use color flash for tap feedback. The scale compression is the only feedback channel.

### Spring Parameters
All animations in the app use spring physics — never linear or ease-in-out curves.

| Spring Preset | Damping | Stiffness | Usage |
|:-------------|:--------|:----------|:------|
| Default | 0.7 | 400 | Tap feedback, card reveals, sheet transitions |
| Counter | 0.7 | 200 | Slow counter roll-up (Day-End Analysis screen) |
| High | 0.7 | 800 | Quick snaps (toggle switches, badge pop-ins) |

### Hard Duration Cap
No animation may exceed **400ms** total duration. If a spring hasn't settled by 400ms, it must be critically damped.

**Exception:** Background particles and 3D orbit animations (continuous, slow, ambient — these are not UI state animations).

### Background Particles
- Slow-drifting orbs of golden light floating across the canvas
- Speed: ~0.5dp/frame, random gentle Brownian drift
- Color: `accentGlow` (theme-aware)
- Count: 15–25 particles at any time
- Opacity: Individual particles range from 5% to 25% — subtle, never distracting

---

## 7. Iconography

### Icon Style
- **Line-art only.** No filled icons. Thin, elegant strokes (1.5dp–2dp).
- **Metaphors drawn from Vrindavan:** Lotus petals, peacock feathers, manuscript scrolls, mala beads, diya flames, creeper vines.
- **Color:** `accentPrimary` when active, `NavIconInactive` (`#FFFFFF` at 35%) when inactive.
- **Size:** 24dp standard, 28dp in navigation bars.

### Forbidden Icon Subjects
- No Om (ॐ) symbol
- No Trishul (trident)
- No Rudraksha beads
- No generic temple icons
- No bell-and-aarti-thali generic pooja imagery

---

## 8. Screen Inventory

### 8.1 Home — Zero-UI Darshan Canvas
The spiritual center of the app. Pure immersive background with a floating counter.

### 8.2 Glass Dashboard (Bottom Sheet)
Contains quick-add controls and the complete 7-section devotional grid.

### 8.3 Section Screens (×7)
Each of the 7 devotional sections has its own screen:
1. **नाम जप** — Dual-track counter (Track A: हरिवंश माला, Track B: राधा count)
2. **श्री हित चतुरसी जी** — 84 pads reading tracker with checkable list
3. **श्री हित राधा सुधानिधी जी** — Shloka reader with side-by-side translation
4. **श्री हित सेवक वाणी** — Chhands reading tracker with meaning
5. **अष्टयाम सेवा पद्धति** — 8-period daily checklist with timestamps
6. **नित्य पाठ रसोपासना** — Daily toggle with 7-day streak and monthly calendar
7. **श्री वृंदावन शत लीला** — Chhands reading tracker

### 8.4 Day-End Analysis
Animated summary screen showing today's performance across all 7 sections with counter roll-up animations, streak fire, and a random scripture verse.

### 8.5 Settings
Theme selector (5 themes), day boundary configuration, target adjustment, backup controls, audio filename entry.

### 8.6 3D Mala Tracker (Deferred)
SceneView/Filament-rendered prayer bead rosary viewed from above at 70° angle. Thumb-arc gesture = bead advancement. Future release.

---

## 9. Emotional Tone Guide

### When the devotee meets their target
- **Visual:** Section card glows rose-pink. Celebration particle burst. Streak fire badge appears.
- **Text:** "गुरु कृपा केवलं" (Only by Guru's grace)
- **Tone:** Humble gratitude, never self-congratulation. The achievement belongs to Guru's blessing.

### When the devotee partially completes
- **Visual:** Section card outlined in warm champak gold. Progress bar filled partway.
- **Text:** Progress shown honestly — "२० / २१,६०० जप"
- **Tone:** Encouraging warmth. Gold is a *warm* color — it says "you started, keep going."

### When the devotee misses entirely
- **Visual:** Section card remains transparent glass. Tertiary white text.
- **Text:** Target shown without judgment — "लक्ष्य: १२ पद"
- **Tone:** **Compassionate, never punishing.** The Day-End Analysis uses the verse:
  > "काहू के बल भजन काहू के बल आचार, व्यास भरोसे कुवरी के सोवत पाँव पसार — हमारे माई श्याम जू को राज"
  >
  > *("Some rely on devotion, some on conduct — but the princess sleeps with her feet stretched out, trusting Vyasa. Our Mother, it is Shyam Ju's kingdom.")*

---

## 10. Stitch Prompt — Full Screen Generation

Copy this prompt directly into Stitch to generate screens matching this design system:

```
Design a premium dark-mode mobile app screen for "Naam Smaran" — a sacred devotional tracker.

ATMOSPHERE: The app lives in Vrindavan at night — imagine moonlight filtering through dense bower creepers onto the Yamuna riverbank. The mood is sacred intimacy, quiet devotional luxury, and velvet depth. Think of the most premium, luxurious dark-mode analytics dashboards you've ever seen, but infused with traditional Indian sacred aesthetics.

COLOR PALETTE — "Sharad Moon":
- Canvas: incredibly deep indigo-black (#070010) — like the sky above a bower at midnight
- Primary accent: soft rose-pink lotus (#E8A0BF) — for completed states, active glows, progress rings
- Secondary accent: cold moonlight blue (#B8C8FF) — for charts, inactive-to-active transitions
- Warning/partial accent: warm champak gold (#F5CBA7) — for partial completion states
- Text: exactly 3 levels — full white (#FFFFFF), 65% white (#A6A6A6..#FFFFFF), 38% white

GLASS SURFACES: Every card uses glassmorphism — 4% white opacity background, 8% white 1px border, 24dp rounded corners, 20dp background blur. No drop shadows ever. Depth comes from layered transparency only.

TYPOGRAPHY: System Devanagari for Hindi labels. System Serif for decorative headers ("राधे राधे"). System Sans-Serif for counter numbers. Only 2 weights: Regular (400) and Bold (700). Hero counter at 44sp.

SACRED RULES: Imagery must only include Vrindavan nature — lotus flowers, peacock feathers, creepers, moonlight, riverbanks. NO Om symbol, NO trishul, NO temple architecture, NO generic Hindu iconography.

Generate the Home Screen showing:
- Full immersive background (no status bar, no nav bar) with a sacred Vrindavan bower painting
- A large floating counter "२१,६००" in hero typography at center
- "राधे राधे" greeting in decorative serif floating above
- A faint "↑ स्लाइड करें" hint at 38% opacity near the bottom
- Soft glowing amber and rose-pink particles drifting slowly across the canvas
```

---

## 11. Dashboard Screen — Full Layout Architecture

> **Source:** Synthesized from 3 AI-generated mockup images + 2 React TSX prototypes (May 2026).
> Reference files saved at `docs/design-references/`.

The Dashboard is a **scrollable analytics screen** accessible via the bottom navigation bar or by swiping up from the home screen. It presents a comprehensive view of all 7 devotional sections' progress, trends, and recent activity. It is the "second brain" of the app — the home screen is pure darshan; the dashboard is pure data.

### 11.1 Dashboard Visual Zones (Top-to-Bottom)

```
┌─────────────────────────────────────────┐
│  ① सप्ताह की झलक (Weekly Glance Strip)  │
├─────────────────────────────────────────┤
│  ② लक्ष्य रुझान        ③ लक्ष्य बनाम    │
│     (Target Trends)      प्राप्ति (Donut) │
├─────────────────────────────────────────┤
│  ④ सातों साधना संक्षिप्त स्थिति          │
│     (7-Sadhana Horizontal Carousel)      │
├─────────────────────────────────────────┤
│  ⑤ लक्ष्य शीघ्र क्रियाएँ                │
│     (Quick Actions Grid — 2-Column)      │
├─────────────────────────────────────────┤
│  ⑥ आज का प्रेरक पद                     │
│     (Inspirational Verse — with BG image)│
├─────────────────────────────────────────┤
│  ⑦ Bottom Navigation Bar                │
└─────────────────────────────────────────┘
```

---

### 11.2 Zone ① — सप्ताह की झलक (Weekly Glance Strip)

A single horizontal glass card showing the current week's daily completion status.

**Layout:** Left = weekly score pill (`6 / 7 दिन लक्ष्य पूरे किए`) with a gradient progress bar. Right = 7 circular day indicators.

**Day Indicators:**
- ✅ **Done:** Solid border in Sacred Forest Green (`#2ECC71` area) with a check icon. Soft green glow.
- ⚠️ **Partial:** Dashed border in Rose-Pink Lotus (`accentPrimary`), half-filled arc.
- ⭕ **Empty:** Dashed border in 20% white, no fill.

**Week header:** "सप्ताह की झलक" in Champak Gold serif, with a "इस सप्ताह ▾" filter dropdown.

---

### 11.3 Zone ② — लक्ष्य रुझान (Target Trends — Area Chart)

A glass card containing a smooth area chart showing the last 30 days of Naam Jap progress.

- **Chart type:** Smooth monotone area chart
- **Line color:** `accentPrimary` (`#E8A0BF`), 3dp stroke
- **Fill gradient:** `accentPrimary` at 30% → 0% (top to bottom)
- **X-axis labels:** Hindi dates in Tertiary white — "28 अप्रैल", "5 मई", "12 मई", "आज"
- **Hero stat:** "8,76,540" in Hero typography (44sp Bold) above the chart
- **Sub-label:** "कुल नाम जप" in Secondary white, followed by "↑ 1,24,560 पिछले 30 दिनों में" in Sacred Forest Green

---

### 11.4 Zone ③ — लक्ष्य बनाम प्राप्ति (Achievement Donut)

A glass card with a three-segment donut chart showing monthly target completion rates.

- **Donut segments:**
  - लक्ष्य पूरे किए (Met): `#E8388B` (Rose-Pink, slightly more vivid than accentPrimary for chart contrast)
  - आंशिक (Partial): `#F39C12` (Warm Orange-Gold)
  - लक्ष्य नहीं पूरे हुए (Missed): `#5B86E5` (Cool Periwinkle Blue)
- **Center label:** "71%" in H1 Bold, "कुल प्रगति" in Caption
- **Legend:** Three rows with colored dots and "X दिन" counts
- **Footer:** "कुल दिन: 31" in Tertiary white

---

### 11.5 Zone ④ — सातों साधना संक्षिप्त स्थिति (7-Sadhana Carousel)

A **horizontally scrollable** row of 7 compact glass cards, one per devotional section. This is the visual heart of the dashboard.

**Each card contains (top to bottom):**
1. **Icon circle** — section-specific icon in a glass circle with themed glow
2. **Section number** — "1" through "7" in Overline, Champak Gold
3. **Section name** — 2-line Hindi name in Title Small
4. **Divider** — 1dp glass border
5. **Progress data** — count fraction ("56 / 84 पद") in Body Bold
6. **Circular progress ring** — small (54dp) with percentage center label
7. **Status tag** — "↑ 6 पद शेष" in Sacred Forest Green, or "🔥 7 दिन" streak

**Special case — Card 1 (Naam Jap):** Shows dual-track layout with two stacked sub-sections:
- Track A: "हरिवंश नाम — 73 / 84 माला" with its own mini progress ring
- Track B: "राधा नाम — 8,76,540 / 10,50,000" with its own mini progress ring

**Section Icon-Color Assignments:**

| # | Section | Icon Metaphor | Accent Color |
|:--|:--------|:-------------|:------------|
| 1 | नाम जप | Mala beads (CircleDot) | `#E8388B` (Rose-Pink) |
| 2 | चतुरसी जी | Open manuscript (BookOpen) | `#F39C12` (Champak Orange) |
| 3 | राधा सुधानिधी जी | Lotus flower (Flower2) | `#E8388B` (Rose-Pink) |
| 4 | सेवक वाणी | Peacock feather (Feather) | `#5B86E5` (Moonlight Blue) |
| 5 | अष्टयाम सेवा | Diya flame (Flame) | `#F39C12` (Champak Orange) |
| 6 | नित्य पाठ | Sacred grove (Landmark) | `#00CEC9` (Yamuna Teal) |
| 7 | शत लीला | Lotus eye (Eye) | `#9B59B6` (Bower Purple) |

---

### 11.6 Zone ⑤ — लक्ष्य शीघ्र क्रियाएँ (Quick Actions Grid)

A 2-column grid of actionable buttons — one per section (+ Naam Jap at top for 7 total + Vrindavan Shat Leela spanning full width at bottom).

**Each action row contains:**
- Left: Section icon circle (colored glow) + Title ("नाम जप जोड़ें") + Subtitle ("राधा या हरिवंश नाम जप दर्ज करें")
- Right: Chevron arrow (→) in Tertiary white

**Interaction:** Tapping navigates to the corresponding section screen.

---

### 11.7 Zone ⑥ — आज का प्रेरक पद (Inspirational Verse Card)

A stunning glass card with a background image peek and a random scripture verse.

- **Background:** A Vrindavan scene (Yamuna bank, lotuses, moonlight — **NO temple architecture**) at 60% opacity with `mix-blend-mode: screen`
- **Left gradient overlay:** From `bgPrimary` 100% → transparent (so text is readable)
- **Header:** "आज का प्रेरक पद" with lotus icon
- **Verse body:** Left-bordered (2dp, `accentPrimary` at 50%) Hindi verse in DevanagariDeco Serif
- **Attribution:** "— श्री हित चतुरसी जी" in Caption
- **Decorative:** Faint peacock emoji or feather SVG on the right side

---

### 11.8 Zone ⑦ — Bottom Navigation Bar

Fixed at the screen bottom. A frosted glass bar with 5 tabs and a floating center action button.

| Tab | Icon | Label | Active Color |
|:----|:-----|:------|:-------------|
| Dashboard | Home | Dashboard | `accentPrimary` |
| Analytics | BarChart | Analytics | `NavIconInactive` → `accentPrimary` |
| **+ (FAB)** | Plus | — | Gradient: `accentPrimary` → `accentGold` |
| Goals | Target | Goals | `NavIconInactive` → `accentPrimary` |
| Profile | User | Profile | `NavIconInactive` → `accentPrimary` |

**Center FAB:**
- Floating 64dp circle, elevated -24dp above the nav bar surface
- Gradient fill: `#E8388B` → `#F5D76E` (rose-pink to champak gold)
- Pulsing neon glow behind it at 40% opacity
- `boxShadow: 0 8px 30px rgba(232,56,139,0.4)`

---

### 11.9 Known Sampraday Violations to Fix in Reference Designs

> [!WARNING]
> The reference mockup images contain the following violations that MUST be corrected during implementation:

1. **Temple/Mandir structure in background images** — Replace with pure bower/nature scenes (creepers, Yamuna, lotuses). No architectural structures.
2. **English text in hero banner** ("Stay focused, achieve your goals") — Must be Hindi. Use "गुरु कृपा केवलं" or "राधे राधे" instead.
3. **Profile picture "Alex"** — Remove. This is a personal devotional app, not a social app. No user avatar.
4. **Dollar signs ($)** in donut chart — Replace with devotional counts (जप, पद, श्लोक).
5. **Notification bell** — Replace with a lotus or peacock feather icon if needed.

---

## 12. Design Reference Files

All reference materials are saved at:
```
docs/design-references/
├── dashboard-chatgpt-v1.png    — ChatGPT mockup with Hindi labels + stats
├── dashboard-chatgpt-v2.png    — ChatGPT mockup with English labels (template)
├── dashboard-gemini.png        — Gemini mockup with full Sadhana overview
├── dashboard-full-hindi.tsx    — TSX: Full Hindi dashboard with charts
└── dashboard-hero-english.tsx  — TSX: Hero banner + overview cards
```

---

**जय श्री हित हरिवंश महाप्रभु 🙏**
**श्री राधावल्लभ लाल जु की जय 🌸**
