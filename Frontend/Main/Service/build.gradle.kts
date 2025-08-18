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
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

compileAndroidLibrary(
    namespace = "com.singularityuniverse.singularity.main.service"
)

compileIOSLibrary(
    namespace = "com.singularityuniverse.singularity.main.service",
    baseName = "Data",
    isStatic = true
)

compileWasmJs(
    outputFileName = "mainService.js",
    outputModuleName = "mainService"
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
    common {
        api(project(":Core"))
    }

    android {
        implementation(libs.room.runtime)
        implementation(libs.sqlite.bundled)
    }

    ios {
        implementation(libs.room.runtime)
        implementation(libs.sqlite.bundled)
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/build/generated/room/schemas")
}