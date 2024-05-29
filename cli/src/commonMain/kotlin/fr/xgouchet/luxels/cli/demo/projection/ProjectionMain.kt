package fr.xgouchet.luxels.cli.demo.projection

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "projection"

    val configuration = configuration(Dimension.D3) {
        simulation {
            quality(Quality.DRAFT)
            threadCount(4)
            passType(PassType.RENDER)
            space(Resolution.SQUARE_2880, 1.0)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

    LuxelEngine.runSimulation(
        simulator = CubeSimulator(),
        configuration = configuration,
    )
}
