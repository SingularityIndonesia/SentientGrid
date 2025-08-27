package plugin.convention.companion

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

fun Project.withKotlinMultiplatformExtension(
    bloc: KotlinMultiplatformExtension.() -> Unit
) {
    extensions.configure<KotlinMultiplatformExtension>(bloc)
}

fun Project.withBaseExtension(
    bloc: BaseExtension.() -> Unit
) {
    extensions.configure<BaseExtension>(bloc)
}

fun Project.withLibraryExtension(
    bloc: LibraryExtension.() -> Unit
) {
    extensions.configure<LibraryExtension>(bloc)
}

fun Project.withBaseAppModuleExtension(
    bloc: BaseAppModuleExtension.() -> Unit
) {
    extensions.configure<BaseAppModuleExtension>(bloc)
}

fun Project.withPluginManager(bloc: PluginManager.() -> Unit) {
    bloc.invoke(pluginManager)
}

fun Project.compileAndroidApplication(
    namespace: String = "",
    applicationId: String = "",
    versionCode: Int = 1,
    versionName: String = "1.0"
) {
    val libs = versionCatalog

    withKotlinMultiplatformExtension {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }

    withBaseAppModuleExtension {
        this.namespace = namespace
        compileSdk = libs.findVersion("android-compileSdk")
            .get()
            .toString()
            .toInt()

        defaultConfig {
            this.applicationId = applicationId
            minSdk = libs.findVersion("android-minSdk")
                .get()
                .toString()
                .toInt()
            targetSdk = libs.findVersion("android-targetSdk")
                .get()
                .toString()
                .toInt()
            this.versionCode = versionCode
            this.versionName = versionName
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

fun Project.compileAndroidLibrary(
    namespace: String = "",
) {
    val libs = versionCatalog

    withKotlinMultiplatformExtension {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }

    withLibraryExtension {
        this.namespace = namespace
        compileSdk = libs.findVersion("android-compileSdk")
            .get()
            .toString()
            .toInt()

        defaultConfig {
            minSdk = libs.findVersion("android-minSdk")
                .get()
                .toString()
                .toInt()
            targetSdk = libs.findVersion("android-targetSdk")
                .get()
                .toString()
                .toInt()
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

fun Project.compileIOSLibrary(
    namespace: String,
    baseName: String,
    isStatic: Boolean = false,
) {
    withKotlinMultiplatformExtension {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                freeCompilerArgs += "-Xbinary=bundleId=$namespace"
                this.baseName = baseName
                this.isStatic = isStatic
            }
        }
    }
}

fun Project.compileWasmJs(
    outputFileName: String = "composeApp.js",
    outputModuleName: String = "composeApp"
) {
    withKotlinMultiplatformExtension {

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            this.outputModuleName.set(outputModuleName)
            browser {
                val rootDirPath = project.rootDir.path
                val projectDirPath = project.projectDir.path
                commonWebpackConfig {
                    this.outputFileName = outputFileName
                    devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static = (static ?: mutableListOf()).apply {
                            // Serve sources to debug inside browser
                            add(rootDirPath)
                            add(projectDirPath)
                        }
                    }
                }
            }
            binaries.executable()
        }
    }
}