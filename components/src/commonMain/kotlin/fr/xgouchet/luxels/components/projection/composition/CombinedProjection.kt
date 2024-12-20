package fr.xgouchet.luxels.components.projection.composition

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.engine.render.Projection

/**
 * Allows using multiple projection combining their results.
 *
 * @param D the dimension of the source position
 *
 * @property delegates the delegates to combine
 */
class CombinedProjection<D : Dimension>(
    vararg val delegates: Projection<D>,
) : Projection<D> {

    // region Projection

    override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        return delegates.flatMap {
            it.project(position, color)
        }
    }

    // endregion
}
