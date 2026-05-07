# Naam Smaran — Testing & QA Guide
# जय श्री हित हरिवंश महाप्रभु 🙏

## Overview

This project uses a two-layer automated QA approach:

| Tool | Role | When |
|------|------|-------|
| **CodeRabbit** | AI code review — catches bugs, logic errors, style violations on every PR/push | On every GitHub Pull Request |
| **Ralph Loop** | Agentic dev loop — AI agent iterates, builds, tests until the task is verified done | For large feature work batches |

---

## 1. CodeRabbit Setup

### One-time Setup
1. Go to [coderabbit.ai](https://coderabbit.ai) and sign in with GitHub.
2. Install CodeRabbit on the `Naam Jap` repository.
3. The `.coderabbit.yaml` file in this directory already has project-specific rules.

### What CodeRabbit will catch
- `GlassBottomSheet` not reacting to `isExpanded` (already fixed, but guards against regression)
- Formula engine drift (doubling formula, 3AM day boundary)
- Hardcoded colors that bypass the theme system
- Symbols banned by the Sampraday (ॐ, trishul, etc.)
- Missing `LaunchedEffect` for reactive state changes
- Gesture areas being accidentally clipped by padding

### Custom commands (type in PR comments)
```
@coderabbitai review          — trigger a fresh review
@coderabbitai summary         — regenerate the PR summary
@coderabbitai generate tests  — suggest unit tests for changed code
@coderabbitai help            — list all commands
```

---

## 2. Ralph Loop (Agentic CI)

### Concept
The Ralph Loop is a shell-scripted AI agent loop that:
1. Reads a task list from `ralph-tasks.md`
2. Runs one task per iteration (builds, tests, verifies)
3. If the build/test fails → feeds the error back to the AI and retries
4. Only marks a task done when `./gradlew assembleDebug` exits 0

### Task File Format (`ralph-tasks.md`)
```markdown
- [ ] Implement NaamJapScreen with counter + quick-add
- [ ] Implement HarivanshScreen (reading carry-over)
- [ ] Day boundary unit tests (3AM rollover)
- [ ] Formula engine unit tests (doubling, INCREMENT=5000)
- [ ] Integrate Espresso smoke tests for HomeScreen gestures
```

### Running the Loop (Windows PowerShell)
```powershell
# Run from the v2-native-kotlin directory
.\scripts\ralph.ps1
```

See `scripts/ralph.ps1` below for the implementation.

---

## 3. Manual Verification Checklist (per APK build)

### HomeScreen
- [ ] App opens with **no black flash** (deep indigo background visible immediately)
- [ ] First showreel image appears within 1s of launch
- [ ] Single tap → counter appears showing `+1` (haptic bead click)
- [ ] Double tap → counter shows `+108` (mala haptic)
- [ ] Counter auto-hides after ~3.5 seconds of no interaction
- [ ] Swipe up (fast, ≥ halfway) → GlassBottomSheet slides up
- [ ] Bottom sheet has blur (Android 12+) or solid glass fallback
- [ ] Bottom sheet can be dragged to half / expanded / collapsed snap points
- [ ] Dismiss bottom sheet → counter re-appears for 3.5s

### GlassBottomSheet Content
- [ ] Stats row shows: किया / लक्ष्य / शेष / अखंडता
- [ ] Quick-add buttons: +१०८ / +१,००० / +५,०००
- [ ] All 7 sections visible in grid (📿 📖 🌸 🌙 🕯️ 🪷 ⚙️)
- [ ] Gallery tile opens image picker
- [ ] Selected gallery image appears in showreel on next cycle

### Formula Engine
- [ ] Day rolls over at 3:00 AM, not midnight
- [ ] When `did >= target`: next target = `did + 5000`
- [ ] When `did < target`: next target = `target + (target - did)` (penalty doubles)
- [ ] Streak increments only when `did >= target`

---

## 4. Build Commands

```powershell
# Full debug build
.\gradlew assembleDebug

# Run unit tests only
.\gradlew testDebugUnitTest

# APK location after build:
# app\build\outputs\apk\debug\NaamSmaran-v2.0.0-darshan-debug.apk
```

---

## 5. Known Issues & Workarounds

| Issue | Workaround |
|-------|-----------|
| MIUI blocks `adb install` | Transfer APK via MTP/ShareMe; install from file manager |
| `compileSdk 36` warning | Add `android.suppressUnsupportedCompileSdk=36` to `gradle.properties` (already done) |
| Blur not visible on Android < 12 | Expected — `SurfaceGlass` color provides fallback |
