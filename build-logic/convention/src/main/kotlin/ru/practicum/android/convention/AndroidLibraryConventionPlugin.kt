package ru.practicum.android.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.practicum.android.withVersionCatalog

/**
 * Android library module: SDK levels and Java version from the version catalog.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            withVersionCatalog { libs ->
                val javaVersion = JavaVersion.valueOf(libs.versions.java.get())

                extensions.configure<LibraryExtension> {
                    compileSdk = libs.versions.compileSdk.get().toInt()
                    defaultConfig.minSdk = libs.versions.minSdk.get().toInt()
                    compileOptions {
                        sourceCompatibility = javaVersion
                        targetCompatibility = javaVersion
                    }
                }
            }
        }
    }
}
