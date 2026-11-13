plugins {
    id("convention.android.feature")
}

android {
    namespace = "com.example.shoppinglist.feature.lists"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.foundation:foundation")
    implementation(libs.androidx.activity.compose)
    implementation(project(":core:domain"))
}
