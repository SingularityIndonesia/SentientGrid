import plugin.convention.companion.compileWasmJs
import plugin.convention.companion.dependency
import plugin.convention.companion.enableContextParameter
import plugin.convention.companion.enableExplicitBackingField
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    id("ConventionUtils")
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

enableExplicitBackingField()
enableContextParameter()

compileWasmJs(
    "mainApp.js",
    "mainApp"
)

withKotlinMultiplatformExtension {
    sourceSets.commonMain {
        kotlin.srcDir("main")
    }
}

dependency {
    common {
        api(project(":Font"))
        api(project(":Core"))
    }

    test {

        test {
            api(libs.junit)
        }
    }
}