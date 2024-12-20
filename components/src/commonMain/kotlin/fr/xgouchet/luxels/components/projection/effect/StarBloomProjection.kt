package fr.xgouchet.luxels.components.projection.effect

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.fromCircular
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.render.Projection
import kotlin.math.abs

/**
 * A Projection applying a star bloom effect on a delegate projection.
 *
 * @param D the dimension of the source position
 *
 * @property delegate the base projection to use
 * @property branches the number of branches for the star effect
 * @property radius the radius (in film space) of the effect
 * @property samples the number of samples to use for the effect
 */
class StarBloomProjection<D : Dimension>(
    val delegate: Projection<D>,
    val branches: Int,
    val radius: Double,
    val samples: Int,
) : Projection<D> {

    // region Projection

    override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        val target = delegate.project(position, color)
        val rndGen = RndGen.double
        val angleStep = TAU / branches

        return target + target.flatMap { (pos, col) ->
            List(samples * branches) { i ->
                val angle = (i % branches) * angleStep
                val distance = abs(rndGen.gaussian()) * radius
                val offset = fromCircular(angle, distance)
                (pos + offset) to col
            }
        }
    }

    // endregion
}
