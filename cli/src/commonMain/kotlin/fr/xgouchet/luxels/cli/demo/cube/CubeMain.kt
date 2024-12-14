package fr.xgouchet.luxels.cli.demo.cube

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configuration
import kotlin.time.Duration.Companion.seconds

/** Main. */
fun main() {
    CubeScene() runWith configuration(D3) {
        simulation {
            quality(Quality.SKETCH)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            bmpFixer(path = baseOutputPath / "projection")
        }

        animation {
            duration(5.seconds)
            fps(24)
        }
        simulationVolumeDensity(1.0)
    }
}
