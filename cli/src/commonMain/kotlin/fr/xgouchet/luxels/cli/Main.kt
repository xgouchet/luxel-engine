package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.cli.aurora.AuroraSimulator
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import okio.Path.Companion.toPath
import kotlin.time.Duration.Companion.seconds

/**
 * Main entry point.
 */
fun main() {
    val outputPath = "../output".toPath()
    val configuration = configurationWithFixedSeeds(13L) {
        simulation {
            quality(Quality.PREVIEW)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.SQUARE_2880, 1.0)
        }

        animation {
            duration(1.seconds)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

//    val simulator = BuddhabrotSimulator()
//    val simulator = NoiseFieldSimulator()
//    val simulator = DebugSimulator()
    val simulator = AuroraSimulator()
//    val simulator = Scene3dSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
