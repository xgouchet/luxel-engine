package fr.xgouchet.luxels.core.render

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

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
