package com.radhavallabh.naamsmaran.platform.santsmaran

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Re-arms the daily alarm whenever the system may have dropped or invalidated it:
 * reboot (including LOCKED_BOOT_COMPLETED — before the first unlock, thanks to device-protected
 * prefs + directBootAware), clock/time-zone change, app update, exact-alarm permission change.
 *
 * Manifest filters decide which actions reach here; any of them just re-syncs with the saved
 * config, so the handler is idempotent.
 */
class SantAlarmBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        SantAlarmScheduler.fromContext(context).syncWithPrefs()
    }
}
