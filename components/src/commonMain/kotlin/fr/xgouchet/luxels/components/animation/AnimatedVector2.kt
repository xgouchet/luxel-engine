package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector2
import kotlin.time.Duration

/**
 * A [Vector2] animated through time.
 * @param duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 */
class AnimatedVector2(
    duration: Duration,
    points: List<Vector2>,
) : AnimatedVector<Vector2>(duration, points, Vector2.Builder)
