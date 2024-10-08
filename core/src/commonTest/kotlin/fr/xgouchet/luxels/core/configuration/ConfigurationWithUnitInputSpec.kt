package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class ConfigurationWithUnitInputSpec : DescribeSpec(
    {
        val name = "Unit Input"
        val expectedInput = listOf(InputData("", 0, Unit))

        include(
            abstractConfigurationSpec(name, Dimension.D1, expectedInput) {
                configuration(Dimension.D1, it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D2, expectedInput) {
                configuration(Dimension.D2, it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D3, expectedInput) {
                configuration(Dimension.D3, it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D4, expectedInput) {
                configuration(Dimension.D4, it)
            },
        )
    },
)
