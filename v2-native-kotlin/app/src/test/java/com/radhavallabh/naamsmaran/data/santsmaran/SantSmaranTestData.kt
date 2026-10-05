package com.radhavallabh.naamsmaran.data.santsmaran

import com.radhavallabh.naamsmaran.domain.model.SantSmaranContent
import java.io.File

/** Locates the real bundled assets from a JVM unit test (working dir = module dir `app/`). */
internal object SantSmaranTestData {

    private val assetsDir: File by lazy {
        listOf(
            File("src/main/assets"),
            File("app/src/main/assets"),
            File("v2-native-kotlin/app/src/main/assets")
        ).firstOrNull { File(it, SANT_SMARAN_ASSET).isFile }
            ?: error("Cannot locate src/main/assets/$SANT_SMARAN_ASSET from ${File(".").absolutePath}")
    }

    val jsonFile: File get() = File(assetsDir, SANT_SMARAN_ASSET)

    /** Photos live in a gitignored folder; a fresh clone of the public repo will not have it. */
    val saintsDir: File get() = File(assetsDir, "saints")

    fun jsonText(): String = String(jsonFile.readBytes(), Charsets.UTF_8)

    fun content(): SantSmaranContent = SantSmaranJsonParser.parse(jsonText())
}
