# 00 — Current Status Handoff

> **Audience:** agents already involved in Naam Smaran work.
> **Last updated:** 2026-05-25 by Codex via `naam-smaran-context`.
> **Canonical sources:** [AGENTS.md](../AGENTS.md) and numbered modules `01`–`07`.

---

## Quick Lock-In

- Active build: `v2-native-kotlin/`.
- Stack: Kotlin + Jetpack Compose + Room + Hilt + SceneView/Filament.
- Default theme: Sharad Moon.
- Formula source of truth: `agents/02-formula.md`; Radha increment is `5,000`.
- Sections: exactly 7 devotional sections, not 6.
- UI/design restriction: Radhavallabh-only visual world; no non-sampraday symbols.
- Worktree is dirty. Preserve existing user/agent changes.

---

## Where We Left Off

Most recent observed work was on the native Android app, especially the Home Zero-UI/bottom-sheet/dashboard flow. The latest handoff note in `v2-native-kotlin/# Fix Android launch crash.md` says:

- Premium dashboard pass was implemented in `HomeScreen.kt` and `GlassBottomSheet.kt`.
- `Color.kt` and `Dimens.kt` received centralized tokens for the dashboard work.
- `.\gradlew.bat testDebugUnitTest`, `.\gradlew.bat assembleDebug`, and `git diff --check` had passed at that time.
- APK installed and launched successfully on the connected phone.
- Screenshot artifact: `naamsmaran-installed-check.png`.

Treat that as the latest narrative handoff, but re-run verification before claiming the current tree is clean because more files are now dirty/untracked.

---

## Current Phase

Recorded project phase:

```text
Layer 1 — Design System:      COMPLETE
Layer 2 — Data Engine:        COMPLETE
Layer 3 — 3D Engine:          IN PROGRESS
Layer 4 — Core Screens:       IN PROGRESS
Layer 5 — Secondary Screens:  NOT STARTED
Layer 6 — Platform Features:  NOT STARTED
```

Active focus:

- HomeScreen Zero-UI rewrite and NaamJap section.
- Correctness pass from `implementation_plan.md`.
- Known UI/UX backlog from `KNOWN_ISSUES.md`.

---

## Active Implementation Plan

Use [implementation_plan.md](../implementation_plan.md) as the current ordered plan.

Strict ordering from that plan:

1. P0 formula correction and tests.
2. P1 section IDs, Room migration/schema, route remap, Home grid update.
3. P2 theme token cleanup and hardcoded color cleanup.

Do not skip ahead. P0 should be verified before P1; P1 migration should be tested before P2.

---

## Latest Observed Files

Before this context refresh, the newest non-build files under `v2-native-kotlin/` were:

| Observed time | File |
|---|---|
| 2026-05-19 12:24 | `v2-native-kotlin/# Fix Android launch crash.md` |
| 2026-05-19 12:10 | `v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/theme/Color.kt` |
| 2026-05-19 12:10 | `v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/theme/Dimens.kt` |
| 2026-05-19 12:09 | `v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/screens/home/HomeScreen.kt` |
| 2026-05-19 12:09 | `v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/screens/home/HomeViewModel.kt` |

Many files from 2026-05-18 are also untracked/modified, including tests and platform scaffolding.

---

## Dirty Worktree Snapshot

Observed with `git status --short` on 2026-05-25:

- Tracked modified files include `agents/06-screens.md`, v1 archived files, and many `v2-native-kotlin` app files.
- Major native files touched include:
  - `AppSettingsStore.kt`
  - `DailyRecordDao.kt`
  - `JapRepository.kt`
  - `DevotionalSectionId.kt`
  - `GlassBottomSheet.kt`
  - `HomeScreen.kt`
  - `HomeViewModel.kt`
  - section screens
  - `Color.kt`, `Dimens.kt`, `Type.kt`
  - `libs.versions.toml`
- Untracked native additions include:
  - `CarryOverEngine.kt`
  - `NityaPathProgressCalculator.kt`
  - `CarryOverSection.kt`
  - `SettingsViewModel.kt`
  - `SheetStage.kt`
  - `ui/screens/sections/`
  - `platform/`
  - test files for carry-over, nitya path, backup, and home sheet state
  - debug/release source-set helpers
- Untracked screenshots exist at repo root.

Do not revert or overwrite these unless the user explicitly asks.

---

## Known Blockers / Bugs

From [KNOWN_ISSUES.md](../KNOWN_ISSUES.md), highest priority:

- C1: app freezes after gallery image upload.
- C2: app freezes when closing bottom sheet.
- C3: Chaturasi shows 50% progress by default.
- C4: Nitya Path calendar/streak pre-filled.
- C5: Vrindavan Shat Leela has no input method.
- C6: Settings controls are non-functional.

The latest handoff says C1/C2 and dashboard UX were worked on and installed successfully, but keep the issue list until verified on the current tree/device.

---

## Next Recommended Actions

1. Re-run `git status --short` and read any newly touched files before editing.
2. If continuing the correctness pass, start with `implementation_plan.md` P0 and verify `TargetEngineTest`.
3. If continuing UI/UX work, inspect `HomeScreen.kt`, `GlassBottomSheet.kt`, and `SheetStageMachineTest.kt` first.
4. Run unit tests before claiming completion.
5. For device QA, build/install the debug APK and capture screenshots only after install succeeds.

Useful verification commands from `v2-native-kotlin/`:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
git diff --check
```

---

## Session Start Reminder

Before app work, confirm:

1. Active build is `v2-native-kotlin/`.
2. Default theme is Sharad Moon.
3. Formula is from `agents/02-formula.md` and must not change.
4. No hardcoded colors in composables.
5. There are 7 devotional sections.
6. No non-Radhavallabh symbols in UI/design work.
