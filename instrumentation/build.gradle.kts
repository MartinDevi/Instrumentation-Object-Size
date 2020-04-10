plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "com.example.instrumentation.MainKt"
    applicationDefaultJvmArgs = listOf("-javaagent:${rootProject.file("instrumentation-agent/build/libs/instrumentation-agent.jar").canonicalPath}")
}

tasks.run.configure {
    dependsOn(":instrumentation-agent:jar")
}

dependencies {
    api(kotlin("stdlib"))
    api("androidx.collection:collection-ktx:1.1.0")
    compileOnly(project(":instrumentation-agent"))
}
