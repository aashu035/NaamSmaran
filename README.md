<div align="center">

# नाम स्मरण (Naam Smaran)

**A personal sadhana tracker rooted in the Shri Radhavallabh Sampraday**

[![License: MIT](https://img.shields.io/badge/License-MIT-gold.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](v2-native-kotlin/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](#tech-stack)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)](#tech-stack)
[![Version](https://img.shields.io/badge/Version-2.2.0-brightgreen)](#development-status)
[![Status](https://img.shields.io/badge/Status-Active%20Development-orange)](#development-status)

_जय जय महाप्रभु श्री हित हरिवंश चंद्र जू महाराज की 🙏_

---

</div>

## About

**Naam Smaran** (नाम स्मरण) is a private, non-commercial Android application built as a personal devotional tracker for daily _sadhana_ (spiritual practice) in the tradition of [Shri Hit Harivansh Mahaprabhu](https://en.wikipedia.org/wiki/Hit_Harivansh) — the founder of the **Radhavallabh Sampraday**.

The app tracks seven distinct devotional sections across reading, chanting, and service, with an adaptive target system that responds to the practitioner's daily discipline.

> **Note:** This is NOT a published app. It is a personal tool built by a single developer for their own devotional use. The source code is shared for transparency and as a reference for native Android development with Jetpack Compose.

---

## Devotional Sections

The app is organized around **7 core sections** of daily practice:

| #   | Section                                                  | Type     | Description                                                                                  |
| :-- | :------------------------------------------------------- | :------- | :------------------------------------------------------------------------------------------- |
| 1   | **नाम जप** (Naam Jap)                                    | Chanting | Dual-track naam jap — _Radha_ naam (Track B, doubling formula) + _Harivansh_ naam (Track A, mala-based) |
| 2   | **श्री हित चतुरसी जी** (Chaturasi)                       | Reading  | 84 devotional verses (_pad_) with daily carry-over targets                                   |
| 3   | **श्री हित राधा सुधानिधि जी स्तोत्र** (Radha Sudhanidhi) | Reading  | Devotional _stotra_ recitation with progress tracking                                        |
| 4   | **अष्टयाम सेवा** (Ashtayam Seva)                         | Service  | Eight-fold daily service checklist aligned to temple schedule timings                        |
| 5   | **नित्य पाठ रसोपासना** (Nitya Paath)                     | Reading  | Daily _rasopasana_ reading with streak tracking and monthly calendar                         |
| 6   | **श्री वृंदावन शत लीला** (Vrindavan Shat Leela)          | Reading  | 100 sacred _leela_ narratives with progress tracking                                         |
| 7   | **सेटिंग्स** (Settings)                                  | System   | Themes, notifications, haptic feedback, backup & restore                                     |

---

## Key Features

- **Zero-UI Home Screen** — Full-screen darshan with immersive devotional imagery and invisible tap counter
- **Adaptive Target System** — Intelligent doubling formula (`T(n+1) = D(n) >= T(n) ? D(n) + 5000 : T(n) + (T(n) - D(n))`) that adjusts daily targets based on actual performance
- **Dual-Track Naam Jap** — Separate tracking for _Radha_ naam (continuous count, doubling formula) and _Harivansh_ naam (mala-based, 11/day)
- **Carry-Over Logic** — Unfinished readings carry forward with new increments (no doubling for reading sections)
- **Day Boundary at 3 AM** — Recognizes that sadhana often extends past midnight
- **Gallery Showreel** — Upload personal darshan images to cycle as the home background
- **Multi-Theme Support** — शरद मून (Sharad Moon), वृंदावन स्प्रिंग (Vrindavan Spring), यमुना नाइट (Yamuna Night)
- **Glassmorphism Design** — Frosted-glass card aesthetic with Material 3 dynamic theming
- **Hindi-First Interface** — All UI text in Devanagari; designed for a native Hindi devotional experience
- **Haptic Engine** — Custom vibration patterns for tap feedback
- **Indian Number Formatting** — Counts displayed in Indian system (1,00,000)

---

## Tech Stack

| Layer                    | Technology                   |
| :----------------------- | :--------------------------- |
| **Language**             | Kotlin 2.0                   |
| **UI Framework**         | Jetpack Compose + Material 3 |
| **Architecture**         | MVVM + Repository Pattern    |
| **Dependency Injection** | Hilt (KSP)                   |
| **Database**             | Room (SQLite)                |
| **Preferences**          | DataStore (Proto)            |
| **3D Rendering**         | SceneView / Filament         |
| **Image Loading**        | Coil Compose                 |
| **Navigation**           | Compose Navigation           |
| **Build System**         | Gradle KTS + Version Catalog |
| **Min SDK**              | 26 (Android 8.0 Oreo)        |
| **Target SDK**           | 36                           |
| **Compile SDK**          | 36                           |

---

## Architecture

The app follows **MVVM + Repository** pattern with Hilt dependency injection:

```
UI Layer (Compose Screens)
        ↕
ViewModel Layer (StateFlow, business logic)
        ↕
Repository Layer (single source of truth)
        ↕
Data Sources: Room DB ←→ DataStore Preferences
```

### Formula Engine (Track B — राधा Naam Jap)

The core adaptive target formula:

```
T(n+1) = D(n) >= T(n)  →  D(n) + 5,000        (goal met: reward + increment)
T(n+1) = D(n) < T(n)   →  T(n) + (T(n) - D(n)) (goal missed: penalty = deficit)
```

- **Default initial target:** 21,600
- **INCREMENT:** 5,000 per day when goal is met
- **Day boundary:** 3:00 AM (sadhana spans midnight)

---

## Project Structure

```
NaamSmaran/
├── v2-native-kotlin/                    # ✅ Active codebase
│   └── app/src/main/java/com/radhavallabh/naamsmaran/
│       ├── data/
│       │   ├── local/
│       │   │   ├── dao/                 # Room DAOs
│       │   │   ├── entity/              # DB entities (DailyRecord, etc.)
│       │   │   └── NaamSmaranDatabase.kt
│       │   ├── repository/              # Repository implementations
│       │   └── datastore/               # DataStore preferences
│       ├── di/                          # Hilt modules
│       ├── engine/
│       │   ├── FormulaEngine.kt         # Adaptive target formula
│       │   └── HapticEngine.kt          # Custom haptic patterns
│       ├── ui/
│       │   ├── components/              # Reusable Compose components
│       │   │   ├── GlassBottomSheet.kt
│       │   │   ├── SectionNavButton.kt
│       │   │   ├── QuickAddButton.kt
│       │   │   ├── AnimatedCounter.kt
│       │   │   └── TapScaleModifier.kt
│       │   ├── navigation/              # NavGraph, AppNavigation
│       │   ├── screens/
│       │   │   ├── home/                # HomeScreen + HomeViewModel
│       │   │   ├── naamjap/             # Naam Jap section
│       │   │   ├── kirtan/              # Chaturasi section
│       │   │   ├── maharas/             # Radha Sudhanidhi section
│       │   │   ├── ashtayamseva/        # Ashtayam Seva section
│       │   │   ├── nityapath/           # Nitya Paath section
│       │   │   ├── vrindavanlila/       # Vrindavan Shat Leela section
│       │   │   └── settings/            # Settings section
│       │   └── theme/                   # Design system, colors, typography
│       └── util/                        # Extensions, formatters, constants
├── v0-prototype/                        # 🗄️ Archived — early HTML prototype
├── v1-react-capacitor/                  # 🗄️ Archived — React + Capacitor attempt
├── agents/                              # AI agent context modules (for dev use)
│   ├── 01-sampraday.md                  # Religious context & visual rules
│   ├── 02-formula.md                    # Adaptive target formula spec
│   ├── 03-sections.md                   # All 7 section specifications
│   ├── 04-architecture.md               # Build layers & data schemas
│   ├── 05-design-system.md              # Design tokens & theming rules
│   ├── 06-screens.md                    # Screen-level specifications
│   └── 07-platform.md                   # Notifications, backup, audio
├── docs/                                # Screenshots and documentation assets
├── KNOWN_ISSUES.md                      # Bug tracker & improvement backlog
├── AGENTS.md                            # AI agent hub file
└── LICENSE                              # MIT License + Devotional Content Notice
```

---

## Development Status

```
Layer 1 — Design System       ████████████████████  COMPLETE
Layer 2 — Data Engine         ████████████████████  COMPLETE
Layer 3 — 3D Mala Engine      ██████████░░░░░░░░░░  IN PROGRESS
Layer 4 — Core Screens        █████████████████░░░  IN PROGRESS
Layer 5 — Secondary Screens   ░░░░░░░░░░░░░░░░░░░░  NOT STARTED
Layer 6 — Platform Features   ░░░░░░░░░░░░░░░░░░░░  NOT STARTED
```

| Screen            | Status         | Notes                              |
| :---------------- | :------------- | :--------------------------------- |
| Home Screen       | ✅ Built        | Zero-UI, gallery showreel, counter |
| Naam Jap          | 🔄 In Progress  | Dual-track, formula engine wired   |
| Chaturasi         | 🔄 In Progress  | 84 pads, carry-over logic          |
| Radha Sudhanidhi  | 🔄 In Progress  | Stotra list                        |
| Ashtayam Seva     | 🔄 In Progress  | 8 seva timings checklist           |
| Nitya Paath       | 🔄 In Progress  | Streak + calendar                  |
| Vrindavan Leela   | 🔄 In Progress  | 100 leelas, no input yet           |
| Settings          | 🔄 In Progress  | UI done; theme/notif not wired     |

**Current version:** `2.2.0` (versionCode: 3)

---

## Building from Source

### Prerequisites

- Android Studio Ladybug (2024.2+) or later
- JDK 17
- Android SDK 36

### Steps

```bash
# Clone the repository
git clone https://github.com/aashu035/NaamSmaran.git
cd NaamSmaran/v2-native-kotlin

# Open in Android Studio and sync Gradle
# OR build from command line (Linux/Mac):
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

The debug APK will be generated at:

```
v2-native-kotlin/app/build/outputs/apk/debug/NaamSmaran-v2.2.0-debug.apk
```

> **Note:** `local.properties` (SDK path) is not committed — Android Studio creates this automatically.

---

## Known Issues

See [KNOWN_ISSUES.md](KNOWN_ISSUES.md) for the full bug tracker and improvement backlog.

**Current critical issues:**
- App freeze after gallery image upload (C1)
- App freeze when closing bottom sheet (C2)
- Chaturasi shows 50% progress on fresh install (C3)
- Settings controls not functional (C6)
- Vrindavan Leela has no input method (C5)

---

## Design Philosophy

- **Sampraday Accurate:** No Om (ॐ), no trishul, no generic Hindu symbols. Only Radhavallabh tradition aesthetics.
- **Glassmorphism First:** Frosted glass cards, subtle backdrop blur, translucent surfaces throughout.
- **Hindi Only:** All user-facing text in Devanagari. English only for code and technical documentation.
- **No Hardcoded Colors:** Every color token flows from the theme system. Three complete themes included.
- **Indian Numbers:** Counts shown in Indian number system (1,00,000 not 100,000).

---

## Devotional Content Notice

This software is a personal devotional tool rooted in the traditions of the **Shri Radhavallabh Sampraday**, established by Shri Hit Harivansh Mahaprabhu Ji.

All religious texts, mantras, section names, and devotional content referenced within this application are **sacred to the Radhavallabh tradition**. Users and contributors are respectfully requested to treat this content with appropriate reverence.

**This is NOT an app for commercial distribution.** It is a personal sadhana tracker built for the developer's own devotional use.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">

**राधे राधे 🙏**

_Built with devotion, not for distribution._

_जय जय महाप्रभु श्री हित हरिवंश चंद्र जू महाराज की_

</div>
