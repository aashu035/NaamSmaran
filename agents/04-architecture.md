# 04 — Build Architecture & Project Structure

> **Parent:** [AGENTS.md](../AGENTS.md)

---

## Version History

| Ver | Folder | Stack | Status | Reason |
|:---:|:---|:---|:---:|:---|
| v0 | `v0-prototype` | Assets only | ❌ | No code |
| v1 | `v1-react-capacitor` | React+Vite+Three.js+Capacitor | ❌ | WebView killed 60fps; 20ms haptic lag |
| **v2** | **`v2-native-kotlin`** | **Kotlin+Compose+Hilt+Room+Filament** | **★ ACTIVE** | Native GPU, zero-latency haptics |

> v1 screenshots: `Assets/Images/V1 ScreenShots/`

---

## 6 Build Layers

**RULE: Never start Layer N+1 before Layer N is stable.**

### Layer 1 — Design System ✅ COMPLETE
`ui/theme/` — Color.kt, Theme.kt, Type.kt, Shape.kt, Dimens.kt

### Layer 2 — Data Engine ✅ COMPLETE
`data/` + `domain/` — Room entities, DAO, DataStore, TargetEngine, StreakEngine, DayBoundary, IndianNumberFormat, Hilt DI

### Layer 3 — 3D Engine 🔨 IN PROGRESS
`engine/` — MalaScene (needs vertical fix), MalaPhysics, MalaInputController (needs thumb-arc), HapticEngine ✅, ParticleBackground [NEW]

### Layer 4 — Core Screens 🔨 IN PROGRESS
`ui/screens/` — HomeScreen (needs Zero-UI rewrite), NaamJapScreen [NEW], HitChaurasi [NEW], RadhaSudhanidhi [NEW], SevakVaani [NEW], AshtayamSeva [NEW], NityaPath [NEW], ShatLeela [NEW]
**Build order:** HomeScreen → NaamJap → HitChaurasi → rest

### Layer 5 — Secondary Screens ❌ NOT STARTED
Calendar, History, DayEndModal, Settings, ThemeSelector

### Layer 6 — Platform Features ❌ NOT STARTED
NotificationService, AudioService, BackupService, WidgetProvider

---

## Current Status
```
Layer 1: [x] COMPLETE    Layer 4: [/] IN PROGRESS
Layer 2: [x] COMPLETE    Layer 5: [ ] NOT STARTED
Layer 3: [/] IN PROGRESS Layer 6: [ ] NOT STARTED
```
**Phase:** Transform HomeScreen to Zero-UI + Build NaamJap section.

---

## Dependencies (libs.versions.toml)

| Dep | Ver | Purpose |
|:---|:---|:---|
| AGP | 8.7.3 | Build |
| Kotlin | 2.1.0 | Language |
| Compose BOM | 2024.12.01 | UI |
| Room | 2.6.1 | DB |
| Hilt | 2.53.1 | DI |
| KSP | 2.1.0-1.0.29 | Annotation |
| DataStore | 1.1.1 | Prefs |
| Nav Compose | 2.8.5 | Navigation |
| Coroutines | 1.9.0 | Async |
| SceneView | 2.2.1 | 3D/Filament |

---

## Folder Structure

```
Naam Jap/
├── AGENTS.md               ← HUB
├── agents/                  ← MODULES (01–07)
├── Assets/Images/           ← Devotional images
├── v0-prototype/            ← ❌ ARCHIVED
├── v1-react-capacitor/      ← ❌ ARCHIVED
└── v2-native-kotlin/        ← ★ ACTIVE
    └── app/src/main/java/com/radhavallabh/naamsmaran/
        ├── di/              (Hilt)
        ├── data/local/      (Room entity, DAO, DB, DataStore)
        ├── data/repository/ (JapRepository)
        ├── domain/engine/   (TargetEngine, StreakEngine, DayBoundary)
        ├── domain/util/     (IndianNumberFormat)
        ├── engine/          (Mala3D, Haptics)
        └── ui/              (theme, components, screens, navigation)
```

---

## DailyRecord Room Entity

```kotlin
@Entity(tableName = "daily_records")
data class DailyRecord(
    @PrimaryKey val date: String,    // "YYYY-MM-DD"
    val target: Long = 21600L,       // T(n)
    val did: Long = 0L,              // D(n)
    val nextDayTarget: Long = 0L,
    val checkNaamJap: Boolean = false,
    val checkChaturasi: Boolean = false,
    val checkRadhaSudhanidhi: Boolean = false,
    val checkSevakVaani: Boolean = false,
    val checkAshtayamSeva: Boolean = false,
    val checkNityaPath: Boolean = false,
    val checkShatLeela: Boolean = false,
    val streakCount: Int = 0,
    // + dayOfWeek, quoteShown, notes, timestamps
)
```

### AppSettings (DataStore)
Defaults: theme="sharad-moon", mantra="राधा", target=21600, increment=1000, dayBoundary=3
