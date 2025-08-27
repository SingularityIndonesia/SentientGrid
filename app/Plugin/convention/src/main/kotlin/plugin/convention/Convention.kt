package plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

// Apply this to give you access to conventions utils
class ConventionUtils : Plugin<Project> {
    override fun apply(project: Project) {
        // empty just so you can import the conventions companion
    }
}