package plugin.convention.companion

import org.gradle.api.Project

fun Project.enableContextParameter() {
    withKotlinMultiplatformExtension {
        compilerOptions {
            freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
        }
    }
}