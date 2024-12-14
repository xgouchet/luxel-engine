package fr.xgouchet.luxels.cli.series.pixie

import fr.xgouchet.luxels.cli.common.baseInputPath
import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configurationWithFilesFrom

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

        simulationVolumeDensity(2.0)
    }

//    LuxelEngine().runSimulation(PixieSimulator(), configuration)
}
