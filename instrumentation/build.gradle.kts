plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "com.github.martindevi.instrumentation.MainKt"
    applicationDefaultJvmArgs = listOf("-javaagent:${rootProject.file("instrumentation-agent/build/libs/instrumentation-agent.jar").canonicalPath}")
}

// TODO: Configure dependency on artifact of agent module
//tasks.run.configure {
//    dependsOn()
//}

dependencies {
    api(kotlin("stdlib"))
    compileOnly(project(":instrumentation-agent"))
}
