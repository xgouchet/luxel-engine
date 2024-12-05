package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.engine.LuxelEngine
import fr.xgouchet.luxels.core.math.Dimension

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "noise"

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.DRAFT)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            bmpFixer(path = outputPath)
        }

        simulationSpaceDensity(1.0)
    }

    val baseNoise = PerlinNoiseGenerator()
    val sourceNoise = VectorToDoubleNoiseGenerator<Dimension.D2>(baseNoise)

    LuxelEngine().runSimulation(
        simulator = NoiseSimulator(sourceNoise),
        configuration = configuration,
    )
}
