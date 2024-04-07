package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.components.geometry.Curve2
import fr.xgouchet.luxels.core.math.geometry.Vector2

class AnimatedCurve2(
    animatedPoints: List<AnimatedVector2>,
) : AnimatedCurve<Vector2, AnimatedVector2>(animatedPoints) {

    constructor(vararg animatedPoints: AnimatedVector2) : this(listOf(*animatedPoints))

    override fun buildCurve(points: List<Vector2>): Curve<Vector2> {
        return Curve2(points)
    }
}
