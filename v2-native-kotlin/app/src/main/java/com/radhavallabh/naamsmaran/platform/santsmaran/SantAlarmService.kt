package com.radhavallabh.naamsmaran.platform.santsmaran

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

/**
 * Rings the "प्रातः संत नाम स्मरण" alarm: looping alarm-stream sound + vibration, a full-screen
 * notification, and a wake lock. Foreground service of type `mediaPlayback` (Android 14 requires a
 * typed FGS). Stops on Snooze/Dismiss, or after [SantAlarmContract.RING_TIMEOUT_MS] — then a
 * "missed" notification is left behind so the reading screen is still one tap away.
 */
class SantAlarmService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var player: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private val onRingTimeout = Runnable { handleRingTimeout() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        SantAlarmNotifications.ensureChannels(this)
        try {
            ServiceCompat.startForeground(
                this,
                SantAlarmContract.NOTIFICATION_ALARM,
                SantAlarmNotifications.ringing(this),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } catch (e: Exception) {
            // e.g. ForegroundServiceStartNotAllowedException on Android 12+: ring via the
            // fallback notification channel instead of crashing.
            Log.w(TAG, "startForeground refused; using fallback notification", e)
            SantAlarmNotifications.postFallback(this)
            stopSelf()
            return START_NOT_STICKY
        }

        acquireWakeLock()
        stopSoundAndVibration() // restart cleanly if a second alarm fires while ringing
        startSound()
        startVibration()

        handler.removeCallbacks(onRingTimeout)
        handler.postDelayed(onRingTimeout, SantAlarmContract.RING_TIMEOUT_MS)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        stopSoundAndVibration()
        wakeLock?.let { if (it.isHeld) it.release() }
        wakeLock = null
        super.onDestroy()
    }

    // ── Ringing ─────────────────────────────────────────────────────────────

    private fun startSound() {
        val candidates = listOfNotNull(
            RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )
        // A user-picked ringtone can live on credential-encrypted storage (unreadable before the
        // first unlock after reboot), so fall through to the system defaults.
        for (uri in candidates) {
            if (tryPlay(uri)) return
        }
        Log.w(TAG, "No playable alarm sound; vibration + notification only")
    }

    private fun tryPlay(uri: Uri): Boolean = try {
        val mp = MediaPlayer()
        mp.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        mp.setDataSource(this, uri)
        mp.isLooping = true
        mp.prepare()
        mp.start()
        player = mp
        true
    } catch (e: Exception) {
        Log.w(TAG, "Could not play $uri", e)
        false
    }

    @Suppress("DEPRECATION")
    private fun startVibration() {
        val v: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if (v == null || !v.hasVibrator()) return
        vibrator = v
        val effect = VibrationEffect.createWaveform(longArrayOf(0, 800, 600), 0) // repeat from index 0
        // The AudioAttributes overload keeps the vibration on the alarm usage (not silenced by
        // touch-feedback settings). Deprecated in API 33 but still the stable way on 26–36.
        v.vibrate(
            effect,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    private fun stopSoundAndVibration() {
        player?.let {
            try {
                if (it.isPlaying) it.stop()
            } catch (_: IllegalStateException) {
            }
            it.release()
        }
        player = null
        vibrator?.cancel()
        vibrator = null
    }

    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) return
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NaamSmaran:SantAlarm").apply {
            // Always bounded: released in onDestroy, or by the timeout if something goes wrong.
            acquire(SantAlarmContract.RING_TIMEOUT_MS + 30_000L)
        }
    }

    private fun handleRingTimeout() {
        stopSoundAndVibration()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        SantAlarmNotifications.postMissed(this)
        stopSelf()
    }

    companion object {
        private const val TAG = "SantAlarm"

        /** Starts ringing. Returns false if the system refused to start the foreground service. */
        fun start(context: Context): Boolean = try {
            ContextCompat.startForegroundService(context, Intent(context, SantAlarmService::class.java))
            true
        } catch (e: Exception) {
            Log.w(TAG, "startForegroundService refused", e)
            false
        }

        /** Silences the alarm and clears its notification (also the fallback one). */
        fun stop(context: Context) {
            context.stopService(Intent(context, SantAlarmService::class.java))
            NotificationManagerCompat.from(context).cancel(SantAlarmContract.NOTIFICATION_ALARM)
        }
    }
}
