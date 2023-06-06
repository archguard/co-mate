@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.metaAction)
    implementation(projects.llmCore)

    implementation(libs.kotlin.stdlib)
    implementation(libs.bundles.markdown)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}