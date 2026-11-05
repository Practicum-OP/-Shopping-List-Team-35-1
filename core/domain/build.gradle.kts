plugins {
    id("convention.kotlin.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
    testImplementation(libs.junit)
}
