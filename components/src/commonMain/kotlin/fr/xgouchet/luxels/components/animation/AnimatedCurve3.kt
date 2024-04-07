package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * An [AnimatedCurve] using [Vector3] control points.
 * @param animatedPoints the animated control points
 */
class AnimatedCurve3(
    animatedPoints: List<AnimatedVector3>,
) : AnimatedCurve<Vector3>(animatedPoints, Vector3.Builder)
