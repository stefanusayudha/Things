import plugin.convention.companion.compileAndroidLibrary
import plugin.convention.companion.compileIOSLibrary
import plugin.convention.companion.compileWasmJs
import plugin.convention.companion.dependency
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    id("ConventionUtils")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
}

compileAndroidLibrary(
    namespace = "com.singularityuniverse.singularity.main.core"
)

compileIOSLibrary(
    namespace = "com.singularityuniverse.singularity.main.core",
    baseName = "Core",
    isStatic = true
)

compileWasmJs(
    "MainCore.js",
    "MainCore"
)

withKotlinMultiplatformExtension {
    sourceSets.commonMain {
        kotlin.srcDir("main")
    }
    sourceSets.commonTest {
        kotlin.srcDir("mainTest")
    }
    sourceSets.androidMain {
        kotlin.srcDir("platform/android")
    }
    sourceSets.iosMain {
        kotlin.srcDir("platform/ios")
    }
    sourceSets.wasmJsMain {
        kotlin.srcDir("platform/wasm")
    }
}

dependency {
    android {
        withKotlinMultiplatformExtension {
            api(compose.preview)
        }
        api(libs.androidx.activity.compose)

        // Android-specific HTTP client
        api(libs.ktor.client.okhttp)
        api(libs.ktor.client.android)
    }

    ios {
        api(libs.kotlin.test)

        // iOS-specific HTTP client
        api(libs.ktor.client.darwin)
    }

    common {
        withKotlinMultiplatformExtension {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
        }
        api(libs.androidx.lifecycle.viewmodel)
        api(libs.androidx.lifecycle.runtimeCompose)

        // Navigation dependencies
        api(libs.navigation.compose)
        api(libs.kotlinx.serialization)

        // Image loading dependencies
        api(libs.coil.compose)
        api(libs.coil.network.ktor3)

        // Orbit
        api(libs.orbit.core)
        api(libs.orbit.viewmodel)
        api(libs.orbit.compose)

        // Koin
        api(project.dependencies.platform(libs.koin.bom))
        api(libs.koin.core)
        api(libs.koin.compose)
        api(libs.koin.viewmodel)

        // Ktor
        api(libs.ktor.client.logging)
        api(libs.ktor.client.content.negotiation)
        api(libs.ktor.serialization.kotlinx.json)

        // IO
        api(libs.kotlinx.io.core)
        api(libs.kotlinx.io.okio)

        // Arrow
        api(libs.arrow.core)
        api(libs.arrow.fx.coroutines)
        api(libs.arrow.optics)
        api(libs.kotlinx.io.bytestring)

        // Font
        api(project(":Font"))
    }

    test {
        api(kotlin("test"))
        api(libs.orbit.test)
    }
}

dependencies {
    debugApi(compose.uiTooling)

    // pluto
    debugApi(libs.android.pluto)
    debugApi(libs.android.pluto.network)
    debugApi(libs.android.pluto.exceptions)
    debugApi(libs.android.pluto.preferences)
    debugApi(libs.android.pluto.datastore.pref)

    releaseApi(libs.android.pluto.no.op)
    releaseApi(libs.android.pluto.network.no.op)
    releaseApi(libs.android.pluto.exceptions.no.op)
    releaseApi(libs.android.pluto.preferences.no.op)
    releaseApi(libs.android.pluto.datastore.pref.no.op)

    add("kspAndroid", libs.arrow.optics.ksp.plugin)
    add("kspIosSimulatorArm64", libs.arrow.optics.ksp.plugin)
    add("kspIosX64", libs.arrow.optics.ksp.plugin)
    add("kspIosArm64", libs.arrow.optics.ksp.plugin)

    // KSP support for Room Compiler.
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
