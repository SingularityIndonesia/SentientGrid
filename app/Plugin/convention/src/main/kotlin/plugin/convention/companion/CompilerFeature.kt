package plugin.convention.companion

import org.gradle.api.Project

fun Project.enableExplicitBackingField() {
    withKotlinMultiplatformExtension {
        compilerOptions {
            freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
        }
    }
}

fun Project.enableContextParameter() {
    withKotlinMultiplatformExtension {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}