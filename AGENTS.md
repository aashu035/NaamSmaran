# AGENTS.md — Naam Smaran App: Hub File
# जय श्री हित हरिवंश महाप्रभु 🙏 | राधे राधे

> **This is the hub file.** Detailed context is split into modular files in `agents/`.
> Load only the module(s) relevant to your current task.
>
> **Last rewritten:** 2026-05-04 — Modular split for token efficiency.

---

## Quick Identity

- **App:** Naam Smaran (नाम स्मरण)
- **Package:** `com.radhavallabh.naamsmaran`
- **Stack:** Native Kotlin + Jetpack Compose + Room + Hilt + SceneView/Filament
- **Active folder:** `v2-native-kotlin/` (v0 and v1 are archived)
- **Purpose:** Personal devotional tracker. Never published. Personal APK only.
- **Sampraday:** Shri Hit Harivansh Mahaprabhu — Radhavallabh Sampraday
- **UI language:** Hindi (Devanagari). English only for technical areas.
- **Number format:** Indian system (1,00,000)

---

## Module Index

| File | Contents | Load When |
|:---|:---|:---|
| [00-current-status.md](agents/00-current-status.md) | Fast handoff: where we left off, latest files, plan, blockers | Resuming active work |
| [00-full-context.md](agents/00-full-context.md) | Full onboarding context for new/outside agents | First time on this project |
| [01-sampraday.md](agents/01-sampraday.md) | Religious context, visual world, forbidden symbols | Any UI/design work |
| [02-formula.md](agents/02-formula.md) | Target formula, streak, day boundary, recomputation | Any logic/engine work |
| [03-sections.md](agents/03-sections.md) | All 7 app sections with specs | Building any section screen |
| [04-architecture.md](agents/04-architecture.md) | Build layers, folder structure, dependencies, data schemas | Any structural work |
| [05-design-system.md](agents/05-design-system.md) | Golden rules, themes, glassmorphism, typography | Any UI/styling work |
| [06-screens.md](agents/06-screens.md) | Home Zero-UI, Mala 3D, Day-end analysis | Building specific screens |
| [07-platform.md](agents/07-platform.md) | Notifications, backup, audio, security, prohibitions | Platform feature work |

---

## The Formula (Quick Reference)

**Track B — "राधा" Naam Jap (doubling formula):**
```
T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
```
Default: initial=21,600, **INCREMENT=5,000** (updated), dayBoundary=3AM.

**Track A — "हरिवंश" Naam Jap:** 11 माला/day → +5–6 माला if met (carry-over if missed)
**Sections 2–4, 7 — Reading:** carry-over pattern (unread + new increment, NO doubling)

Full details + verification table → [02-formula.md](agents/02-formula.md)

---

## Current Build Status

```
Layer 1 — Design System:      [x] COMPLETE
Layer 2 — Data Engine:        [x] COMPLETE
Layer 3 — 3D Engine:          [/] IN PROGRESS
Layer 4 — Core Screens:       [/] IN PROGRESS
Layer 5 — Secondary Screens:  [ ] NOT STARTED
Layer 6 — Platform Features:  [ ] NOT STARTED
```

**Current phase:** HomeScreen Zero-UI rewrite + NaamJap section.

---

## Session Start Checklist

Every agent must confirm:
1. Active build is `v2-native-kotlin` (Kotlin + Compose + Room + Hilt)
2. Default theme is Sharad Moon (deep indigo, pink lotus)
3. The formula is in [02-formula.md](agents/02-formula.md) — never change it
4. No hardcoded colors — all values from theme system
5. There are **7** devotional sections, not 6
6. No Om (ॐ), no trishul, no non-Radhavallabh symbols — see [01-sampraday.md](agents/01-sampraday.md)
7. Read [00-current-status.md](agents/00-current-status.md) before resuming active work

---

**जय श्री हित हरिवंश महाप्रभु 🙏**
