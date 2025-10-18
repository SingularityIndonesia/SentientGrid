import plugin.convention.companion.compileWasmJs
import plugin.convention.companion.dependency
import plugin.convention.companion.enableContextParameter
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    id("ConventionUtils")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

enableContextParameter()

compileWasmJs(
    "mainCore.js",
    "mainCore"
)

withKotlinMultiplatformExtension {
    sourceSets.commonMain {
        kotlin.srcDir("main")
    }
    sourceSets.commonTest {
        kotlin.srcDir("mainTest")
    }
    sourceSets.wasmJsMain {
        kotlin.srcDir("platform/wasm")
    }
}

dependency {

    wasm {
        // JS-specific HTTP client
        api(libs.ktor.client.js)
        implementation(npm("mqtt", "5.14.1"))
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
