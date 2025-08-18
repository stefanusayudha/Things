plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinxSerialization)
    application
}

group = "com.singularityuniverse.things"
version = "1.0.0"

application {
    mainClass.set("bff.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {
    
    api(libs.logback)
    api(libs.ktor.serverCore)
    api(libs.ktor.serverNetty)
    api(libs.ktor.serverContentNegotiation)
    api(libs.ktor.serializationKotlinxJson)

    // Database dependencies
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlinDatetime)
    implementation(libs.exposed.json)
    implementation(libs.postgresql)
    implementation(libs.h2) // for testing
    implementation(libs.hikari)

    // Test dependencies
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.ktor.clientContentNegotiation)
}