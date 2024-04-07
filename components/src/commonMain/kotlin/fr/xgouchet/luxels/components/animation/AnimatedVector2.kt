package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve2
import fr.xgouchet.luxels.core.math.geometry.Vector2
import kotlin.time.Duration

class AnimatedVector2(
    val duration: Duration,
    val points: List<Vector2>,
) : Animated<Vector2> {

    constructor(
        duration: Duration,
        vararg points: Vector2,
    ) : this(duration, listOf(*points))

    internal val curve = Curve2(points)

    override fun getValue(time: Duration): Vector2 {
        val progress = time.inWholeNanoseconds.toDouble() / duration.inWholeNanoseconds.toDouble()
        return curve.getPosition(progress)
    }
}
