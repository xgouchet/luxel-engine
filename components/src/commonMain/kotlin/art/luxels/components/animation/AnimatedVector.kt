package art.luxels.components.animation

import art.luxels.components.geometry.Curve
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import kotlin.time.Duration

/**
 * A [Vector] animated through time.
 * @param D the dimension of the vector to animate
 * @property duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 */
open class AnimatedVector<D : Dimension>(val duration: Duration, points: List<Vector<D>>) : Animated<Vector<D>> {

    constructor(
        duration: Duration,
        vararg points: Vector<D>,
    ) : this(duration, listOf(*points))

    internal val curve = Curve(points)

    init {
        require(duration.inWholeNanoseconds > 0) { "Can't create a curve with duration zero" }
    }

    // region Animated

    override fun getValue(time: Duration): Vector<D> {
        val progress = time / duration
        return curve.getPosition(progress)
    }

    // endregion
}
