package fr.xgouchet.luxels.components.position

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.position.UpdatablePositionSource

/**
 * An [UpdatablePositionSource] (loosely) based on Newton's First Law of Motion.
 *
 * This class keeps track of the momentum of the luxel, and allows applying forces to it.
 * @param D the dimension of the space luxels evolve in
 * @param initialPosition the initial position of the luxel
 * @param initialSpeed the speed position of the luxel
 */
class InertiaPositionSource<D : Dimension>(
    initialPosition: Vector<D>,
    initialSpeed: Vector<D>,
) : UpdatablePositionSource<D> {

    private var position = initialPosition

    /** The current speed of the luxel. */
    var speed = initialSpeed.normalized()
        private set

    // region PositionSource

    override fun position(): Vector<D> {
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
    fun peekFuturePosition(strength: Double = 1.0): Vector<D> {
        return position + (speed * strength)
    }

    /**
     * Updates the speed with a new speed.
     * @param newSpeed the new speed to apply
     * @param inertia the inertia, i.e.: how much of the previous speed should we keep (default: 0)
     */
    fun updateSpeed(newSpeed: Vector<D>, inertia: Double = 0.0) {
        // TODO check the newtownian formulas for inertia ;)
        speed = ((speed * inertia) + newSpeed) / (inertia + 1.0)
    }
}
