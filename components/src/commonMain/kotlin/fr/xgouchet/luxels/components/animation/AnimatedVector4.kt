package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve4
import fr.xgouchet.luxels.core.math.geometry.Vector4
import kotlin.time.Duration

class AnimatedVector4(
    val duration: Duration,
    val points: List<Vector4>,
) : Animated<Vector4> {

    constructor(
        duration: Duration,
        vararg points: Vector4,
    ) : this(duration, listOf(*points))

    internal val curve = Curve4(points)

    override fun getValue(time: Duration): Vector4 {
        val progress = time.inWholeNanoseconds.toDouble() / duration.inWholeNanoseconds.toDouble()
        return curve.getPosition(progress)
    }
}
