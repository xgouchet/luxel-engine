import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    id("common")
    id("com.goncalossilva.resources") version ("0.4.1") // Use Resources in Tests
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
        "macos_x64" -> macosX64()
        "linux_x64" -> linuxX64()
        else -> {
            println("Unknown host ${HostManager.hostName}")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
//                implementation(libs.kotlin)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)
                implementation("com.goncalossilva:resources:0.4.1")
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
