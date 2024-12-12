package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.math.Dimension
import io.kotest.core.spec.style.DescribeSpec

class ConfigurationWithRandomSeedsInputSpec : DescribeSpec(
    {
        val name = "Random Seeds"

        include(
            abstractConfigurationSpec(name, Dimension.D1, null) {
                configurationWithRandomSeeds(Dimension.D1, configure = it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D2, null) {
                configurationWithRandomSeeds(Dimension.D2, configure = it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D3, null) {
                configurationWithRandomSeeds(Dimension.D3, configure = it)
            },
        )

        include(
            abstractConfigurationSpec(name, Dimension.D4, null) {
                configurationWithRandomSeeds(Dimension.D4, configure = it)
            },
        )
    },
)
