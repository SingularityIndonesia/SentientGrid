import plugin.convention.companion.Main
import plugin.convention.companion.compileWasmJs
import plugin.convention.companion.dependency
import plugin.convention.companion.enableContextParameter
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    id("ConventionUtils")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compileWasmJs(
    outputFileName = "composeApp.js",
    outputModuleName = "composeApp"
)

enableContextParameter()

dependency {
    common {
        withKotlinMultiplatformExtension {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        implementation(libs.androidx.lifecycle.viewmodel)
        implementation(libs.androidx.lifecycle.runtimeCompose)

//        Main("App", transitive = true)
    }
}
