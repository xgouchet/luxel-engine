package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.cli.debug.NoiseFieldSimulator
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds

fun main() {
    val configuration = configurationWithFixedSeeds(42L) {
        simulation {
            quality(Quality.GOOD)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.SQUARE_2880, 1.0)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            fixer(mainFixer)
        }
    }

//    val simulator = BuddhabrotSimulator()
    val simulator = NoiseFieldSimulator()
//    val simulator = DebugSimulator()
//    val simulator = AuroraSimulator()

    LuxelEngine.runSimulation(simulator, configuration)
}
