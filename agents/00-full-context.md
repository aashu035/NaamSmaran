# 00 — Full Project Context for New Agents

> **Audience:** agents or collaborators who are not already inside the Naam Smaran work circle.
> **Last updated:** 2026-05-25 by Codex via `naam-smaran-context`.
> **Canonical sources:** [AGENTS.md](../AGENTS.md), then `agents/01`–`07`.

---

## Project Identity

- App: Naam Smaran (नाम स्मरण).
- Package: `com.radhavallabh.naamsmaran`.
- Active codebase: `v2-native-kotlin/`.
- Archived codebases: `v0-prototype/` and `v1-react-capacitor/`.
- Stack: native Kotlin + Jetpack Compose + Room + Hilt + SceneView/Filament.
- Purpose: personal devotional tracker. This is not a public, multi-user, analytics, social, or commercial app.
- UI language: Hindi/Devanagari for user-facing text. English is acceptable for technical docs/code.
- Number format: Indian numbering system, for example `1,00,000`.

The project has older React/Capacitor history, but current app work belongs in `v2-native-kotlin/`.

---

## Sampraday Context and Restrictions

Naam Smaran belongs to the Shri Hit Harivansh Mahaprabhu — Radhavallabh Sampraday.

Core devotional frame:

- Acharya: Shri Hit Harivansh Chandra Mahaprabhu Ji.
- Ishta Dev: Shri Radhavallabh Lal Ju.
- Swamini: Shri Radha Maharani; Radha Charan Pradhan.
- Gurudev: Shri Hit Govind Sharan Premanand Ji Maharaj.
- Bhav: Sahchari / Sakhi / Tat-Sukh Bhav.

Allowed visual world:

- Vrindavan, Yamuna pulin, Nikunj, Van Vihar, Sharad Purnima.
- Champak, bakul, madhavi lata, malati, juhi, kadamba, lotus.
- Peacock, parrot, cuckoo, swan.
- Deep indigo, rose-gold, champak-gold, emerald green, moonlight silver, diya amber, pearl white.

Forbidden in UI/design/content:

- Om symbol, trishul, rudraksha, Garuda, conch as decoration.
- Shiva/Durga/generic Hindu iconography.
- Direct deity-form depiction.
- Gaudiya Vaishnava symbols/references.
- Temple architecture backgrounds.
- Anything outside the Radhavallabh visual world.

When in doubt, read `agents/01-sampraday.md` before designing.

---

## The 7 Devotional Sections

There are exactly 7 devotional sections:

| # | Hindi name | Type | Daily baseline | Rule |
|---|---|---|---|---|
| 1 | नाम जप | Dual-track chanting | Radha count + Harivansh mala | Radha formula + mala carry-over |
| 2 | श्री हित चतुरसी जी | Reading | 12 पद | Carry-over, +6 पद if met |
| 3 | श्री हित राधा सुधानिधी जी | Reading with meaning | 10 श्लोक | Carry-over, +5 श्लोक if met |
| 4 | श्री हित सेवक वाणी | Reading with meaning | 5 छंद | Carry-over, +2 छंद if met |
| 5 | अष्टयाम सेवा पद्धति | Checklist | Minimum 3 seva times | Flexible checklist |
| 6 | नित्य पाठ रसोपासना | Daily toggle | Complete daily | Completion/streak |
| 7 | श्री वृंदावन शत लीला | Reading | 10 छंद | Carry-over, +5 छंद if met |

Do not collapse these to 6 sections. Do not treat settings as a devotional section.

---

## Formula and Logic Rules

### Radha Naam Jap Track B

Applies only to "राधा" naam jap count:

```text
T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
```

Defaults:

- Initial target: `21,600`.
- Increment: `5,000`.
- Day boundary: `3 AM`.
- If missed with `D(n)=0`, the target doubles.
- No maximum cap unless the user explicitly requests one.

Canonical verification table is in `agents/02-formula.md`.

### Harivansh Naam Jap Track A

- Unit: mala, with 1 mala = 108 repetitions.
- Daily baseline: 11 mala.
- If met: increase by 5–6 mala.
- If missed: carry over deficit.
- This does not use the Radha doubling formula.

### Reading Sections

Sections 2, 3, 4, and 7 use carry-over:

```text
If met: tomorrow = today_target + increment
If missed: tomorrow = unfinished_remainder + new_daily_increment
```

Do not use the Radha doubling formula for reading.

### Streak and Day Boundary

- Streak requires target met or exceeded; partial completion resets streak.
- Day boundary is 3 AM local time. Before 3 AM, entries still belong to the previous devotional day.
- Editing a past day requires recomputing subsequent targets/streaks.

---

## Architecture

Active build: `v2-native-kotlin/`.

Why v2 exists:

- v1 React/Capacitor had WebView performance problems, including 60fps and haptic latency issues.
- Native Android gives GPU/haptic control.

Build layers:

```text
Layer 1 — Design System:      COMPLETE
Layer 2 — Data Engine:        COMPLETE
Layer 3 — 3D Engine:          IN PROGRESS
Layer 4 — Core Screens:       IN PROGRESS
Layer 5 — Secondary Screens:  NOT STARTED
Layer 6 — Platform Features:  NOT STARTED
```

Important package areas:

```text
v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/
├── di/               Hilt modules
├── data/local/       Room, DAO, DataStore
├── data/repository/  JapRepository and persistence orchestration
├── domain/engine/    TargetEngine, StreakEngine, DayBoundary, carry-over engines
├── domain/model/     Section models and IDs
├── domain/util/      Indian number formatting
├── engine/           Mala/3D/haptics
├── platform/         Android alarms/backup/receivers when present
└── ui/               theme, components, screens, navigation
```

Use the repo's existing patterns and theme system before adding new abstractions.

---

## Design System Rules

Default theme: Sharad Moon.

Core rules:

- Do not hardcode colors or spacing in composables.
- Use `NaamSmaranTheme`, `LocalNaamSmaranColors`, `Color.kt`, `Dimens.kt`, and related theme tokens.
- Do not use Material default purple/pink/teal palette.
- Do not use `shadow()` elevation for glass depth; use the project's glass/blur approach.
- Cards should respect the project radius rules.
- Text opacity hierarchy is fixed: primary 100%, secondary 65%, tertiary 38%.
- Animations should communicate state changes, not decorate randomly.
- Use Indian number formatting in UI counts.

Read `agents/05-design-system.md` before UI/styling work.

---

## Screen Direction

### Home Zero-UI Darshan

The Home screen should feel like an immersive devotional canvas:

- Full-screen dark canvas using theme background.
- Background image showreel from devotional/user images.
- Floating counter number.
- Tap anywhere = +1 naam jap.
- Swipe up = glass bottom sheet with quick-add controls/stats/navigation.
- No visible control panel on the resting surface.
- No progress ring on the main canvas.
- No visible buttons on the surface.

Recent work has focused on the bottom sheet/dashboard presentation and the staged swipe behavior.

### Naam Jap

Section 1 has two distinct tasks:

- Track A: Harivansh mala.
- Track B: Radha count.

Keep formulas and UI labels clear so these are not accidentally mixed.

### 3D Mala

The intended 3D mala is vertical-perspective, viewed like holding a mala in the lap:

- Sumeru bead at 12 o'clock.
- Thumb-arc gesture on bottom half of the screen.
- SceneView/Filament for 60fps rendering.
- Deferred/future if not ready.

---

## Platform and Security Constraints

This is a local personal app:

- No server.
- No public API.
- No social sharing.
- No analytics or external tracking.
- Do not suggest publishing or making it multi-user.

Platform plans:

- Notifications via WorkManager/Alarm scheduling and channels.
- Google Drive backup as one `backup.json` in Drive AppData.
- OAuth tokens must use encrypted storage, not plain preferences.
- Audio via ExoPlayer from a designated app directory, with sanitized filenames.

Data integrity is the main security concern. Room writes should be atomic, and restore flows should recompute derived targets/streaks.

---

## Current Implementation Direction

Use `implementation_plan.md` as the current correctness plan. It covers:

- P0: formula defaults and tests.
- P1: section IDs, Room migration/schema, route remap, new section scaffolds, Home grid update.
- P2: theme overlay tokens and hardcoded color cleanup.

Strict plan rule:

- Verify P0 before P1.
- Verify migration/schema before P2.
- Do not start later layers before earlier layers are stable.

Use `KNOWN_ISSUES.md` as the active bug/UX backlog. Major current issues include gallery/sheet freeze, fresh-state progress bugs, missing Vrindavan Shat Leela input, and non-functional settings controls.

---

## Workflow for Future Agents

Start every session by reading:

1. `AGENTS.md`.
2. `agents/00-current-status.md`.
3. The numbered module(s) relevant to the task.

Then:

1. Check `git status --short`.
2. Read existing changed files before editing them.
3. Preserve user changes.
4. Work only in `v2-native-kotlin/` unless the user explicitly asks about archived builds.
5. Keep edits scoped to the requested behavior.
6. Update `agents/00-current-status.md` when a meaningful handoff point is reached.

If doing UI/design work, load `01-sampraday.md`, `05-design-system.md`, and relevant screen docs.

If doing engine/formula/data work, load `02-formula.md`, `03-sections.md`, and `04-architecture.md`.

If doing platform work, load `07-platform.md`.

---

## Verification Expectations

Common local checks from `v2-native-kotlin/`:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
git diff --check
```

For UI/device work:

- Install the generated debug APK.
- Launch the app.
- Capture screenshot(s) from the connected device.
- Verify no forbidden symbols are visible.
- Verify Home tap/swipe/quick-add behavior on device, not just in code.

Do not claim a fix is complete without saying what was verified and what remains unverified.

---

## Canonical Context Map

| File | Use for |
|---|---|
| `AGENTS.md` | Hub, identity, module index, quick rules |
| `agents/00-current-status.md` | Returning-agent handoff |
| `agents/00-full-context.md` | New-agent onboarding |
| `agents/01-sampraday.md` | Religious context, symbols, visual restrictions |
| `agents/02-formula.md` | Formula, streak, day boundary, recomputation |
| `agents/03-sections.md` | All 7 section specs |
| `agents/04-architecture.md` | Layers, folders, dependencies, schemas |
| `agents/05-design-system.md` | Theme, glass, typography, UI rules |
| `agents/06-screens.md` | Home, Mala, day-end specs |
| `agents/07-platform.md` | Notifications, backup, audio, security |
