package com.radhavallabh.naamsmaran.platform.santsmaran

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.radhavallabh.naamsmaran.ui.screens.santsmaran.SantPrimaryButton
import com.radhavallabh.naamsmaran.ui.screens.santsmaran.SantSecondaryButton
import com.radhavallabh.naamsmaran.ui.screens.santsmaran.santBackground
import com.radhavallabh.naamsmaran.ui.theme.Dimens
import com.radhavallabh.naamsmaran.ui.theme.NaamSmaranTheme
import com.radhavallabh.naamsmaran.ui.theme.NumberFont
import com.radhavallabh.naamsmaran.ui.theme.SantDevanagari
import com.radhavallabh.naamsmaran.ui.theme.SantSmaranColors
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Full-screen alarm UI, shown over the lock screen while the alarm rings.
 *
 * - Snooze → ring again in [SantAlarmContract.SNOOZE_MINUTES] minutes.
 * - "स्मरण आरंभ करें" (dismiss) → silence, unlock if needed, open the reading screen.
 *
 * Also receives [SantAlarmContract.ACTION_OPEN_READING] from the notification's action button,
 * because Android 12+ forbids starting activities from a notification-triggered broadcast.
 */
class SantAlarmActivity : ComponentActivity() {

    private var openReadingRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOverLockScreen()
        enableEdgeToEdge()
        openReadingRequested = intent?.action == SantAlarmContract.ACTION_OPEN_READING

        setContent {
            NaamSmaranTheme {
                SantAlarmScreen(onSnooze = ::snooze, onStartReading = ::startReading)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openReadingRequested = intent.action == SantAlarmContract.ACTION_OPEN_READING
    }

    override fun onResume() {
        super.onResume()
        // The keyguard can only be asked to dismiss once the window is actually showing.
        if (openReadingRequested) startReading()
    }

    private fun snooze() {
        SantAlarmService.stop(this)
        SantAlarmScheduler.fromContext(this).scheduleSnooze()
        finishAndRemoveTask()
    }

    private fun startReading() {
        openReadingRequested = false
        SantAlarmService.stop(this)

        val keyguard = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguard.isKeyguardLocked) {
            launchReading()
            return
        }
        keyguard.requestDismissKeyguard(this, object : KeyguardManager.KeyguardDismissCallback() {
            override fun onDismissSucceeded() = launchReading()
            override fun onDismissError() = launchReading()
            override fun onDismissCancelled() {
                // Alarm is already silenced; stay here so the user can try again.
            }
        })
    }

    private fun launchReading() {
        startActivity(SantAlarmIntents.openReading(this))
        finishAndRemoveTask()
    }

    @Suppress("DEPRECATION")
    private fun showOverLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

private val clockFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

@Composable
private fun SantAlarmScreen(
    onSnooze: () -> Unit,
    onStartReading: () -> Unit
) {
    val timeText by produceState(initialValue = LocalTime.now().format(clockFormatter)) {
        while (true) {
            delay(1_000)
            value = LocalTime.now().format(clockFormatter)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .santBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = Dimens.Space6, vertical = Dimens.Space8)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "प्रातः संत नाम स्मरण",
                fontFamily = SantDevanagari,
                fontWeight = FontWeight.W700,
                fontSize = 28.sp,
                color = SantSmaranColors.Gold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.Space6))
            Text(
                text = timeText,
                fontFamily = NumberFont,
                fontWeight = FontWeight.W700,
                fontSize = 72.sp,
                color = SantSmaranColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))
            Text(
                text = "उठने का समय हो गया 🙏",
                fontFamily = SantDevanagari,
                fontSize = 18.sp,
                color = SantSmaranColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space3)
        ) {
            SantPrimaryButton(text = "स्मरण आरंभ करें", onClick = onStartReading)
            SantSecondaryButton(
                text = "${SantAlarmContract.SNOOZE_MINUTES} मिनट बाद फिर",
                onClick = onSnooze
            )
        }
    }
}
