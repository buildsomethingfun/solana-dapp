plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.splash"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonUtil)
    implementation(projects.sources.common.commonNetwork)
    implementation(projects.sources.common.commonArch)
    implementation(projects.sources.common.commonNavigation)
    implementation(projects.sources.repository.repositoryUser)
    implementation(projects.sources.repository.repositorySomething)
    implementation(libs.androidxAppcompat)
    implementation(libs.timber)
    implementation(libs.koinAndroid)
    implementation(libs.coilCore)
    implementation(libs.coilCompose)
    implementation(libs.pageIndicator)
}
