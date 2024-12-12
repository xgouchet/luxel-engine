plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlinGradlePlugin)

    implementation(libs.dokkaGradlePlugin)
    implementation(libs.versionsGradlePlugin)
    implementation(libs.ktlintGradlePlugin)
    implementation(libs.detektGradlePlugin)
    implementation(libs.kotestGradlePlugin)
    implementation(libs.mokkeryGradlePlugin)

    implementation(libs.pitestGradlePlugin)
    implementation(libs.koverGradlePlugin)
}
