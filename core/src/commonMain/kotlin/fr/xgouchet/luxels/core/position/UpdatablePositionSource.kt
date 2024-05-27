package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Dimension

/**
 * A generic way to make a position updatable.
 * @param D the dimension of the space luxels evolve in
 */
interface UpdatablePositionSource<D : Dimension> : PositionSource<D> {
    /**
     * Updates the internal position of the luxel, with a weighing factor.
     * @param strength the strength of the update (default: 1.0)
     */
    fun updatePosition(strength: Double = 1.0)
}
