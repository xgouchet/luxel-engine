package fr.xgouchet.luxels.cli.demo.buddhabrot

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import okio.Path.Companion.toPath

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "buddhabrot"

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.GOOD)
            threadCount(10)
            passType(PassType.RENDER)
            space(Volume.unit(Dimension.D2).expanded(3.0))
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

    LuxelEngine.runSimulation(
        simulator = BuddhabrotSimulator(0x10_000),
        configuration = configuration,
    )
}
