import org.jetbrains.kotlin.konan.target.HostManager

plugins {
//    application
    kotlin("multiplatform")

    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.kotest.multiplatform")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        mainRun {
            mainClass.set("fr.xgouchet.luxels.cli.MainKt")
        }
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
                implementation(project(":core"))
                implementation(libs.kotlinxDateTime)
                implementation(libs.kotlinxSerialization)
                implementation(libs.kotlinxCoroutines)
                implementation(libs.okio)
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

ktlint {
    version = "1.1.1"
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
    }
}

// application {
//    mainClass.set("fr.xgouchet.luxels.cli.MainKt")
// }

tasks.named<Test>("jvmTest") { useJUnitPlatform() }

tasks.withType(Jar::class.java) {
    manifest {
        val main by kotlin.jvm().compilations.getting
        attributes(
            "Main-Class" to "fr.xgouchet.luxels.cli.MainKt",
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
