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

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "buddhabrot"

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.DRAFT)
            space(Volume.unit(Dimension.D2).expanded(3.0))
        }

        render {
            resolution(Resolution.SQUARE_2880)
            bmpFixer(path = outputPath)
        }
    }

    LuxelEngine.runSimulation(
        simulator = BuddhabrotSimulator(0x1_000),
        configuration = configuration,
    )
}
