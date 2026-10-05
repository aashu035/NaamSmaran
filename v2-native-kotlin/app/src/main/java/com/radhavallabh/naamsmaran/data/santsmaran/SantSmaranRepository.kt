package com.radhavallabh.naamsmaran.data.santsmaran

import android.content.Context
import com.google.gson.Gson
import com.radhavallabh.naamsmaran.domain.model.SantSmaranContent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/** Asset path of the verified saint list, relative to the assets root. */
const val SANT_SMARAN_ASSET = "sant_smaran.json"

/** Coil-loadable URI for an `imageAsset` value such as "saints/kabir.webp". */
fun santImageUri(imageAsset: String): String = "file:///android_asset/$imageAsset"

/**
 * Pure JSON → domain parsing (no Android dependencies) so it can be unit-tested
 * on the JVM. Strings are never modified.
 */
object SantSmaranJsonParser {
    private val gson = Gson()

    fun parse(json: String): SantSmaranContent =
        requireNotNull(gson.fromJson(json, SantSmaranDto::class.java)) {
            "sant_smaran.json is empty"
        }.toDomain()
}

/**
 * Loads "प्रातः संत नाम स्मरण" content from the bundled asset. Fully offline.
 * Parsed once and cached for the process lifetime.
 */
@Singleton
class SantSmaranRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mutex = Mutex()

    @Volatile
    private var cached: SantSmaranContent? = null

    suspend fun load(): SantSmaranContent {
        cached?.let { return it }
        return mutex.withLock {
            cached ?: withContext(Dispatchers.IO) {
                val json = context.assets.open(SANT_SMARAN_ASSET).use { stream ->
                    // Explicit UTF-8: the file is Devanagari and must never go through a default charset.
                    String(stream.readBytes(), Charsets.UTF_8)
                }
                SantSmaranJsonParser.parse(json)
            }.also { cached = it }
        }
    }
}
