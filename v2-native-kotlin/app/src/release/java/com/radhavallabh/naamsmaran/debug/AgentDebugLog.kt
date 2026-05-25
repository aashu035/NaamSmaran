package com.radhavallabh.naamsmaran.debug

/** Release no-op — keeps main code compiling without network. */
object AgentDebugLog {
    @Suppress("UNUSED_PARAMETER")
    fun log(
        location: String,
        message: String,
        hypothesisId: String,
        data: Map<String, Any?> = emptyMap(),
        runId: String = "pre-fix"
    ) = Unit
}
