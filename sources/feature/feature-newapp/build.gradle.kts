plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.newapp"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonNavigation)
    implementation(libs.androidxAppcompat)
}
