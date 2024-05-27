package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import kotlin.time.Duration

/**
 * A [Vector] animated through time.
 * @param D the dimension of the vector to animate
 * @property duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 */
open class AnimatedVector<D : Dimension>(
    val duration: Duration,
    points: List<Vector<D>>,
) : Animated<Vector<D>> {

    constructor(
        duration: Duration,
        vararg points: Vector<D>,
    ) : this(duration, listOf(*points))

    internal val curve = Curve(points)

    // region Animated

    override fun getValue(time: Duration): Vector<D> {
        val progress = time.inWholeNanoseconds.toDouble() / duration.inWholeNanoseconds.toDouble()
        return curve.getPosition(progress)
    }

    // endregion
}
