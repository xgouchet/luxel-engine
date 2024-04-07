package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.components.geometry.Curve3
import fr.xgouchet.luxels.core.math.geometry.Vector3

class AnimatedCurve3(
    animatedPoints: List<AnimatedVector3>,
) : AnimatedCurve<Vector3, AnimatedVector3>(animatedPoints) {

    constructor(vararg animatedPoints: AnimatedVector3) : this(listOf(*animatedPoints))

    override fun buildCurve(points: List<Vector3>): Curve<Vector3> {
        return Curve3(points)
    }
}
