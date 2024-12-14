package fr.xgouchet.luxels.engine.render

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.render.Exposure

/**
 * Uses a [Projection] to expose a 2D [Exposure] from any input.
 * @param D the dimension of the position being exposed.
 * @param target the target exposure
 * @param projection the projection
 */
class ProjectionExposure<D : Dimension>(
    val target: Exposure<Dimension.D2>,
    val projection: Projection<D>,
) : Exposure<D> {

    // region Exposure

    override fun expose(position: Vector<D>, color: Color) {
        projection.project(position, color).forEach { (p, c) ->
            target.expose(p, c)
        }
    }

    // endregion
}
