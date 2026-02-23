plugins {
    alias(libs.plugins.composeCompiler)
    id("convention.android-library-compose")
}

android.namespace = "dapp.buildsomething.feature.auth"

dependencies {
    implementation(projects.sources.common.commonUi)
    implementation(projects.sources.common.commonArch)
    implementation(projects.sources.common.commonNavigation)
    implementation(projects.sources.repository.repositorySolana)
    implementation(projects.sources.repository.repositorySomething)
    implementation(projects.sources.repository.repositoryUser)
    implementation(libs.androidxAppcompat)
    implementation(libs.koinAndroid)
}
