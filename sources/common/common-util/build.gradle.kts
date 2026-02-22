plugins {
    id("convention.android-library")
}

android.namespace = "dapp.buildsomething.common.util"

dependencies {
    implementation(libs.bundles.androidxLifecycle)
    implementation(libs.timber)
    implementation(libs.kotlinSerialization)
    implementation(libs.kotlinCoroutinesCore)
    implementation(libs.androidBrowserHelper)
}
