plugins {
    application
    kotlin("jvm")

    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlinxDateTime)

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

application {
    mainClass.set("fr.xgouchet.luxels.cli.MainKt")
}

ktlint {
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
    }
}

tasks.withType(Test::class.java) {
    useJUnitPlatform {
        includeEngines("spek2", "junit-jupiter", "junit-vintage")
    }
}

// tasks.withType(Jar::class.java) {
//    manifest {
//        val main by kotlin.jvm().compilations.getting
//        attributes(
//            "Main-Class" to "fr.xgouchet.luxels.cli.MainKt"
//        )
//    }
// }

tasks.named("check") {
    dependsOn("detektMain")
    dependsOn("ktlintCheck")
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
