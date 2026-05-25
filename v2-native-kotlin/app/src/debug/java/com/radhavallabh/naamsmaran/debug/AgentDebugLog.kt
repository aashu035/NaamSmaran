package com.radhavallabh.naamsmaran.debug

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Session debug logger: POST NDJSON-shaped payloads to the Cursor ingest server.
 * Emulator: host loopback is [10.0.2.2]. Physical device: `adb reverse tcp:7800 tcp:7800`.
 */
object AgentDebugLog {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private const val ENDPOINT = "http://10.0.2.2:7800/ingest/b92302a0-2408-432a-abdd-b9dd91b50e94"
    private const val SESSION = "315ef4"
    private const val TAG = "AgentDebug315ef4"

    fun log(
        location: String,
        message: String,
        hypothesisId: String,
        data: Map<String, Any?> = emptyMap(),
        runId: String = "pre-fix"
    ) {
        val json = JSONObject().apply {
            put("sessionId", SESSION)
            put("runId", runId)
            put("hypothesisId", hypothesisId)
            put("location", location)
            put("message", message)
            put("timestamp", System.currentTimeMillis())
            val d = JSONObject()
            data.forEach { (k, v) -> d.put(k, v?.toString() ?: "null") }
            put("data", d)
        }
        val body = json.toString()
        Log.w(TAG, body)
        scope.launch {
            runCatching {
                val conn = URL(ENDPOINT).openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("X-Debug-Session-Id", SESSION)
                conn.doOutput = true
                conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
                conn.connect()
                conn.inputStream.use { it.readBytes() }
                conn.disconnect()
            }
        }
    }
}
