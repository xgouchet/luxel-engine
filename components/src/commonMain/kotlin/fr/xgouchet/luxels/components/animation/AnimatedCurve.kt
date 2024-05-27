package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.Dimension
import kotlin.time.Duration

/**
 * A [Curve] animated through time.
 * @param D the dimension of the vector that make the control points of the curve
 * @param animatedPoints the animated control points
 */
open class AnimatedCurve<D : Dimension>(
    private val animatedPoints: List<AnimatedVector<D>>,
) : Animated<Curve<D>> {

    init {
        check(animatedPoints.size >= 2)
    }

    // region Animated

    override fun getValue(time: Duration): Curve<D> {
        val points = animatedPoints.map { it.getValue(time) }
        return Curve(points)
    }

    // endregion
}
