plugins {
    id("convention.android.feature")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.shoppinglist.feature.listdetail"
}

dependencies {
    implementation(project(":core:domain"))
}
