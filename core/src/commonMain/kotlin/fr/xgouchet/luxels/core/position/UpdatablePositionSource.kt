package fr.xgouchet.luxels.core.position

/**
 * A generic way to make a position updatable.
 */
interface UpdatablePositionSource : PositionSource {
    /**
     * Updates the internal position of the luxel, with a weighing factor.
     * @param strength the strength of the update (default: 1.0)
     */
    fun updatePosition(strength: Double = 1.0)
}
