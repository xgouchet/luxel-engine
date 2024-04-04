import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.internal.file.AbstractFileCollection
import org.gradle.api.internal.file.FileCollectionBackedFileTree
import org.gradle.api.internal.file.FileTreeInternal
import org.gradle.api.internal.file.UnionFileTree
import org.gradle.api.internal.tasks.DefaultTaskDependencyFactory
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(libs.kotlinGradlePlugin)

        classpath(libs.dokkaGradlePlugin)
        classpath(libs.versionsGradlePlugin)
        classpath(libs.ktlintGradlePlugin)
        classpath(libs.detektGradlePlugin)
        classpath(libs.kotestGradlePlugin)

        classpath(libs.pitestGradlePlugin)
        classpath(libs.koverGradlePlugin)
    }
}

val jvmMainPlatformTypes = arrayOf(KotlinPlatformType.jvm, KotlinPlatformType.common, KotlinPlatformType.androidJvm)

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    if (this != rootProject && this.name != "detekt") {
        afterEvaluate {
            val kotlinExt = extensions.findByType(KotlinMultiplatformExtension::class.java)
            if (kotlinExt != null) {

                val detektJvmMain = tasks.named("detektJvmMain").get() as? Detekt
                val detektJvmTest = tasks.named("detektJvmTest").get() as? Detekt

                val mainSourceSets = mutableListOf<FileTreeInternal>()
                val testSourceSets = mutableListOf<FileTreeInternal>()

                kotlinExt.targets.forEach { target ->
                    if (target.platformType in jvmMainPlatformTypes) {
                        target.compilations.forEach { compilation ->
                            compilation.kotlinSourceSets.forEach {
                                when (compilation.name) {
                                    "main" -> mainSourceSets
                                    "test" -> testSourceSets
                                    else -> {
                                        println("Unknown compilation type: ${target.name} / ${compilation.name}")
                                        null
                                    }
                                }?.add(
                                    FileCollectionBackedFileTree(
                                        DefaultTaskDependencyFactory.withNoAssociatedProject(),
                                        { PatternSet() },
                                        it.kotlin.sourceDirectories as AbstractFileCollection,
                                    ),
                                )
                            }
                        }
                    }
                }

                if (detektJvmMain != null) {
                    detektJvmMain.source = UnionFileTree(
                        DefaultTaskDependencyFactory.withNoAssociatedProject(),
                        "unified detektJvmMain source set",
                        mainSourceSets,
                    )
                }
            }
        }
    }
}
