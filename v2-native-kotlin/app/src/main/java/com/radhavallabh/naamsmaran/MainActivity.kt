package com.radhavallabh.naamsmaran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.platform.AppAlarmScheduler
import com.radhavallabh.naamsmaran.ui.navigation.NaamSmaranApp
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTheme
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainActivity — Single Activity, edge-to-edge, Compose-first.
 *
 * The activity does NOTHING except:
 * 1. Enable edge-to-edge rendering
 * 2. Read the active theme from DataStore
 * 3. Apply NaamSmaranTheme
 * 4. Launch the Compose navigation graph
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsStore: AppSettingsStore

    @Inject
    lateinit var alarmScheduler: AppAlarmScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure full screen immersive mode to hide upper strip (status bar) and below strip (navigation bar)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        lifecycleScope.launch {
            if (settingsStore.dailyReminderEnabled.first()) {
                alarmScheduler.scheduleDailyReminder(
                    settingsStore.dailyReminderHour.first(),
                    settingsStore.dailyReminderMinute.first()
                )
            } else {
                alarmScheduler.cancelDailyReminder()
            }

            if (settingsStore.autoBackupEnabled.first()) {
                alarmScheduler.scheduleAutoBackup()
            } else {
                alarmScheduler.cancelAutoBackup()
            }
        }

        setContent {
            val themeId by settingsStore.activeTheme
                .collectAsState(initial = NaamSmaranThemeId.SHARAD_MOON)

            NaamSmaranTheme(themeId = themeId) {
                NaamSmaranApp(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
