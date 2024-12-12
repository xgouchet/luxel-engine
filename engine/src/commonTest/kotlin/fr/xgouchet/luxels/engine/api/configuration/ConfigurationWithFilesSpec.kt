package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.graphikio.fileSystem
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.input.InputData
import io.kotest.core.spec.style.DescribeSpec
import okio.Path.Companion.toPath

class ConfigurationWithFilesSpec : DescribeSpec(
    {
        val name = "Files Input"
        val tempDir = "../temp".toPath()
        val fileNames = arrayOf(".eggs", "bar.k", "baz", "foo.ext", "spam.spam")
        val expectedInputs = fileNames.map {
            InputData(it.substringBeforeLast('.'), it.hashCode().toLong(), tempDir / it)
        }

        fileSystem.deleteRecursively(tempDir, mustExist = false)
        fileSystem.createDirectories(tempDir)
        expectedInputs.forEach { inputData ->
            fileSystem.write(inputData.data) {
                writeUtf8("${inputData.id}::${inputData.seed}")
            }
        }

        include(
            abstractConfigurationSpec(name, Dimension.D1, expectedInputs) { configBuilder ->
                configurationWithFilesFrom(Dimension.D1, tempDir) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D2, expectedInputs) { configBuilder ->
                configurationWithFilesFrom(Dimension.D2, tempDir) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D3, expectedInputs) { configBuilder ->
                configurationWithFilesFrom(Dimension.D3, tempDir) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D4, expectedInputs) { configBuilder ->
                configurationWithFilesFrom(Dimension.D4, tempDir) {
                    configBuilder()
                }
            },
        )
    },
)
