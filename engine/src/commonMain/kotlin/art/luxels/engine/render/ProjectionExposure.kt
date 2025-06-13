package art.luxels.engine.render

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.render.Exposure
import art.luxels.imageio.color.Color

/**
 * Uses a [Projection] to expose a 2D [Exposure] from any input.
 * @param D the dimension of the position being exposed.
 * @param target the target exposure
 * @param projection the projection
 */
class ProjectionExposure<D : Dimension>(val target: Exposure<Dimension.D2>, val projection: Projection<D>) :
    Exposure<D> {

    // region Exposure

    override fun expose(position: Vector<D>, color: Color) {
        projection.project(position, color).forEach { (p, c) ->
            target.expose(p, c)
        }
    }

    // endregion
}
