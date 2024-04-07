package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.geometry.Vector
import kotlin.time.Duration

/**
 * A [Curve] animated through time.
 * @param V the type of the Control points
 * @param animatedPoints the animated control points
 * @param vectorBuilder the [Vector.Builder] to build interpolated positions
 */
open class AnimatedCurve<V : Vector>(
    private val animatedPoints: List<AnimatedVector<V>>,
    private val vectorBuilder: Vector.Builder<V>,
) : Animated<Curve<V>> {

    init {
        check(animatedPoints.size >= 2)
    }

    // region Animated

    override fun getValue(time: Duration): Curve<V> {
        val points = animatedPoints.map { it.getValue(time) }
        return Curve(points, vectorBuilder)
    }

    // endregion
}
