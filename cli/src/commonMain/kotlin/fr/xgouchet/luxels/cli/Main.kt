package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.cli.debug.Scene3dSimulator
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import okio.Path.Companion.toPath

fun main() {
    val outputPath = "../output".toPath()
    val configuration = configurationWithFixedSeeds(13L, 42L, 69L, 1337L) {
        simulation {
            quality(Quality.PREVIEW)
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
//    val simulator = NoiseFieldSimulator()
//    val simulator = DebugSimulator()
//    val simulator = AuroraSimulator()
    val simulator = Scene3dSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
