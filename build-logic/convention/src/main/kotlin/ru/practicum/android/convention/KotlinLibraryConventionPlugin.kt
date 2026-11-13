package ru.practicum.android.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import ru.practicum.android.withVersionCatalog

/**
 * Pure Kotlin/JVM module without Android dependencies.
 */
class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            withVersionCatalog { libs ->
                val javaVersion = JavaVersion.valueOf(libs.versions.java.get())

                extensions.configure<JavaPluginExtension> {
                    sourceCompatibility = javaVersion
                    targetCompatibility = javaVersion
                }
                extensions.configure<KotlinJvmProjectExtension> {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
                    }
                }
            }
        }
    }
}
