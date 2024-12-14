package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configuration

/** Main. */
fun main() {
    NoiseScene() runWith configuration(Dimension.D2) {
        simulation {
            quality(Quality.DRAFT)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            hdrFixer(path = baseOutputPath / "noise")
        }

        simulationVolumeDensity(1.0)
    }
}
