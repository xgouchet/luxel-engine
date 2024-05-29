package fr.xgouchet.luxels.cli.pixie

import fr.xgouchet.luxels.cli.common.baseInputPath
import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFilesFrom
import fr.xgouchet.luxels.core.math.Dimension

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "pixie"
    val inputPath = baseInputPath / "pixie"

    val configuration = configurationWithFilesFrom(Dimension.D2, inputPath) {
        simulation {
            quality(Quality.DRAFT)
        }

        render {
            resolution(Resolution.UHD_4K)
            filmType(FilmType.CLEAN)
            bmpFixer(path = outputPath)
        }

        simulationSpaceDensity(2.0)
    }

    LuxelEngine.runSimulation(PixieSimulator(), configuration)
}
