plugins {
    id("convention.android.library")
}

android {
    namespace = "com.example.shoppinglist.core.data"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
}
