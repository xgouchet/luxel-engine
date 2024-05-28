package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
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
    val outputPath = baseOutputPath / "noise"

    val configuration = configuration(Dimension.D2) {
        simulation {
            quality(Quality.MEDIUM)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.SQUARE_2880, 1.0)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            filmType(FilmType.ROUGH)
            hdrFixer(outputPath)
        }
    }

    val baseNoise = PerlinNoiseGenerator()
    val sourceNoise = VectorToDoubleNoiseGenerator<Dimension.D2>(baseNoise)

    LuxelEngine.runSimulation(
        simulator = NoiseSimulator(sourceNoise),
        configuration = configuration,
    )
}
