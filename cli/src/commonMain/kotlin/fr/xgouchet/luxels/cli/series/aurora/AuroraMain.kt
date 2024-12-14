package fr.xgouchet.luxels.cli.series.aurora

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configurationWithFixedSeeds

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

        simulationVolumeDensity(1.0)
    }

    // TODO val simulator = AuroraSimulator()
    // TODO LuxelEngine().runSimulation(simulator, configuration)
}
