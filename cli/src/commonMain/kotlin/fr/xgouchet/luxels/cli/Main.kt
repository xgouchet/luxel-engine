package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.cli.aurora.AuroraSimulator
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
            quality(Quality.ROUGH)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.FHD_1080, 1.0)
        }

        render {
            resolution(Resolution.FHD_1080)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

//    val simulator = BuddhabrotSimulator()
//    val simulator = DebugSimulator()
//    val simulator = Scene3dSimulator()
    val simulator = AuroraSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
