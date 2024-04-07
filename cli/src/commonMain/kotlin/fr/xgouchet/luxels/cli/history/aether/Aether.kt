package fr.xgouchet.luxels.cli.history.aether

import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import okio.Path.Companion.toPath
import kotlin.time.Duration.Companion.seconds

/** Main. */
fun main() {
    val outputPath = "../output".toPath()

    val configuration = configurationWithFixedSeeds(13L) {
        simulation {
            quality(Quality.ROUGH)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.UHD_4K, 2.0)
        }

        render {
            resolution(Resolution.UHD_4K)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }

        animation {
            duration(1.seconds)
            fps(100)
        }
    }

    LuxelEngine.runSimulation(AetherSimulator(), configuration)
}
