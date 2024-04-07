package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector2

/**
 * An [AnimatedCurve] using [Vector2] control points.
 * @param animatedPoints the animated control points
 */
class AnimatedCurve2(
    animatedPoints: List<AnimatedVector2>,
) : AnimatedCurve<Vector2>(animatedPoints, Vector2.Builder)
