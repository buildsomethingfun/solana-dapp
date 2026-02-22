plugins {
    id("convention.android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "dapp.buildsomething.repository.preferences"

dependencies {
    implementation(libs.datastore)
    implementation(libs.timber)
    implementation(libs.koinAndroid)
    implementation(libs.kotlinSerialization)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.kotlinReflect)
}
