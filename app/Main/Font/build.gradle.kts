import plugin.convention.companion.compileWasmJs
import plugin.convention.companion.dependency
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    id("ConventionUtils")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "font.resources"
    generateResClass = auto
}

compileWasmJs(
    "mainFont.js",
    "mainFont"
)

dependency {
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
