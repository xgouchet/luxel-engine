buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(libs.kotlinGradlePlugin)

        classpath(libs.dokkaGradlePlugin)
        classpath(libs.versionsGradlePlugin)
        classpath(libs.ktlintGradlePlugin)
        classpath(libs.detektGradlePlugin)

        classpath(libs.pitestGradlePlugin)
        classpath(libs.koverGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
