plugins {
    `kotlin-dsl`
}

group = "ru.practicum.android.buildlogic"

dependencies {
    implementation(projects.gradleExt)

    // Provided at runtime by the root build's plugin classpath
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    // workaround for https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "ru.practicum.android.convention.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "convention.android.compose"
            implementationClass = "ru.practicum.android.convention.AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "convention.android.feature"
            implementationClass = "ru.practicum.android.convention.AndroidFeatureConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "convention.kotlin.library"
            implementationClass = "ru.practicum.android.convention.KotlinLibraryConventionPlugin"
        }
    }
}
