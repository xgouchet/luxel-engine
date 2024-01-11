import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm")

    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")

    id("info.solidsoft.pitest")
    id("org.jetbrains.kotlinx.kover")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlinxDateTime)
    implementation(libs.bundles.imageIo)

    implementation(files("libs/JavaHDR.jar"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}

ktlint {
    version = "1.1.1"
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("check") {
    dependsOn("detektMain")
    dependsOn("ktlintCheck")
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

pitest {
    testPlugin = "junit5"
    targetClasses.set(setOf("fr.xgouchet.luxels.*"))
    pitestVersion = "1.15.3"
    threads = 10
    outputFormats = setOf("XML", "HTML")
    timestampedReports = false

    junit5PluginVersion = "1.2.0"
    verbose = true
}

koverReport {
}
