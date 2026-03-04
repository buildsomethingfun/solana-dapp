plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.onboarding"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonArch)
    implementation(projects.sources.common.commonNavigation)
    implementation(projects.sources.repository.repositoryOnboarding)
    implementation(libs.androidxAppcompat)
    implementation(libs.koinAndroid)
}
