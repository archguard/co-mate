val ktor_version = "2.3.1"

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    id("io.ktor.plugin") version "2.3.1"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}

dependencies {
    implementation(projects.comateCore)
    implementation(projects.llmCore)

    implementation(projects.specLang)
    implementation(projects.specRuntime)

    implementation(libs.dotenv)

    // for backend code element
    implementation(libs.chapi.domain)

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-compression:$ktor_version")

    implementation("ch.qos.logback:logback-classic:1.4.5")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version") // for testing
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation(libs.bundles.test)

    testImplementation("io.mockk:mockk:1.13.5")
}
