package fr.xgouchet.luxels.cli.aurora

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import fr.xgouchet.luxels.core.math.Dimension

/**
 * Main entry point.
 */
fun main() {
    val outputPath = baseOutputPath / "aurora"

    val configuration = configurationWithFixedSeeds(Dimension.D3, 13L) {
        simulation {
            quality(Quality.DRAFT)
        }

        render {
            resolution(Resolution.FHD_1080)
            bmpFixer(path = outputPath)
        }

        simulationSpaceDensity(1.0)
    }

    val simulator = AuroraSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
