plugins {
    id("convention.android.feature")
}

android {
    namespace = "com.example.shoppinglist.feature.listdetail"
}

dependencies {
    implementation(project(":core:domain"))
}
