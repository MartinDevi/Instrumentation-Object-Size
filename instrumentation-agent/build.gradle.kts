plugins {
    kotlin("jvm")
}

dependencies {
    api(kotlin("stdlib"))
}

tasks.jar {
    manifest {
        attributes(
            "Premain-class" to "com.github.martindevi.instrumentation.agent.InstrumentationAgent"
        )
    }
}
