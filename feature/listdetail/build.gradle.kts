plugins {
    id("convention.android.feature")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.shoppinglist.feature.listdetail"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation("androidx.compose.foundation:foundation")
}
