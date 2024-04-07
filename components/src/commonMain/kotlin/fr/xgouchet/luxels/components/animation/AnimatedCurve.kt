package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.math.geometry.Vector
import kotlin.time.Duration

abstract class AnimatedCurve<V : Vector, A : Animated<V>>(
    private val animatedPoints: List<A>,
) : Animated<Curve<V>> {

    constructor(vararg animatedPoints: A) : this(listOf(*animatedPoints))

    init {
        check(animatedPoints.size >= 2)
    }

    override fun getValue(time: Duration): Curve<V> {
        val points = animatedPoints.map { it.getValue(time) }
        return buildCurve(points)
    }

    abstract fun buildCurve(points: List<V>): Curve<V>
}
