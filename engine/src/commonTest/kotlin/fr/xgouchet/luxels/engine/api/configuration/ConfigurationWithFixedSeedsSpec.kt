package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.input.InputData
import io.kotest.core.spec.style.DescribeSpec

class ConfigurationWithFixedSeedsSpec : DescribeSpec(
    {
        val name = "Fixed Seeds Input"
        val fixedSeeds = longArrayOf(4L, 8L, 15L, 16L, 23L, 42L)
        val expectedInputs = fixedSeeds.map { InputData(it.toString(16), it, it) }

        include(
            abstractConfigurationSpec(name, Dimension.D1, expectedInputs) { configBuilder ->
                configurationWithFixedSeeds(Dimension.D1, *fixedSeeds) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D2, expectedInputs) { configBuilder ->
                configurationWithFixedSeeds(Dimension.D2, *fixedSeeds) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D3, expectedInputs) { configBuilder ->
                configurationWithFixedSeeds(Dimension.D3, *fixedSeeds) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D4, expectedInputs) { configBuilder ->
                configurationWithFixedSeeds(Dimension.D4, *fixedSeeds) {
                    configBuilder()
                }
            },
        )
    },
)
