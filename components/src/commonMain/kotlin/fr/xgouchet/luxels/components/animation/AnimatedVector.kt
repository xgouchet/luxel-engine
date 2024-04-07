package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.geometry.Vector
import kotlin.time.Duration

/**
 * A [Vector] animated through time.
 * @param V the type of the control points
 * @property duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 * @param vectorBuilder the [Vector.Builder] to build interpolated positions
 */
open class AnimatedVector<V : Vector>(
    val duration: Duration,
    points: List<V>,
    vectorBuilder: Vector.Builder<V>,
) : Animated<V> {

    constructor(
        duration: Duration,
        vararg points: V,
        vectorBuilder: Vector.Builder<V>,
    ) : this(duration, listOf(*points), vectorBuilder)

    internal val curve = Curve(points, vectorBuilder)

    // region Animated

    override fun getValue(time: Duration): V {
        val progress = time.inWholeNanoseconds.toDouble() / duration.inWholeNanoseconds.toDouble()
        return curve.getPosition(progress)
    }

    // endregion
}
