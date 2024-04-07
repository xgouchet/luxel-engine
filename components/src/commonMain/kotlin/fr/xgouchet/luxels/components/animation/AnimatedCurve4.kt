package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.components.geometry.Curve4
import fr.xgouchet.luxels.core.math.geometry.Vector4

class AnimatedCurve4(
    animatedPoints: List<AnimatedVector4>,
) : AnimatedCurve<Vector4, AnimatedVector4>(animatedPoints) {

    constructor(vararg animatedPoints: AnimatedVector4) : this(listOf(*animatedPoints))

    override fun buildCurve(points: List<Vector4>): Curve<Vector4> {
        return Curve4(points)
    }
}
