# Consolidated Implementation Plan — Naam Smaran Correctness Pass

> **Source documents merged:** Revised Plan B, Binding Amendments Review, Final Implementation Plan, agents/02-formula.md, agents/03-sections.md, agents/05-design-system.md
>
> **Scope:** P0–P2 correctness and structural fixes. No new libraries. No polish pass.
>
> **Estimated effort:** ~4.5 hours

---

## P0: Formula Correction

### P0.1 — Fix INCREMENT defaults

#### [MODIFY] [TargetEngine.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/domain/engine/TargetEngine.kt)

- Line 35: `increment: Int = 1000` → `increment: Int = 5000`
- Line 54: `increment: Int = 1000` → `increment: Int = 5000`
- Lines 9–16: Replace verification table with canonical table from `agents/02-formula.md`:

```
| Day | Target   | Did       | Case     | Next Target |
|-----|----------|-----------|----------|-------------|
| 1   | 21,600   | 25,000    | exceeded | 30,000      |
| 2   | 30,000   | 30,000    | exact    | 35,000      |
| 3   | 35,000   | 20,000    | deficit  | 50,000      |
| 4   | 50,000   | 0         | missed   | 1,00,000    |
| 5   | 1,00,000 | 0         | missed   | 2,00,000    |
| 6   | 2,00,000 | 2,10,000  | exceeded | 2,15,000    |
```

#### [MODIFY] [AppSettingsStore.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/data/local/AppSettingsStore.kt)

- Line 69: `?: 1000` → `?: 5000`
- Add one-time migration: if stored value == `1000`, upgrade to `5000`; preserve any other custom value.

### P0.2 — Formula unit tests

#### [NEW] TargetEngineTest.kt

Test every row of the verification table above plus edge cases: `D(n) = 0`, `D(n) = T(n) - 1`, `D(n) = T(n)`, `D(n) = T(n) + 1`.

---

## P1: Sections, Routes, Schema

### P1.1 — DevotionalSectionId enum

#### [NEW] DevotionalSectionId.kt

Location: `domain/model/DevotionalSectionId.kt`

```kotlin
enum class DevotionalSectionId {
    NAAM_JAP,           // Sec 1: नाम जप (dual-track)
    HIT_CHAURASI,       // Sec 2: श्री हित चतुरसी जी
    RADHA_SUDHANIDHI,   // Sec 3: श्री हित राधा सुधानिधी जी
    SEVAK_VAANI,        // Sec 4: श्री हित सेवक वाणी
    ASHTAYAM_SEVA,      // Sec 5: अष्टयाम सेवा पद्धति
    NITYA_PATH,         // Sec 6: नित्य पाठ रसोपासना
    VRINDAVAN_SHAT_LEELA // Sec 7: श्री वृंदावन शत लीला
}
```

### P1.2 — Room v1→v2 migration

#### [MODIFY] [DailyRecord.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/data/local/entity/DailyRecord.kt)

Add 7 new columns (keep 6 old ones for backward compat):

```kotlin
// New v2 section columns (aligned to DevotionalSectionId)
val checkHarivanshMala: Boolean = false,       // Track A
val checkHitChaurasi: Boolean = false,         // Sec 2
val checkRadhaSudhanidhi: Boolean = false,     // Sec 3
val checkSevakVaani: Boolean = false,          // Sec 4
val checkAshtayamSeva: Boolean = false,        // Sec 5
val checkNityaPath: Boolean = false,           // Sec 6
val checkVrindavanShatLeela: Boolean = false,  // Sec 7
```

Keep `checkNaamJap` (Track B). Keep old 5 stale columns — do NOT drop in this migration.

#### [MODIFY] [NaamSmaranDatabase.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/data/local/NaamSmaranDatabase.kt)

- `version = 1` → `version = 2`
- `exportSchema = false` → `exportSchema = true`
- Add migration object with 7 `ALTER TABLE daily_records ADD COLUMN` statements
- Add `UPDATE daily_records SET checkHitChaurasi = checkChaturasi` to preserve data
- Add schema export directory in `build.gradle.kts`

### P1.3 — Screen & route remap

#### [MODIFY] [Screen.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/navigation/Screen.kt)

Rename route objects to match 7-section spec. Add 2 new routes:

| Index | Route Object | Route String | Hindi Label |
|-------|-------------|--------------|-------------|
| 0 | `NaamJap` | `section/naamjap` | नाम जप |
| 1 | `HitChaurasi` | `section/hitchaurasi` | श्री हित चतुरसी जी |
| 2 | `RadhaSudhanidhi` | `section/sudhanidhi` | श्री हित राधा सुधानिधी जी |
| 3 | `SevakVaani` | `section/sevakvaani` | श्री हित सेवक वाणी |
| 4 | `AshtayamSeva` | `section/ashtayam` | अष्टयाम सेवा पद्धति |
| 5 | `NityaPath` | `section/nityapath` | नित्य पाठ रसोपासना |
| 6 | `VrindavanShatLeela` | `section/shatleela` | श्री वृंदावन शत लीला |
| 7 | `Settings` | `settings` | सेटिंग्स |

Update `fromSectionIndex()` accordingly (0–6 = devotional, 7 = Settings).

#### [NEW] AshtayamSevaScreen.kt + [NEW] NityaPathScreen.kt

Create safe scaffold screens (using existing `SectionScaffold` pattern) for the 2 missing sections. Minimal: title + "जल्द आएगा" placeholder.

#### [MODIFY] [NaamSmaranApp.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/navigation/NaamSmaranApp.kt)

- Rename all `composable(Screen.Kirtan.route)` etc. to new route objects
- Add 2 new `composable()` entries for `AshtayamSeva` and `NityaPath`
- Update existing screen imports for renamed files (Kirtan→HitChaurasi, etc.)

#### [MODIFY] Existing screen files

Rename folders/files to match new names:
- `kirtan/` → `hitchaurasi/` (KirtanScreen → HitChaurasi Screen)
- `maharas/` → `sudhanidhi/` (MaharasScreen → SudhanidhiScreen)
- `lalita/` → `sevakvaani/` (LalitaScreen → SevakVaaniScreen)
- `chaturdas/` → `shatleela/` (ChaturdasScreen → ShatLeelaScreen)
- `harivansh/` → keep as `harivansh/` (HarivanshScreen — but update route)

### P1.4 — HomeScreen grid update

#### [MODIFY] [HomeScreen.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/screens/home/HomeScreen.kt)

Replace `allSections` (lines 322–330) with 8 tiles matching the spec. Remove emoji navigation — use spec-correct Hindi labels:

```kotlin
private val allSections = listOf(
    SectionEntry("📿", "नाम जप", 0),
    SectionEntry("📖", "श्री हित\nचतुरसी जी", 1),
    SectionEntry("📜", "राधा\nसुधानिधी जी", 2),
    SectionEntry("🙏", "सेवक\nवाणी", 3),
    SectionEntry("🕐", "अष्टयाम\nसेवा", 4),
    SectionEntry("📿", "नित्य पाठ\nरसोपासना", 5),
    SectionEntry("🌸", "वृंदावन\nशत लीला", 6),
    SectionEntry("⚙️", "सेटिंग्स", 7)
)
```

> [!NOTE]
> Emoji are temporary placeholders (P2 will replace with sampraday-safe marks). The labels are what matter now.

---

## P2: Theme Token Cleanup

### P2.1 — Add overlay tokens

#### [MODIFY] [Color.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/theme/Color.kt)

Add after line 92:

```kotlin
// Overlay/scrim colors
val OverlayLight = Color(0x73000000)  // ~45% black — badges, light scrims
val OverlayHeavy = Color(0xE0000000)  // ~88% black — showreel bottom scrim
val TextEmphasis = Color(0xFFFFFFFF)  // 100% white — hero counter digits
```

#### [MODIFY] [Theme.kt](file:///c:/Users/Harsh/OneDrive/Desktop/Naam%20Jap/v2-native-kotlin/app/src/main/java/com/radhavallabh/naamsmaran/ui/theme/Theme.kt)

Add to `NaamSmaranColorScheme`:

```kotlin
val overlayLight: Color = OverlayLight,
val overlayHeavy: Color = OverlayHeavy,
val textEmphasis: Color = TextEmphasis,
```

### P2.2 — Replace hardcoded colors

#### [MODIFY] HomeScreen.kt

Replace all 11 `Color.White.copy()` + 1 `Color.Black.copy()` with theme tokens via `LocalNaamSmaranColors.current`:

| Current | Replacement |
|---------|-------------|
| `Color.White` (100%) | `colors.textEmphasis` |
| `Color.White.copy(alpha = 0.90f)` | `colors.textPrimary` |
| `Color.White.copy(alpha = 0.55f)` | `colors.textSecondary` |
| `Color.White.copy(alpha = 0.38f)` | `colors.textTertiary` |
| `Color.Black.copy(alpha = 0.45f)` | `colors.overlayLight` |

#### [MODIFY] SectionNavButton.kt, QuickAddButton.kt

Same pattern — replace `Color.White.copy()` with `colors.textPrimary`.

#### [MODIFY] ImageShowreelBackground.kt

Replace gradient stops: `Color.Black.copy(0.25f)` → `colors.overlayLight`, `Color.Black.copy(0.88f)` → `colors.overlayHeavy`.

### P2.3 — Devanagari typography lock

Verify in `Type.kt`: no negative `letterSpacing`, Hindi text uses generous `lineHeight`. Fix if violated.

---

## Verification Plan

### Unit Tests
- `TargetEngineTest`: all 6 rows of verification table + edge cases
- `StreakEngineTest`: streak reset on miss, increment on meet
- `DayBoundaryEngineTest`: 3AM boundary, 2:59 AM = yesterday, 3:00 AM = today

### Migration Test
- Room v1→v2: existing `did`, `target`, `checkNaamJap` preserved; `checkChaturasi` mapped to `checkHitChaurasi`; 7 new columns default to `false`

### Manual QA Checklist
- [ ] Home tap +1, double tap +108
- [ ] Swipe-up opens bottom sheet with 8+1 tiles (7 sections + settings + gallery)
- [ ] Each of 7 devotional tiles routes to correct screen
- [ ] Settings tile routes to settings (not a devotional section)
- [ ] No forbidden symbols visible
- [ ] No `Color.White` / `Color.Black` literals in modified files
- [ ] Hindi labels match `agents/03-sections.md` exactly
- [ ] Grid fits on small screen (360dp width)

---

## Explicit Deferrals

**Not in this pass:** Haze blur, Ken Burns showreel, haptic ladder expansion, Macrobenchmark, Baseline Profiles, Drive backup, audio, notifications, Glance widgets, Lottie, FTS4, DataStore Proto, custom Lint rules, emoji→icon replacement (P3+).

---

## Implementation Order

```
P0.1  Fix INCREMENT defaults (TargetEngine + AppSettingsStore)     ~15 min
P0.2  Update verification table comment                           ~10 min
P0.3  Write TargetEngineTest                                      ~30 min
P1.1  Create DevotionalSectionId enum                             ~15 min
P1.2  DailyRecord v2 schema + Room migration + migration test     ~60 min
P1.3  Rename Screen.kt routes + create 2 new screens              ~45 min
P1.4  Update NaamSmaranApp.kt nav graph                           ~15 min
P1.5  Update HomeScreen allSections grid (7+1 tiles)              ~15 min
P2.1  Add overlay tokens to Color.kt + Theme.kt                  ~15 min
P2.2  Replace hardcoded colors in Home + components               ~30 min
P2.3  Devanagari typography audit                                 ~10 min
      ─────────────────────────────────────────────────────────
      TOTAL                                                      ~4.5 hrs
```

> [!IMPORTANT]
> **Strict ordering:** P0 must be verified (tests green) before starting P1. P1 migration must be tested before P2 begins. No skipping ahead.

---

**जय श्री हित हरिवंश महाप्रभु 🙏**
