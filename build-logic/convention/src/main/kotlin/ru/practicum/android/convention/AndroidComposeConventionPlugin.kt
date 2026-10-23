package ru.practicum.android.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.practicum.android.withVersionCatalog

/**
 * Android library module with Jetpack Compose enabled.
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("convention.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true
            }

            withVersionCatalog { libs ->
                dependencies {
                    add(IMPLEMENTATION, platform(libs.androidx.compose.bom))
                    add(IMPLEMENTATION, libs.androidx.compose.ui)
                    add(IMPLEMENTATION, libs.androidx.compose.material3)
                    add(IMPLEMENTATION, libs.androidx.compose.ui.tooling.preview)
                    add(DEBUG_IMPLEMENTATION, libs.androidx.compose.ui.tooling)
                }
            }
        }
    }
}
