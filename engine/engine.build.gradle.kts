import dev.mokkery.MockMode
import org.jetbrains.kotlin.konan.target.HostManager


plugins {
    kotlin("multiplatform")
    id("common")
    id("dev.mokkery")
}

kotlin {
    jvmToolchain(17)

    jvm {
        withJava()
    }

    // JS is disabled for now because KoTest doesn't support fully JS target
    // TODO js { nodejs() }

    when (HostManager.hostName) {
        "macos_arm64" -> macosArm64()
        "linux_x64" -> linuxX64()
        else -> {
            println("Unknown host ${HostManager.hostName}")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
//                implementation(libs.kotlin)
                implementation(libs.kotlinxDateTime)
                implementation(libs.kotlinxSerialization)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)

                implementation(project(":core"))
                implementation(project(":graphikio"))
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
                implementation(libs.bundles.imageIo)
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

mokkery {
    defaultMockMode.set(MockMode.autoUnit)
}
