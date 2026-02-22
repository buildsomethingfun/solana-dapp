plugins {
    `kotlin-dsl`
}

group = "dapp.buildsomething.build-conventions"

dependencies {
    implementation(libs.pluginKotlinGradle)
    implementation(projects.environment)
}
