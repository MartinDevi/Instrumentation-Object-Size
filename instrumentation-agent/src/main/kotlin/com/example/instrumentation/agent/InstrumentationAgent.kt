package com.example.instrumentation.agent

import java.lang.instrument.Instrumentation

object InstrumentationAgent {

    @Volatile
    @JvmStatic
    private lateinit var instrumentation: Instrumentation

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun premain(agentArgs: String?, instrumentation: Instrumentation) {
        InstrumentationAgent.instrumentation = instrumentation
    }

    @JvmStatic
    fun getSize(any: Any): Long = instrumentation.getObjectSize(any)
}
