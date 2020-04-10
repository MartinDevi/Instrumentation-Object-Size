plugins {
    kotlin("jvm")
}

dependencies {
    api(kotlin("stdlib"))
}

tasks.jar {
    manifest.attributes["Premain-class"] =
        "com.example.instrumentation.agent.InstrumentationAgent"
}
