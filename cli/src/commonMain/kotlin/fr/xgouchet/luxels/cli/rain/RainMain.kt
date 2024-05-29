package fr.xgouchet.luxels.cli.rain

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import fr.xgouchet.luxels.core.math.Dimension

/**
 * Main entry point.
 */
fun main() {
    val outputPath = baseOutputPath / "rain"

    val configuration = configurationWithFixedSeeds(Dimension.D2, 13L) {
        simulation {
            quality(Quality.DRAFT)
            threadCount(4)
            passType(PassType.RENDER)
            space(Resolution.FHD_1080, 1.0)
        }

        render {
            resolution(Resolution.FHD_1080)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

    val simulator = RainSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
