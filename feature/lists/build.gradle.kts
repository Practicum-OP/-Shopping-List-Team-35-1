plugins {
    id("convention.android.feature")
}

android {
    namespace = "com.example.shoppinglist.feature.lists"
}

dependencies {
    implementation(project(":core:domain"))
}
