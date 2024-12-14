package fr.xgouchet.luxels.cli.fractals.buddhabrot

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configuration

/** Main. */
fun main() {
    BuddhabrotScene() runWith configuration(Dimension.D2) {
        simulation {
            quality(Quality.LOW)
            space(
                Volume(
                    min = Vector2(-2.5, -2.0),
                    max = Vector2(1.5, 2.0),
                ),
            )
        }

        render {
            resolution(Resolution.SQUARE_2880)
            hdrFixer(path = baseOutputPath / "buddhabrot")
        }
    }
}
