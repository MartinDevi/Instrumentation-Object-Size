plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "com.example.instrumentation.MainKt"

    val instrumentationAgentProject =
        project(":instrumentation-agent")
    val instrumentationAgentJar =
        instrumentationAgentProject.file("build/libs/instrumentation-agent.jar")
    applicationDefaultJvmArgs =
        listOf("-javaagent:${instrumentationAgentJar.canonicalPath}")
}

tasks.run.configure {
    dependsOn(":instrumentation-agent:jar")
    args(file("build/outputs/dot").canonicalPath)
}

dependencies {
    api(kotlin("stdlib"))
    api("androidx.collection:collection-ktx:1.1.0")
    compileOnly(project(":instrumentation-agent"))
}

task<Exec>("pngs") {
    group = "application"
    description = "Generate PNGs for reference graphs"
    dependsOn("run")
    commandLine("./dot_png.sh")
}
