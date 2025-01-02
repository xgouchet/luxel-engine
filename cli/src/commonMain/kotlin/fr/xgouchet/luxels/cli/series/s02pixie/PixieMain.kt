package fr.xgouchet.luxels.cli.series.s02pixie

import fr.xgouchet.luxels.cli.common.baseInputPath
import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.api.configuration.configurationWithFilesFrom

/** Main. */
fun main() {
    PixieScene() runWith configurationWithFilesFrom(Dimension.D2, baseInputPath / "pixie") {
        simulation {
            quality(Quality.SKETCH)
            passType(SimulationType.RENDER)
        }

        render {
            resolution(Resolution.FHD_1080)
            filmType(FilmType.ROUGH)
            hdrFixer(path = baseOutputPath / "pixie")
        }

//        animation{
//            duration(2.seconds)
//            fps(24)
//        }

        simulationVolumeDensity(2.0)
    }
}
