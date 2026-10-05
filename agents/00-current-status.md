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

## प्रातः संत नाम स्मरण (added 2026-10-05)

Morning saint-name recitation: a 04:00 alarm that opens a swipe-through reading screen of 138 verified names (+ opening, collective vandana, prayer, jaykara = 142 pages). Requested explicitly by the user, so it was built even though Layer 6 (platform) is still "not started" above.

- **Source of truth:** `Assets/For Claude code.zip` → `sant_smaran_bundle.zip` → extracted to `Assets/sant_smaran/` (gitignored). Read its `HANDOFF_CLAUDE_CODE.md` + `VERIFICATION.md` §5 before touching content. `Assets/sant_smaran.json` (loose) is a stale pre-verification draft — do not use.
- **Content rule:** never change, "fix", transliterate or re-spell any Devanagari string. `SantSmaranContentTest` pins `assets/sant_smaran.json` to a SHA-256; a content change must be user-approved and the hash updated deliberately.
- **Public repo:** `app/src/main/assets/saints/*.webp` (79 photos, screenshots from another app) is **gitignored** — never commit. Missing photos fall back to the decorative name-card, so a fresh clone still builds. The JSON stays tracked (the unanchored `Assets/` ignore rule also matches this folder, hence the `!` re-include in `.gitignore`).
- **Order (user-confirmed):** opening → sections 1–10 → collective vandana → prayer → last section (धाम, ब्रज एवं सखी वृंद) → jaykara. Built by `domain/engine/SantSmaranPageBuilder`.
- **Reading screen:** `ui/screens/santsmaran/` (Compose `HorizontalPager`, Coil from `file:///android_asset/`, bundled Noto Sans Devanagari via `SantDevanagari` in `Type.kt`, maroon/saffron/gold tokens in `SantSmaranColors`). Route `Screen.SantSmaran`; entry points are the Settings card and the alarm only — Home keeps its 7 sections.
- **Alarm:** `platform/santsmaran/`. `AlarmManager.setAlarmClock` → `SantAlarmReceiver` (reschedules tomorrow first) → `SantAlarmService` (`mediaPlayback` FGS: alarm-stream sound, vibration, full-screen notification) → `SantAlarmActivity` (show-when-locked; Snooze 10 min / "स्मरण आरंभ करें" → `MainActivity` with `EXTRA_OPEN_SANT_SMARAN`). `SantAlarmBootReceiver` re-arms on boot / locked boot / time + zone change / package update / exact-alarm permission change. Not WorkManager (no exact-time guarantee) and not `setExactAndAllowWhileIdle` (Doze-throttled).
- **Why separate prefs:** alarm config lives in **device-protected** SharedPreferences (`SantAlarmPrefs`), not `AppSettingsStore` (EncryptedSharedPreferences/AndroidKeystore cannot be read before first unlock, which would lose the alarm after an overnight reboot). Not part of the backup payload. The older 06:00 "दैनिक स्मरण" reminder is unchanged and independent.
- **Permissions checklist** lives in Settings (`SantSmaranSettingsCard`): notifications, exact alarm (12/12L only; 13+ has `USE_EXACT_ALARM`), full-screen intent (14+), battery optimisation, OEM autostart guide. The card also has a 10-second **test alarm** button.
- **Items awaiting the user's confirmation** (shipped verbatim): `VERIFICATION.md` §5 — #22, #23, #27, #28, #79, #93, #109.
- **Needs on-device verification** (cannot be proven in JVM tests): real lock-screen ring, Doze, reboot before first unlock, OEM autostart kills, and that the foreground-service start from the alarm broadcast is allowed on the user's Android version (a fallback full-screen notification channel exists if it is refused).

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
