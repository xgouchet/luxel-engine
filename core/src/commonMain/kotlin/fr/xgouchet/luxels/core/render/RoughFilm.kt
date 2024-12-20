package fr.xgouchet.luxels.core.render

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import kotlin.math.roundToInt

/**
 * A basic [Film] implementation exposing the nearest pixel.
 * @param resolution the resolution of the film
 */
class RoughFilm(
    resolution: Resolution,
) : AbstractFilm(resolution) {

    // region Exposure

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        if (position.isNaN()) return

        val i = position.x.roundToInt()
        val j = position.y.roundToInt()
        expose(i, j, color, 1.0)
    }

    // endregion
}
