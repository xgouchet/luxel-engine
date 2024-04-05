package fr.xgouchet.luxels.components.position

import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.position.PositionSource
import fr.xgouchet.luxels.core.position.UpdatablePositionSource

/**
 * A [PositionSource] based on Newton's First Law of Motion.
 *
 * This class keeps track of the momentum of the luxel, and allows applying forces to it.
 * @param P the type of the delegate [PositionSource]
 * @property delegate a delegate [PositionSource]
 * @property radius the radius of the fuzziness in simulation space
 */
data class FuzzyPositionSource<P : PositionSource>(
    val delegate: P,
    var radius: Double,
) : UpdatablePositionSource {
    // region PositionSource

    override fun position(): Vector3 {
        return delegate.position() + (RndGen.vector3.gaussian() * radius)
    }

    // endregion

    // region UpdatablePositionSource

    override fun updatePosition(strength: Double) {
        if (delegate is UpdatablePositionSource) {
            delegate.updatePosition(strength)
        }
    }

    // endregion
}
