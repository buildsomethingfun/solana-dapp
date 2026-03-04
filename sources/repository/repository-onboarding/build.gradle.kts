plugins {
    id("convention.android-library")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "dapp.buildsomething.repository.onboarding"

dependencies {
    implementation(projects.sources.repository.repositoryPreferences)
    implementation(projects.sources.repository.repositoryUser)
    implementation(projects.sources.repository.repositorySolana)
    implementation(projects.sources.repository.repositorySomething)
    implementation(libs.koinAndroid)
    implementation(libs.kotlinSerialization)
    implementation(libs.kotlinCoroutinesCore)
}
