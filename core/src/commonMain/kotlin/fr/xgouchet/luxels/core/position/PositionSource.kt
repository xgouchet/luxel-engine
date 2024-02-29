package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3

/**
 * An interface providing a position information for a Luxel.
 */
fun interface PositionSource {
    /**
     * @return the current position for the luxel in simulation space.
     */
    fun position(): Vector3
}
