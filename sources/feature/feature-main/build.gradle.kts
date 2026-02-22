plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.main"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonNavigation)
    implementation(projects.sources.feature.featureApps)
    implementation(projects.sources.feature.featureNewapp)
    implementation(projects.sources.feature.featureProfile)
    implementation(libs.androidxAppcompat)
}
