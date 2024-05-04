package fr.xgouchet.luxels.cli.history.pixie

import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFilesFrom
import okio.Path.Companion.toPath

/** Main. */
fun main() {
    val outputPath = "../output".toPath()
    val inputPath = "../input".toPath()

    val configuration = configurationWithFilesFrom(inputPath) {
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
    }

    LuxelEngine.runSimulation(PixieSimulator(), configuration)
}
