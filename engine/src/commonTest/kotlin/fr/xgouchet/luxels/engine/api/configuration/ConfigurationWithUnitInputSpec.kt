package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.input.InputData
import io.kotest.core.spec.style.DescribeSpec

class ConfigurationWithUnitInputSpec : DescribeSpec(
    {
        val name = "Unit Input"
        val expectedInput = listOf(InputData("UNIT", 0, Unit))

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
