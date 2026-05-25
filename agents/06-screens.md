# 06 — Screen Specifications (Home, Mala 3D, Day-End)

> **Parent:** [AGENTS.md](../AGENTS.md)

---

## Home Screen — "Zero-UI Darshan"

The Home screen must be a **pure immersive Darshan experience** — NO control panel.

### What the Home Screen IS:
- Full-screen dark canvas (`bgPrimary`) with animated particle shimmer
- **Background image showreel:** user-uploaded images cycling in infinite loop
  (like v1 background — images from `Assets/Images/` folders).
  Users can upload custom images from gallery. Images showcase in background
  as an infinite-loop slideshow.
- A single floating counter number, hovering on the canvas
- Single tap anywhere = +1 (haptic pulse, scale feedback)
- Swipe up = open GlassBottomSheet with quick-add controls + stats
- Tiny hint "↑ स्लाइड करें" at 38% opacity, fades after 5 seconds

### What the Home Screen is NOT:
- No GlassCard with target/streak info visible on main canvas
- No ProgressRing on the main canvas
- No buttons visible on the surface
- No "control panel" of any kind

### GlassBottomSheet Content:
- Quick-add buttons (+108, +1,000, +5,000)
- Today's stats (target / did / streak) in compact format
- Section navigation links to all 7 sections

---

## 3D Mala Specification (Vertical Perspective)

### Camera & Perspective
- Camera positioned **above**, looking **down** at 70° angle
- Beads arranged in vertical circle viewed from top (like holding mala in lap)
- Sumeru bead (109th) at 12 o'clock position — golden highlight
- **NOT** horizontal/caterpillar view — must feel like looking down at your hands

### Gesture Mapping
- Thumb-arc gesture (bottom-half of screen, 180° arc) → bead advancement
- Each arc sweep = 1 bead click + haptic tick
- Natural right-hand thumb motion mapping
- Completing 108 beads = celebration particle burst + haptic heavy

### Display
- Center: `माला: 3 | शेष: 45` and total `कुल: 369`
- Uses SceneView/Filament for true 3D rendering at 60fps

### Tech
- SceneView 2.2.1 with Filament renderer
- `engine/MalaScene.kt` — geometry
- `engine/MalaPhysics.kt` — rotation, snap
- `engine/MalaInputController.kt` — touch mapping

---

## Day-End Analysis Screen

### Triggers
- Notification at 22:00
- Auto on app open after day change
- Crossing DAY_BOUNDARY_HOUR (3 AM)

### Content
- Target vs Did (animated counter roll-up)
- ▲/▼ indicator with color coding
- Streak count with 🔥 if ≥3
- Next day target with formula explanation
- Random pad quote from scripture library
- All 7 section completion status (checkmarks)

### Emotional Tone
- **Met/exceeded:** "गुरु कृपा केवलं" — celebration particles
- **Missed:** Compassionate — "काहू के बल भजन काहू के बल आचार, व्यास भरोसे कुवरी के सोवत पाँव पसार - हमारे माई श्याम जू को राज"
- Never punishing, never guilt-inducing
