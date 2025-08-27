plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinxSerialization)
}

group = "plugin.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.gradle)
    implementation(libs.kotlinx.serialization)
    implementation(libs.compose.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("ConventionUtils") {
            id = "ConventionUtils"
            implementationClass = "plugin.convention.ConventionUtils"
        }
    }
}
