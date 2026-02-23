plugins {
    id("convention.android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "dapp.buildsomething.repository.something"

dependencies {
    implementation(projects.sources.common.commonNetwork)
    implementation(projects.sources.repository.repositoryUser)
    implementation(projects.sources.repository.repositoryPreferences)
    implementation(projects.sources.repository.repositorySolana)

    implementation(libs.timber)
    implementation(libs.koinAndroid)
    implementation(libs.kotlinSerialization)
    implementation(libs.kotlinCoroutinesCore)
}
