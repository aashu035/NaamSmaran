# 02 — Core Formula & Logic Engines
# NEVER CHANGE WITHOUT EXPLICIT INSTRUCTION

> **Parent:** [AGENTS.md](../AGENTS.md)
> **Last updated:** 2026-05-04 — Radha INCREMENT updated to 5,000. Harivansh mala track added. Carry-over pattern documented.

---

## Formula Type 1: Radha Naam Jap (Track B — Doubling formula)

Applies to: **"राधा" naam jap count only**

```
T(n+1) = D(n) >= T(n) ? D(n) + INCREMENT : T(n) + (T(n) - D(n))
```

Where:
- **T(n)** = today's target
- **D(n)** = today's actual naam jap count (did)
- **T(n+1)** = tomorrow's target
- **INCREMENT** = **5,000** (updated; was 1,000)

> ⚠️ If missed, the deficit is ADDED to tomorrow's target — compounding like debt.
> No cap on maximum target — the formula runs pure.

### Verification Table — Test Against These (INCREMENT = 5,000)

| Day | Target | Did | Case | Next Target |
|-----|--------|-----|------|--------------|
| 1 | 21,600 | 25,000 | exceeded | 30,000 |
| 2 | 30,000 | 30,000 | exact | 35,000 |
| 3 | 35,000 | 20,000 | deficit 15k | 50,000 |
| 4 | 50,000 | 0 | missed | 1,00,000 |
| 5 | 1,00,000 | 0 | missed | 2,00,000 |
| 6 | 2,00,000 | 2,10,000 | exceeded | 2,15,000 |

**Initial target:** 21,600 (configurable in Settings)

### Implementation Location
`v2-native-kotlin/.../domain/engine/TargetEngine.kt`

---

## Formula Type 2: Harivansh Naam Jap (Track A — Mala count)

Applies to: **"राधावल्लभ श्री हरिवंश" naam jap (counted in माला)**

- **Unit:** 1 माला = 108 repetitions
- **Daily baseline:** 11 माला
- **If met:** +5 to +6 माला increase for next day
- **If missed:** deficit carries over (same carry-over pattern as sections 2–4)
- **Status:** Input via manual mala count for now; 3D bead counter deferred.

### Implementation Location
`v2-native-kotlin/.../domain/engine/MalaTargetEngine.kt` (to be built)

---

## Formula Type 3: Reading Sections (Carry-Over — Sections 2, 3, 4, 7)

Applies to: **श्री हित चतुरसी जी, राधा सुधानिधी जी, सेवक वाणी, वृंदावन शत लीला**

> ⚠️ These do NOT use the doubling formula. Missed work is carried forward.

```
If met:  tomorrow = today's target + INCREMENT
If missed: tomorrow = remaining_unread + INCREMENT
```

| Section | Daily Base | Increment |
|---------|------------|----------|
| चतुरसी जी | 12 पद | +6 पद |
| राधा सुधानिधी जी | 10 श्लोक | +5 श्लोक |
| सेवक वाणी | 5 छंद | +2 छंद |
| वृंदावन शत लीला | 10 छंद | +5 छंद |

**Example (चतुरसी जी):** Target 12 पद, read 7 → tomorrow = 5 (remaining) + 12 (new) = 17 पद

---

## Streak Definition

```
S(n) = D(n) >= T(n) ? S(n-1) + 1 : 0
```

Streak requires target MET or exceeded. Partial completion breaks streak.

### Implementation Location
`v2-native-kotlin/.../domain/engine/StreakEngine.kt`

---

## Day Boundary

```
effectiveDate = (currentHour < DAY_BOUNDARY_HOUR) ? yesterday : today
```

- **DAY_BOUNDARY_HOUR = 3** (configurable 1–5 AM in settings)
- At 2:30 AM, the app still treats it as the previous day for data entry purposes.

### Implementation Location
`v2-native-kotlin/.../domain/engine/DayBoundaryEngine.kt`

---

## Missed Day Auto-Fill

If no entry exists for a past day, treat **D(n) = 0**.
This means target doubles each consecutive missed day (T × 2 each day).

---

## Past Day Edit → Full Recomputation

When any past day's D(n) is edited:

1. Recompute that day's status (met/missed)
2. Recompute all subsequent targets in chain
3. Recompute all subsequent streak counts
4. Update lifetime statistics cache
5. If chain > 100 days: run on background thread, show progress indicator

---

## Indian Number Formatting

All numbers in the UI use Indian numbering system:
- `1,000` / `10,000` / `1,00,000` / `9,99,999`

### Implementation Location
`v2-native-kotlin/.../domain/util/IndianNumberFormat.kt`
