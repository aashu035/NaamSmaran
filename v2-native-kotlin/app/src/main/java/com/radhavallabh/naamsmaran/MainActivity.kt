package com.radhavallabh.naamsmaran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.ui.navigation.NaamSmaranApp
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTheme
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranThemeId
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
