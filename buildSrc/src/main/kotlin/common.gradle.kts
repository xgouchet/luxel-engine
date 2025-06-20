import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType

plugins {
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.kotest.multiplatform")
    id("org.jetbrains.kotlinx.kover")
}

// region JUnit

project.afterEvaluate {
    tasks.findByName("jvmTest")?.apply {
        (this as? Test)?.useJUnitPlatform()
    }

    tasks.findByName("koverXmlReport")?.apply {
        this.dependsOn("jvmTest")
    }

    tasks.create("checkJvm") {
        group = "verification"
        this.dependsOn("ktlintCheck")
        this.dependsOn("detektJvmMain")
        this.dependsOn("jvmTest")
        this.dependsOn("koverXmlReport")
    }
}

// endregion

// region KtLint

ktlint {
    version.set("1.6.0")
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
        include("**/luxels/cli/**")
    }
}

// endregion

// region Kover

koverReport {
    defaults {
        filters {
            includes {
                classes("fr.xgouchet.*")
            }
        }
        verify {
            rule {
                minBound(33, MetricType.LINE, AggregationType.COVERED_PERCENTAGE)
            }
        }
    }
}

// endregion

// region Dependency version

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

// endregion
