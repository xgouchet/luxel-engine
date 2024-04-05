import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    id("common")
}

kotlin {

    jvm {
        jvmToolchain(17)
        withJava()
    }

    // JS is disabled for now because KoTest doesn't support fully JS target
    // TODO js { nodejs() }

    if (HostManager.hostIsLinux) {
        linuxX64()
        linuxArm64()
    }
    if (HostManager.hostIsMac) {
        macosX64()
        macosArm64()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotest)
                implementation(libs.okioTest)
                implementation(libs.kotlinxCoroutinesTest)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.okioJvm)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotestJunit5)
            }
        }
    }
}

dependencies {
    detektPlugins(project(":detekt"))
    detekt(libs.detektCli)
}
