package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3

/**
 * A [PositionSource] based on Newton's First Law of Motion.
 *
 * This class keeps track of the momentum of the luxel, and allows applying forces to it.
 * @param initialPosition the initial position of the luxel
 * @param initialSpeed the speed position of the luxel
 */
class InertiaPositionSource(
    initialPosition: Vector3,
    initialSpeed: Vector3,
) : UpdatablePositionSource {
    private var position = initialPosition

    /** The current speed of the luxel. */
    var speed = initialSpeed.normalized()
        private set

    // region PositionSource

    override fun position(): Vector3 {
        return position
    }

    // endregion

    // region UpdatablePositionSource

    override fun updatePosition(strength: Double) {
        position += (speed) * strength
    }

    // endregion

    /**
     * Peeks the position the luxel will be after the next call to [UpdatablePositionSource.updatePosition].
     * @param strength the strength of the update
     * @return the position that would be returned by the [UpdatablePositionSource.updatePosition] method
     */
    fun peekFuturePosition(strength: Double = 1.0): Vector3 {
        return position + (speed * strength)
    }

    /**
     * Updates the speed with a new speed.
     * @param newSpeed the new speed to apply
     * @param inertia the inertia, i.e.: how much of the previous speed should we keep (default: 0)
     */
    fun updateSpeed(newSpeed: Vector3, inertia: Double = 0.0) {
        // TODO check the newtownian formulas for inertia ;)
        speed = ((speed * inertia) + newSpeed) / (inertia + 1.0)
    }
}
