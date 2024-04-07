package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector4

/**
 * An [AnimatedCurve] using [Vector4] control points.
 * @param animatedPoints the animated control points
 */
class AnimatedCurve4(
    animatedPoints: List<AnimatedVector4>,
) : AnimatedCurve<Vector4>(animatedPoints, Vector4.Builder)
