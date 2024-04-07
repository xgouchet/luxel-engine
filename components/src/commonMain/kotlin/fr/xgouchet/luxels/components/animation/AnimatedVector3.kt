package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve3
import fr.xgouchet.luxels.core.math.geometry.Vector3
import kotlin.time.Duration

class AnimatedVector3(
    val duration: Duration,
    points: List<Vector3>,
) : Animated<Vector3> {

    constructor(
        duration: Duration,
        vararg points: Vector3,
    ) : this(duration, listOf(*points))

    internal val curve = Curve3(points)

    override fun getValue(time: Duration): Vector3 {
        val progress = time.inWholeNanoseconds.toDouble() / duration.inWholeNanoseconds.toDouble()
        return curve.getPosition(progress)
    }
}
