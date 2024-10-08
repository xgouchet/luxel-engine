package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec
import kotlin.reflect.typeOf

class ConfigurationWithEnumSpec : DescribeSpec(
    {
        val name = "Enum Input"
        val expectedInputs = Planets.entries.map { InputData(it.toString(), it.ordinal.toLong(), it) }

        include(
            abstractConfigurationSpec(name, Dimension.D1, expectedInputs) { configBuilder ->
                configurationWithEnum(Dimension.D1, typeOf<Planets>()) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D2, expectedInputs) { configBuilder ->
                configurationWithEnum(Dimension.D2, typeOf<Planets>()) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D3, expectedInputs) { configBuilder ->
                configurationWithEnum(Dimension.D3, typeOf<Planets>()) {
                    configBuilder()
                }
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D4, expectedInputs) { configBuilder ->
                configurationWithEnum(Dimension.D4, typeOf<Planets>()) {
                    configBuilder()
                }
            },
        )
    },
)

internal enum class Planets {
    MERCURY,
    VENUS,
    EARTH,
    MARS,
    JUPITER,
    SATURN,
    URANUS,
    NEPTUNE,
    PLUTO, // Sue me ⊂(▀¯▀⊂ )
}
