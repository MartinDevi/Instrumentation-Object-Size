plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "com.github.martindevi.instrumentation.MainKt"
    applicationDefaultJvmArgs = listOf("-javaagent:${rootProject.file("instrumentation-agent/build/libs/instrumentation-agent.jar").canonicalPath}")
}

tasks.run.configure {
    dependsOn(":instrumentation-agent:jar")
}

dependencies {
    api(kotlin("stdlib"))
    compileOnly(project(":instrumentation-agent"))
}
