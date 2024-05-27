package fr.xgouchet.luxels.cli.buddhabrot

import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension
import okio.Path.Companion.toPath

/** Main. */
fun main() {
    val outputPath = "../output".toPath()

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.DRAFT)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.UHD_4K, 2.0)
        }

        render {
            resolution(Resolution.UHD_4K)
            filmType(FilmType.CLEAN)
            hdrFixer(outputPath)
        }

//        animation {
//            duration(1.seconds)
//            fps(100)
//        }
    }

    LuxelEngine.runSimulation(BuddhabrotSimulator(), configuration)
}
