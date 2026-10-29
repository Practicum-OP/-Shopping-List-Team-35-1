plugins {
    id("convention.android.feature")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.shoppinglist.feature.auth"
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.core)

    testImplementation(libs.junit)
}
