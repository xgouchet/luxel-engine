package art.luxels.core.render

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.x
import art.luxels.core.math.y
import art.luxels.imageio.color.Color
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.floor
import kotlin.math.sin

/**
 * A basic [Film] implementation exposing the four nearest pixel,
 * using a disc section area as intensity.
 * @param resolution the resolution of the film
 */
class CleanFilm(resolution: Resolution) : AbstractFilm(resolution) {

    // region Exposure

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        val x1 = floor(position.x).toInt()
        val x2 = x1 + 1
        val y1 = floor(position.y).toInt()
        val y2 = y1 + 1

        expose(x1, y1, color, truncatedSectorArea(position.x - x1, position.y - y1))
        expose(x1, y2, color, truncatedSectorArea(position.x - x1, y2 - position.y))
        expose(x2, y1, color, truncatedSectorArea(x2 - position.x, position.y - y1))
        expose(x2, y2, color, truncatedSectorArea(x2 - position.x, y2 - position.y))
    }

    // endregion

    // region Internal

    private fun truncatedSectorArea(w: Double, h: Double): Double {
        val alpha = acos(PIXEL_RADIUS - w)
        val beta = asin(PIXEL_RADIUS - h)
        val theta = alpha - beta

        val triangle = w * h * 0.5
        val segment = (theta - sin(theta)) * PIXEL_HALF_R_SQUARED

        return (triangle + segment) * SCALE
    }

    // endregion

    companion object {
        private const val PIXEL_RADIUS = 0.5
        private const val PIXEL_RADIUS_SQUARED = PIXEL_RADIUS * PIXEL_RADIUS
        private const val PIXEL_HALF_R_SQUARED = PIXEL_RADIUS_SQUARED / 2.0

        private const val SCALE = 4.0 / PI
    }
}
