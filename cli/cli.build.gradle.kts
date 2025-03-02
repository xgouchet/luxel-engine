import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    id("common")
}

kotlin {
    jvmToolchain(17)

    jvm {
        withJava()
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set("art.luxels.cli.MainKt")
        }
    }

    // JS is disabled for now because KoTest doesn't support fully JS target
    // TODO js { nodejs() }

    when (HostManager.hostName) {
        "macos_arm64" -> macosArm64 {
            binaries {
                executable {
                    entryPoint = "art.luxels.cli.main"
                }
            }
        }

        "linux_x64" -> linuxX64 {
            binaries {
                executable {
                    entryPoint = "art.luxels.cli.main"
                }
            }
        }

        else -> {
            println("Unknown host ${HostManager.hostName}")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":engine"))
                implementation(project(":components"))
                implementation(project(":imageio"))

//                implementation(libs.kotlin)
                implementation(libs.kotlinxDateTime)
                implementation(libs.kotlinxSerialization)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)
                implementation(libs.clikt)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.bundles.imageIo)
            }
        }
    }
}

dependencies {
    detektPlugins(project(":detekt"))
    detekt(libs.detektCli)
}

tasks.withType(Jar::class.java) {
    manifest {
        val main by kotlin.jvm().compilations.getting
        attributes(
            "Main-Class" to "art.luxels.cli.MainKt",
        )
    }
}

tasks.register<Copy>("install") {
    group = "run"
    description = "Build the native executable and install it"
    val destDir = "/usr/local/bin"

    dependsOn("runDebugExecutableNative")
    val folder = "build/bin/native/debugExecutable"
    from(folder) {
        include("${rootProject.name}.kexe")
        rename { "luxels-cli" }
    }
    into(destDir)
    doLast {
        println("$ cp $folder/${rootProject.name}.kexe $destDir/luxels-cli")
    }
}
