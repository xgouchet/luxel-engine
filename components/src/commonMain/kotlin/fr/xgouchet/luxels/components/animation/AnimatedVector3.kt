package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.core.math.geometry.Vector3
import kotlin.time.Duration

/**
 * A [Vector3] animated through time.
 * @param duration the duration of the animation
 * @param points the key frames (regularly spread through the animation duration)
 */
class AnimatedVector3(
    duration: Duration,
    points: List<Vector3>,
) : AnimatedVector<Vector3>(duration, points, Vector3.Builder)
