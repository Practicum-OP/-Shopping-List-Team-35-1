package ru.practicum.android.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.practicum.android.withVersionCatalog

/**
 * Feature module: Compose library + design system, ViewModel and Koin for MVI screens.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("convention.android.compose")

            withVersionCatalog { libs ->
                dependencies {
                    add(IMPLEMENTATION, project(":core:designsystem"))

                    add(IMPLEMENTATION, libs.androidx.lifecycle.viewmodel.compose)
                    add(IMPLEMENTATION, platform(libs.koin.bom))
                    add(IMPLEMENTATION, libs.koin.androidx.compose)
                }
            }
        }
    }
}
