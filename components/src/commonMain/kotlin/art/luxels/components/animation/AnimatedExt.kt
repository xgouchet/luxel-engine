package art.luxels.components.animation

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RandomGenerator
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import kotlin.time.Duration

/**
 * Generates a random [AnimatedVector] with each key frames are random positions in
 * the given [Volume].
 * @param D the dimension of the [AnimatedVector] and [Volume]
 * @param volume the volume in which to generate the animated vector
 * @param duration the duration of the animation
 */
fun <D : Dimension> RandomGenerator<Vector<D>>.inVolumeAnimated(
    volume: Volume<D>,
    duration: Duration,
): AnimatedVector<D> {
    return AnimatedVector(
        duration,
        List(RndGen.int.inRange(3, 8)) { inVolume(volume) },
    )
}
