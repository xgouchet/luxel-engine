import dev.mokkery.MockMode
import org.jetbrains.kotlin.konan.target.HostManager


plugins {
    kotlin("multiplatform")
    id("common")
    id("dev.mokkery")
}

kotlin {
    jvmToolchain(17)

    jvm {}

    // JS is disabled for now because KoTest doesn't support fully JS target
    // TODO js { nodejs() }

    when (HostManager.hostName) {
        "macos_arm64" -> macosArm64()
        "macos_x64" -> macosX64()
        "linux_arm64" -> linuxArm64()
        "linux_x64" -> linuxX64()
        else -> {
            println("Unknown host ${HostManager.hostName}")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxDateTime)
                implementation(libs.kotlinxSerialization)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)

                implementation(project(":imageio"))
                implementation(project(":core"))
                implementation(project(":engine"))
                implementation(project(":components"))
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
                implementation(libs.kotlinxCoroutinesJvm)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotestJunit5)
            }
        }

        when (HostManager.hostName) {
            "macos_arm64" -> macosArm64Main {
                dependencies {
                    implementation(libs.okioMacosArm64)
                }
            }

            "macos_x64" -> macosX64Main {
                dependencies {
                    implementation(libs.okioMacosX64)
                }
            }

            "linux_arm64" -> linuxArm64Main {
                dependencies {
                    implementation(libs.okioLinuxArm64)
                }
            }

            "linux_x64" -> linuxX64Main {
                dependencies {
                    implementation(libs.okioLinuxX64)
                }
            }
        }
    }
}

dependencies {
    detektPlugins(project(":detekt"))
    detekt(libs.detektCli)
}

mokkery {
    defaultMockMode.set(MockMode.autoUnit)
}
