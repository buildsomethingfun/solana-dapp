plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.profile"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonArch)
    implementation(projects.sources.common.commonNavigation)
    implementation(projects.sources.common.commonUtil)
    implementation(projects.sources.repository.repositorySomething)
    implementation(projects.sources.repository.repositoryOnboarding)
    implementation(projects.sources.repository.repositorySolana)
    implementation(libs.androidxAppcompat)
    implementation(libs.koinAndroid)
    implementation(libs.coilCompose)
}
