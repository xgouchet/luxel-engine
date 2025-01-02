package fr.xgouchet.luxels.cli.fractals.mandelbrot

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.configuration

/** Main. */
fun main() {
    MandelbrotScene() runWith configuration(Dimension.D2) {
        simulation {
            space(
                Volume(
                    min = Vector2(-3.0, -2.0),
                    max = Vector2(1.0, 2.0),
                ),
            )
        }

        render {
            resolution(Resolution.SQUARE_2880)
            hdrFixer(path = baseOutputPath / "mandelbrot")
        }
    }
}
