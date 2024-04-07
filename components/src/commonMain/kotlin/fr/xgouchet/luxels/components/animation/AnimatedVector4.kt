package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector4
import kotlin.time.Duration

/**
 * A [Vector4] animated through time.
 * @param duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 */
class AnimatedVector4(
    duration: Duration,
    points: List<Vector4>,
) : AnimatedVector<Vector4>(duration, points, Vector4.Builder)
