package art.luxels.components.projection.composition

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.engine.render.Projection
import art.luxels.imageio.color.Color

/**
 * Allows using multiple projection combining their results.
 *
 * @param D the dimension of the source position
 *
 * @property delegates the delegates to combine
 */
class CombinedProjection<D : Dimension>(vararg val delegates: Projection<D>) : Projection<D> {

    // region Projection

    override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        return delegates.flatMap {
            it.project(position, color)
        }
    }

    // endregion
}
