plugins {
    id("convention.android-library")
}

android.namespace = "dapp.buildsomething.common.arch"

dependencies {
    implementation(libs.bundles.androidxLifecycle)
    implementation(libs.timber)
}
