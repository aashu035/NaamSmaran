# 03 — App Sections (All 7)

> **Parent:** [AGENTS.md](../AGENTS.md)
> **Last updated:** 2026-05-04 — Section restructure: correct names, targets, increment rules.

---

## Section Overview

| # | Hindi Name | Type | Daily Baseline | Increment (if met) | Pattern (if missed) |
|---|:---|:---|:---|:---|:---|
| 1 | नाम जप | Counter (dual-track) | See below ↓ | See below ↓ | Formula (see 02-formula.md) |
| 2 | श्री हित चतुरसी जी | Reading tracker | 12 पद | +6 पद | Carry over unfinished + new day |
| 3 | श्री हित राधा सुधानिधी जी | Reading tracker (with meaning) | 10 श्लोक | +5 श्लोक | Carry over unfinished + new day |
| 4 | श्री हित सेवक वाणी | Reading tracker (with meaning) | 5 छंद | +2 छंद | Carry over unfinished + new day |
| 5 | अष्टयाम सेवा पद्धति | Checklist | Min 3 seva times | — (flexible) | — |
| 6 | नित्य पाठ रसोपासना | Daily toggle | Complete daily | — | — |
| 7 | श्री वृंदावन शत लीला | Reading tracker | 10 छंद | +5 छंद | Continue from base (not doubled) |

---

## Section 1: नाम जप (Naam Jap — Dual-Track)

> ⚠️ This section contains **2 separate tasks** (not sub-sections). Each has its own
> counter, target, and formula engine.

### Track A: "राधावल्लभ श्री हरिवंश" Naam Jap

- **Unit:** माला (mala = 108 repetitions each)
- **Daily baseline:** 11 माला
- **If met:** +5 to +6 माला added to next day target
- **If missed:** Carry over deficit to next day target (standard formula)
- **Input modes:** Mala tracker (3D bead counter, future feature) + manual mala count

### Track B: "राधा" Naam Jap

- **Unit:** count (individual repetitions)
- **Daily baseline:** 21,600 (configurable in settings)
- **INCREMENT:** **5,000** (updated from 1,000)
- **Formula:** `T(n+1) = D(n) >= T(n) ? D(n) + 5000 : T(n) + (T(n) - D(n))`
- **If met:** next target = D(n) + 5,000
- **If missed:** target compounds (see [02-formula.md](./02-formula.md))
- **Input modes:** Quick-add, Custom number, Tap-per-Jap

### Naam Jap Input Modes (for Track B — "राधा" Naam Jap)

#### Mode 1: Quick-Add Buttons (via Home Bottom Sheet)
- **+108**, **+1,000**, **+5,000** buttons
- Each tap adds to daily total immediately
- Haptic feedback on each tap

#### Mode 2: Custom Number Input
- Numeric keypad entry
- User types any number → taps "जोड़ें" (Add)

#### Mode 3: Tap-per-Jap
- Full-screen single-tap counting
- Each tap = +1, haptic pulse + scale feedback
- Large touch area, no precision needed
- Live counter display: "3,456" updating in real time

#### Mode 4: Mala Tracker (3D Bead Counter)
- **Status: DEFERRED — future release**
- Placeholder UI with "जल्द आएगा" message
- See [06-screens.md](./06-screens.md) for full spec (preserved for future build)

---

## Section 2: श्री हित चतुरसी जी

- **Total length:** 84 पद (the complete Chaturasi text)
- **Daily target:** 12 पद to start
- **If met:** +6 पद added to next day target
- **If missed:** incomplete पद carried over, combined with next day's new target
- **Display:** "आज का पद: 42 / 84 पद पढ़े"
- Cycle restarts after completing all 84 पद
- Each पद is individually checkable in the list

---

## Section 3: श्री हित राधा सुधानिधी जी

- **Daily target:** 10 श्लोक (with meaning/अर्थ)
- **If met:** +5 श्लोक added to next day target
- **If missed:** incomplete श्लोक carried over + new target
- Display: "आज के श्लोक: 10 | अर्थ सहित"
- Notes field for reflections / अनुभव

---

## Section 4: श्री हित सेवक वाणी

- **Daily target:** 5 छंद (with meaning/अर्थ)
- **If met:** +2 छंद added to next day target
- **If missed:** incomplete छंद carried over + new target
- Notes field for reflections

---

## Section 5: अष्टयाम सेवा पद्धति

- **Checklist** of daily devotional service times
- **Minimum:** 3 seva times per day (morning, noon, evening/night)
- **Full schedule** (8 periods): Mangala, Shringar, Gwal, Raj Bhog, Utthapan, Bhog, Sandhya, Shayan
- Each item has a checkbox + optional timestamp
- Notes field: "आज की अनुभूति" (max 500 chars)
- **No target escalation** — it's a flexible checklist, not a counter

---

## Section 6: नित्य पाठ रसोपासना

- **Simple daily completion toggle** — done / not done
- No target escalation
- 7-day streak log visible on section screen
- Monthly calendar grid showing completion history
- Monthly stats: "इस महीने: 22 / 27 दिन"

---

## Section 7: श्री वृंदावन शत लीला

- **Daily target:** 10 छंद
- **If met:** +5 छंद added to next day target
- **If missed:** continue from current base (NOT doubled like Naam Jap formula)
- Notes field for reflections
- Progress tracked across the full text

---

## Missed Day Pattern (Sections 2–4 and 7)

> ⚠️ These sections use a **carry-over** pattern, NOT the doubling formula.
> Doubling formula is **only for Track B (Radha Naam Jap)**.

```
If missed:
  tomorrow's target = today's incomplete remainder + new daily increment
```

Example (Section 2): Target 12 पद, read 7 → tomorrow = 5 (remaining) + 12 (new) = 17 पद
