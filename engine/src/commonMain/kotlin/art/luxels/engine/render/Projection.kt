package art.luxels.engine.render

import art.luxels.core.math.Dimension
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.core.render.Film
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.Color

/**
 * A Projection converts the position of a [Luxel] in simulation into a position on a [Film].
 *
 * @param D the dimension of the source position
 */
interface Projection<D : Dimension> { // TODO make it even more generic?

    /**
     * Converts a simulation space position to a film space position.
     * @param position a simulation-space position
     * @param color the color to project
     * @return a list of 2D position and associated color
     */
    fun project(position: Vector<D>, color: Color): List<Pair<Vector<D2>, Color>>
}
