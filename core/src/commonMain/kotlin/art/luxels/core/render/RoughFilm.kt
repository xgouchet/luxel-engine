package art.luxels.core.render

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.x
import art.luxels.core.math.y
import art.luxels.imageio.color.Color
import kotlin.math.roundToInt

/**
 * A basic [Film] implementation exposing the nearest pixel.
 * @param resolution the resolution of the film
 */
class RoughFilm(resolution: Resolution) : AbstractFilm(resolution) {

    // region Exposure

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        if (position.isNaN()) return

        val i = position.x.roundToInt()
        val j = position.y.roundToInt()
        expose(i, j, color, 1.0)
    }

    // endregion
}
