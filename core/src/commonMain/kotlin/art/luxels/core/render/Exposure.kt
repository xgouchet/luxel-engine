package art.luxels.core.render

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.imageio.color.Color

/**
 * Describes an object which can have color exposed at a given position.
 * @param D the dimension of the position being exposed.
 */
interface Exposure<D : Dimension> {

    /**
     * Exposes color at the given position.
     * @param position the position to expose.
     * @param color the color to expose.
     */
    fun expose(position: Vector<D>, color: Color)
}
