package com.radhavallabh.naamsmaran.platform.santsmaran

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Entry point of the alarm: fired by AlarmManager (ACTION_FIRE) and by the notification's
 * Snooze action (ACTION_SNOOZE) / stop requests (ACTION_STOP).
 */
class SantAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val appContext = context.applicationContext
        when (intent?.action) {
            SantAlarmContract.ACTION_FIRE ->
                onFire(appContext, intent.getStringExtra(SantAlarmContract.EXTRA_KIND))

            SantAlarmContract.ACTION_SNOOZE -> {
                SantAlarmService.stop(appContext)
                SantAlarmScheduler.fromContext(appContext).scheduleSnooze()
            }

            SantAlarmContract.ACTION_STOP -> SantAlarmService.stop(appContext)
        }
    }

    private fun onFire(context: Context, kind: String?) {
        val scheduler = SantAlarmScheduler.fromContext(context)

        if (kind == SantAlarmContract.KIND_DAILY) {
            // Switched off between scheduling and firing: stay silent and make sure nothing remains.
            if (!scheduler.isEnabled) {
                scheduler.cancelDaily()
                return
            }
            // Tomorrow's alarm FIRST, so the daily chain survives even if ringing fails below.
            scheduler.syncWithPrefs()
        }

        if (!SantAlarmService.start(context)) {
            SantAlarmNotifications.postFallback(context)
        }
    }
}
