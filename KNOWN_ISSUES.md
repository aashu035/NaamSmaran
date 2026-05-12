# Known Issues & Improvement Backlog

> **Last reviewed:** v2.1.0-darshan | May 2026

This document tracks known bugs, UI improvements, and pending features identified during internal testing. Items are prioritized by severity.

---

## 🔴 Critical Bugs

### C1 — App Freezes After Gallery Image Upload
**Section:** Home Screen  
**Behavior:** After uploading a gallery image, all gestures stop responding — swipe, tap counter, and section navigation all freeze. The "राधे राधे" text also disappears. Requires force-close to recover.  
**Repro:** Upload any image via gallery → counter and swipe become unresponsive.

### C2 — App Freezes When Closing Bottom Sheet
**Section:** Home Screen  
**Behavior:** Swiping up twice (to fully expand the bottom sheet) then swiping down once causes the same freeze as C1 — no gestures work, "राधे राधे" disappears, counter non-functional.  
**Repro:** Open sheet fully → dismiss with one swipe down → freeze.

### C3 — चतुरसी Shows 50% Progress by Default
**Section:** श्री हित चतुरसी जी  
**Behavior:** The overall progress shows `42/84 — 50%` on a fresh install, even without any *pad* being marked as read.  
**Expected:** Progress should start at `0/84 — 0%`.

### C4 — नित्य पाठ Calendar Pre-Filled
**Section:** नित्य पाठ रसोपासना  
**Behavior:** The streak counter "लगातार शृंखला" and calendar "मई 2026" show pre-filled data on first launch.  
**Expected:** All streak and calendar data should start empty.

### C5 — श्री वृंदावन शत लीला Has No Input Method
**Section:** श्री वृंदावन शत लीला  
**Behavior:** There is no button or mechanism to add daily progress. The section is display-only with no way to record completed *leelas*.

### C6 — Settings Controls Non-Functional
**Section:** सेटिंग्स  
**Behavior:** Multiple settings controls do not work:
- Theme switching has no effect
- Notification time is not changeable for "दैनिक स्मरण"
- Data backup and restore buttons are non-functional

---

## 🟡 UI / UX Improvements

### U1 — Tap vs Double-Tap Sensitivity on Home Counter
**Section:** Home Screen  
**Behavior:** When doing rapid *jap* (e.g., short naam like "राधा"), the system incorrectly registers rapid single taps as double-taps (+108 instead of +1). The time threshold between single and double tap needs tuning for rapid chanting.

### U2 — Bottom Sheet First-Swipe Alignment
**Section:** Home Screen  
**Behavior:** The first swipe-up opens the sheet too little — the quick-add buttons (+108, +1,000, +5,000) are half-cut and unusable. Users must swipe a second time to access them.  
**Expected:** First swipe should open just enough to show quick-add buttons fully. Second swipe should show the full section grid.

### U3 — Gallery Upload Icon Barely Visible
**Section:** Home Screen → Bottom Sheet (fully expanded)  
**Behavior:** The gallery upload icon is nearly invisible against the sheet background. It functions correctly but requires tapping the extreme edge of the screen.

### U4 — Section Icons Invisible on Bottom Sheet
**Section:** Home Screen → Bottom Sheet (fully expanded)  
**Behavior:** All 8 section/navigation icons on the fully expanded bottom sheet are not clearly visible.

### U5 — Bottom Sheet Background Should Be Transparent
**Section:** Home Screen  
**Behavior:** The sheet background is solid black. A transparent/translucent glassmorphism style would be more consistent with the design system.

### U6 — Gallery Image Glitch on Reopen
**Section:** Home Screen  
**Behavior:** When reopening the app after uploading a gallery image, the default first image flashes briefly (microseconds), then the uploaded image appears, then the loop continues normally. Minor but breaks the immersive feel.

### U7 — Gallery Images Not Persisted Across Sessions
**Section:** Home Screen  
**Behavior:** Uploaded gallery images may not persist. Needs verification — user asked if re-upload is required on every app launch.

### U8 — "जोड़ें" Button Shape Issue
**Section:** नाम जप → Custom number input  
**Behavior:** The "जोड़ें" (Add) button is horizontally stretched but vertically too thin — zero vertical padding makes it look like a compressed pill rather than a proper button.

---

## 📝 Content & Text Corrections

### T1 — "स्लाइड करें" Spelling
**Location:** Home Screen → bottom hint text  
**Issue:** Uses incorrect "स्लाइड" (with small इ). Should be "स्लाईड" (with big ई).  
**Fix:** `स्लाइड → स्लाईड`

### T2 — चतुरसी Section Title
**Location:** Section header  
**Issue:** Title reads "चतुरसी" — should be "श्री हित चतुरसी जी".  
**Fix:** Already partially corrected in inner card, update the top navigation title.

### T3 — राधा सुधानिधि Section Title
**Location:** Section header  
**Issue:** Title reads "श्री हित राधा सुधानिधी जी" — should include "स्तोत्र".  
**Fix:** `श्री हित राधा सुधानिधी जी → श्री हित राधा सुधानिधी जी स्तोत्र`

### T4 — अष्टयाम सेवा Inner Title
**Location:** Section inner content  
**Issue:** Shows "दैनिक सेवा चेक्लिस्ट" instead of "अष्टयाम सेवा". Also uses an inappropriate candle emoji.  
**Fix:** Change title, remove emoji, add 8 seva entries with correct timings.

### T5 — नित्य पाठ Section Title
**Location:** Section header  
**Issue:** Title reads just "नित्य पाठ" — should be "नित्य पाठ रसोपासना".  
**Fix:** `नित्य पाठ → नित्य पाठ रसोपासना`  
**Also:** Remove moon emoji — not appropriate here.

### T6 — शत लीला Carry-Over Rule Exposure
**Location:** श्री वृंदावन शत लीला section  
**Issue:** The "केरी ओवर नियम" (carry-over rule) text is displayed to the user. Implementation rules should be hidden — they are engine logic, not user-facing content.

### T7 — Settings Footer Text
**Location:** सेटिंग्स → bottom  
**Issue:**
- Change "जय श्री हित हरिवंश महाप्रभु" → "जय जय महाप्रभु श्री हित हरिवंश चंद्र जू महाराज की" (or shorter: "जय जय श्री हित हरिवंश")
- Change "राधावल्लभ संप्रदाय" → "राधावल्लभ श्री हरिवंश"
- Remove emojis from footer — they reduce the premium feel.

---

## ✅ Verified Working

- Home screen tap counter (+1 on single tap)
- Home screen double-tap counter (+108)
- Progress display "84,264 का 0%"
- Swipe-up gesture opens bottom sheet
- Quick-add buttons (+108, +1,000, +5,000) — functional when sheet is fully expanded
- Gallery upload opens file manager and displays selected image
- Section navigation from bottom sheet
- Image showreel loop (after initial glitch)
- Settings screen layout and theme selector UI

---

*राधे राधे 🙏*
