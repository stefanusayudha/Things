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
}

compileAndroidLibrary(
    namespace = "com.singularityuniverse.singularity.main.font"
)

compileIOSLibrary(
    namespace = "com.singularityuniverse.singularity.main.font",
    baseName = "Font",
    isStatic = true
)

compileWasmJs(
    "mainFont.js",
    "mainFont"
)

compose.resources {
    publicResClass = true
    packageOfResClass = "font.resources"
    generateResClass = auto
}

dependency {
    android {
        withKotlinMultiplatformExtension {
            implementation(compose.preview)
        }
    }

    ios {
        implementation(libs.kotlin.test)
    }

    common {
        withKotlinMultiplatformExtension {
            implementation(compose.runtime)
            implementation(compose.components.resources)
        }
        implementation(libs.androidx.lifecycle.runtimeCompose)
    }

    test {
        implementation(kotlin("test"))
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
