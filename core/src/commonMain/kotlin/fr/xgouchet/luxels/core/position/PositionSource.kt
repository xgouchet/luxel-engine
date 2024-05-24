package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

/**
 * An interface providing a position information for a Luxel.
 */
fun interface PositionSource<D : Dimension> {
    /**
     * @return the current position for the luxel in simulation space.
     */
    fun position(): Vector<D>
}
