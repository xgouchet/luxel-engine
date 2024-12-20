package fr.xgouchet.luxels.components.projection.effect

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.render.Projection
import kotlin.math.pow

/**
 * A Projection applying a gaussian bloom effect on a delegate projection.
 *
 * @param D the dimension of the source position
 *
 * @property delegate the base projection to use
 * @property radius the radius (in film space) of the effect
 * @property samples the number of samples to use for the effect
 */
class GaussianBloomProjection<D : Dimension>(
    val delegate: Projection<D>,
    val radius: Double,
    val samples: Int,
) : Projection<D> {

    // region Projection

    override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        val target = delegate.project(position, color)
        val rndGen = RndGen.vector2

        return target + target.flatMap { (pos, col) ->
            List(samples) {
                val offset = rndGen.gaussian() * radius
                val factor = (1.0 / (offset.length() + 0.5)).pow(10)
                (pos + offset) to (col.asHDR() * factor)
            }
        }
    }

    // endregion
}
