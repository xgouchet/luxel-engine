package art.luxels.core.math.random

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume

/**
 * @param D the dimension of the vector / volume space
 * @param volume the volume in which to generate the vector
 * @return a random vector within the bounds of the given volume
 */
fun <D : Dimension> RandomGenerator<Vector<D>>.inVolume(volume: Volume<D>): Vector<D> {
    return inRange(volume.min, volume.max)
}
