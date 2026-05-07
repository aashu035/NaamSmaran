# ralph-tasks.md — Naam Smaran v2-native-kotlin
# Ralph Loop task list. Each `- [ ]` is picked up automatically.
# जय श्री हित हरिवंश महाप्रभु 🙏
#
# Status key:
#   - [ ] = pending
#   - [/] = in progress
#   - [x] = done (verified by build + test passing)

## Phase 2B — Core Screens

- [x] Implement NaamJapScreen: show today's count, target, progress arc, + manual entry field
- [x] Implement HarivanshScreen: 11 mala/day tracker with carry-over (no doubling formula)
- [x] Implement KirtanScreen: reading carry-over tracker (unread pages + new increment)
- [x] Implement MaharasScreen: reading carry-over tracker
- [x] Implement LalitaScreen: reading carry-over tracker
- [x] Implement ChaturdasScreen: reading carry-over tracker
- [x] Implement SettingsScreen: day boundary display, theme switcher, gallery manager

## Phase 2C — Navigation

- [x] Wire all 7 section screens into NaamSmaranApp navigation back-stack
- [x] Add NavHost with section destinations (replace TODO in NaamSmaranApp)
- [x] Ensure back-press from any section returns to HomeScreen (not closes app)

## Phase 3A — Engine Tests

- [x] Unit test: formula doubling when did >= target (T+1 = did + 5000)
- [x] Unit test: formula penalty when did < target (T+1 = target + (target - did))
- [x] Unit test: day boundary rolls at 3:00 AM (not midnight)
- [x] Unit test: streak increments only on met-target days
- [x] Unit test: carry-over sections accumulate correctly (no doubling)

## Phase 3B — UI Smoke Tests (Espresso)

- [x] Espresso: HomeScreen renders without crash on cold start
- [x] Espresso: Single tap increments counter (verify text change)
- [x] Espresso: Swipe-up opens GlassBottomSheet
- [x] Espresso: Quick-add +108 button works in bottom sheet
- [x] Espresso: Gallery picker launches on tile tap

## Phase 4 — Platform Features

- [ ] Daily reminder notification at user-configured time (WorkManager)
- [ ] Auto-backup to local JSON on day-end
- [ ] SplashScreen API integration (remove default white flash on cold start)
