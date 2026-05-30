plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    kotlin("jvm")
}

group = "com.example.moscowguide"
version = "1.0.0"

application {
    mainClass.set("com.example.moscowguide.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.cors)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.hikaricp)
    implementation(libs.postgresql)

    implementation(libs.logback)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
