package com.radhavallabh.naamsmaran.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.radhavallabh.naamsmaran.data.backup.AppBackupManager
import com.radhavallabh.naamsmaran.data.local.AppSettingsStore
import com.radhavallabh.naamsmaran.data.local.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AutoBackupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val pendingResult = goAsync()
        val appContext = context.applicationContext

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settingsStore = AppSettingsStore(appContext.settingsDataStore)
                val scheduler = AppAlarmScheduler.fromContext(appContext)
                if (settingsStore.autoBackupEnabled.first()) {
                    AppBackupManager.fromContext(appContext).writeAutomaticBackup()
                    scheduler.scheduleAutoBackup()
                } else {
                    scheduler.cancelAutoBackup()
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
