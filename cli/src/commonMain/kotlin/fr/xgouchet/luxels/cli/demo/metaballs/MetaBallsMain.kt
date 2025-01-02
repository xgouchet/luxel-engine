package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configuration
import kotlin.time.Duration.Companion.seconds

/** Main. */
fun main() {
    MetaBallsScene() runWith configuration(D3) {
        simulation {
            quality(Quality.GRAINY_8)
        }

        render {
            resolution(Resolution.HD_720)
            hdrFixer(path = baseOutputPath / "metaballs")
        }

        simulationVolumeDensity(1.0)

        animation {
            duration(5.seconds)
        }
    }
}
