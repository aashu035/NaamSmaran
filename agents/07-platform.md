# 07 — Platform Features (Notifications, Backup, Audio, Security)

> **Parent:** [AGENTS.md](../AGENTS.md)

---

## Notifications (5 Types — WorkManager + Notification Channels)

| Type | Time | Content |
|------|------|---------|
| Morning | 04:00 AM | Today's target + "शुभ साधना हो 🙏" |
| Evening | 20:00 PM | Progress update + remaining count |
| Day-end | 22:00 PM | "दिन का विश्लेषण तैयार है" |
| Progress | 14:00 PM | "X नाम जप शेष हैं" if >5000 remaining |
| Encouragement | 06:00 AM | Random pad/quote from library |

All via Capacitor-equivalent WorkManager scheduling. All configurable in settings.

---

## Google Drive Backup

- **Format:** single `backup.json` in Drive AppData folder
- **Contains:** all DailyRecords, AppSettings, custom entries
- **Auto-backup:** daily after day-end analysis (22:00)
- **Manual backup:** button in settings
- **Restore:** Settings → Backup → "पुनर्स्थापित करें" → confirm → restore → recompute
- **Auth:** Google Sign-In via Credential Manager (native Android)

---

## Audio

```kotlin
// User places mp3 in app's documents directory.
// Filename set manually in Settings. No file picker needed.
// Playback via ExoPlayer with background service.
val audioPath = "${context.getExternalFilesDir(null)}/audio/${settings.audioFileName}"
```

- ExoPlayer for background playback
- Loop toggle in settings
- Volume control in settings
- No file picker — user manually places files and enters filename

---

## Security Constraints

This is a personal local app. No server, no public API.

> [!IMPORTANT]
> Data integrity is the #1 security concern for this app.

1. **Data integrity above all:** Room transactions must be atomic. Interrupted writes
   roll back — never leave partial state.
2. **Google Drive token:** OAuth token in `EncryptedSharedPreferences`. Never in plain
   SharedPreferences or DataStore.
3. **Audio file path:** Sanitize filename before constructing path. Only read from
   designated audio directory.
4. **Dependency hygiene:** Before APK build, check for vulnerable dependencies.

**NOT relevant:** SQL injection, XSS, rate limiting, authentication architecture.

---

## Prohibited Actions — ANY Agent

- Do NOT start Layer N+1 before Layer N is tested and confirmed stable
- Do NOT hardcode any color, spacing, or radius value in composable files
- Do NOT use any Om, trishul, rudraksha, or non-Radhavallabh symbol
- Do NOT change the formula without explicit written instruction from the developer
- Do NOT use plain SharedPreferences for sensitive data (OAuth tokens)
- Do NOT add any social sharing, analytics, or external tracking
- Do NOT suggest publishing this app or making it multi-user
- Do NOT use Material default color palette (Purple/Pink/Teal defaults)
- Do NOT use `shadow()` with elevation > 0 on glass surfaces
- Do NOT use `RoundedCornerShape` below 12.dp on any card
