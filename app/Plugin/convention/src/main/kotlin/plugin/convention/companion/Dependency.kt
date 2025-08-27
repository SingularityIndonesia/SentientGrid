package plugin.convention.companion

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class DependencyScope(private val project: Project) {
    fun android(bloc: KotlinDependencyHandler.() -> Unit = {}) {
        with(project) {
            withKotlinMultiplatformExtension {
                sourceSets {
                    androidMain.dependencies(bloc)
                }
            }
        }
    }

    fun ios(bloc: KotlinDependencyHandler.() -> Unit = {}) {
        with(project) {
            withKotlinMultiplatformExtension {
                sourceSets {
                    iosMain.dependencies(bloc)
                }
            }
        }
    }

    fun wasm(bloc: KotlinDependencyHandler.() -> Unit = {}) {
        with(project) {
            withKotlinMultiplatformExtension {
                sourceSets {
                    wasmJsMain.dependencies(bloc)
                }
            }
        }
    }

    fun common(bloc: KotlinDependencyHandler.() -> Unit = {}) {
        with(project) {
            withKotlinMultiplatformExtension {
                sourceSets {
                    commonMain.dependencies(bloc)
                }
            }
        }
    }

    fun test(bloc: KotlinDependencyHandler.() -> Unit = {}) {
        with(project) {
            withKotlinMultiplatformExtension {
                sourceSets {
                    commonTest.dependencies(bloc)
                }
            }
        }
    }
}

fun Project.dependency(bloc: DependencyScope.() -> Unit) {
    val scope = DependencyScope(this)
    bloc.invoke(scope)
}

// tobe used in convention plugin
val ksp: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("ksp", dependencyNotation)
    }

val debugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("debugImplementation", dependencyNotation)
    }

val devDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("devDebugImplementation", dependencyNotation)
    }

val stagingDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("stagingDebugImplementation", dependencyNotation)
    }

val prodDebugImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("prodDebugImplementation", dependencyNotation)
    }

val releaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("releaseImplementation", dependencyNotation)
    }

val devReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("devReleaseImplementation", dependencyNotation)
    }

val stagingReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("stagingReleaseImplementation", dependencyNotation)
    }

val prodReleaseImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("prodReleaseImplementation", dependencyNotation)
    }

val androidTestImplementation: DependencyHandler.(Any) -> Unit =
    { dependencyNotation ->
        add("androidTestImplementation", dependencyNotation)
    }

val debugAllImplementation: DependencyHandler.(Any) -> Unit =
    { pkg ->
        listOf(
            debugImplementation,
            devDebugImplementation,
            stagingDebugImplementation,
            prodDebugImplementation
        ).forEach {
            it(pkg)
        }
    }

val releaseAllImplementation: DependencyHandler.(Any) -> Unit =
    { pkg ->
        listOf(
            releaseImplementation,
            devReleaseImplementation,
            stagingReleaseImplementation,
            prodReleaseImplementation
        ).forEach {
            it(pkg)
        }
    }

// short hands
fun KotlinDependencyHandler.Main(pkgName: String, transitive: Boolean = false) {
    if (transitive)
        api("main:$pkgName")
    else
        implementation("main:$pkgName")
}