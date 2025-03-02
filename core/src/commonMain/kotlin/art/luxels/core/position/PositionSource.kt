package art.luxels.core.position

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector

/**
 * An interface providing a position information for a Luxel.
 * @param D the dimension of the space luxels evolve in
 */
fun interface PositionSource<D : Dimension> {
    /**
     * @return the current position for the luxel in simulation space.
     */
    fun position(): Vector<D>
}
