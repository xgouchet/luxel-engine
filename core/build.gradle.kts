import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")

    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.kotest.multiplatform")

//    id("info.solidsoft.pitest")
//    id("org.jetbrains.kotlinx.kover")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    js {
        nodejs()
    }

    if (HostManager.hostIsLinux) {
        linuxX64()
        linuxArm64()
    }
    if (HostManager.hostIsMac) {
        macosX64()
        macosArm64()
    }

//    linuxX64()
//    mingwX64()
//    macosX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxDateTime)
                implementation(libs.kotlinxSerialization)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotest)
                implementation(libs.okioTest)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.bundles.imageIo)
                implementation(files("libs/JavaHDR.jar"))
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

ktlint {
    version = "1.1.1"
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

// tasks.named("check") {
//    dependsOn("detektMain")
//    dependsOn("ktlintCheck")
// }
//
// tasks.withType<DependencyUpdatesTask> {
//    rejectVersionIf {
//        isNonStable(candidate.version)
//    }
// }
//
// fun isNonStable(version: String): Boolean {
//    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
//    val regex = "^[0-9,.v-]+$".toRegex()
//    val isStable = stableKeyword || regex.matches(version)
//    return isStable.not()
// }
