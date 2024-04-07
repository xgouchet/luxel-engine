package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.cli.debug.DebugSimulator
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import okio.Path.Companion.toPath

/**
 * Main entry point.
 */
fun main() {
    val outputPath = "../output".toPath()
    val configuration = configurationWithFixedSeeds(13L) {
        simulation {
            quality(Quality.GOOD)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.SQUARE_2880, 1.0)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

//    val simulator = BuddhabrotSimulator()
    val simulator = DebugSimulator()
//    val simulator = Scene3dSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
