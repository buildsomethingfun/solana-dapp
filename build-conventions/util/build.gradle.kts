plugins {
    `kotlin-dsl`
}

group = "dapp.buildsomething.build-conventions"

dependencies {
    implementation(libs.commonsConfiguration)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location)) // Workaround for https://github.com/gradle/gradle/issues/15383
}