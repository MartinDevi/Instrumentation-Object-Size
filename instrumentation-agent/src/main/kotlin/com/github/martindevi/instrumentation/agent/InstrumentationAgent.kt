package com.github.martindevi.instrumentation.agent

import java.lang.instrument.Instrumentation

object InstrumentationAgent {

    @Volatile
    @JvmStatic
    private var globalInstrumentation: Instrumentation? = null

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation?) {
        globalInstrumentation = inst
    }

    @JvmStatic
    fun getObjectSize(any: Any?): Long {
        val instrumentation = checkNotNull(globalInstrumentation) { "Agent not initialized." }
        return instrumentation.getObjectSize(any)
    }
}
