package fr.xgouchet.luxels.cli.demo.mandelbrot

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "mandelbrot"

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.DRAFT)
            space(
                Volume(
                    min = Vector2(-0.75, -0.25),
                    max = Vector2(-0.69, -0.19),
                ),
            )
        }

        render {
            resolution(Resolution.SQUARE_2880)
            hdrFixer(path = outputPath)
        }
    }

    LuxelEngine.runSimulation(
        simulator = MandelbrotSimulator(0x1_000),
        configuration = configuration,
    )
//    LuxelEngine.runSimulation(
//        simulator = BuddhabrotSimulator(0x1_000),
//        configuration = configuration,
//    )
//    LuxelEngine.runSimulation(
//        simulator = PulldelbrotSimulator(0x1_000),
//        configuration = configuration,
//    )
}
