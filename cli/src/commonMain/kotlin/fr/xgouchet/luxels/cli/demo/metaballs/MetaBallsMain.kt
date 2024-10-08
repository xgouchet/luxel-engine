package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.math.Dimension

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "metaballs"

    val configuration = configuration(Dimension.D3) {
        simulation {
            quality(Quality.LOW)
        }

        render {
            resolution(Resolution.HD_720)
            hdrFixer(path = outputPath)
        }

        simulationSpaceDensity(1.0)
    }

    LuxelEngine.runSimulation(
        simulator = MetaBallsSimulator(),
        configuration = configuration,
    )
}
