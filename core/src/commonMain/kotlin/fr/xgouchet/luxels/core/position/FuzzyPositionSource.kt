package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.gen.random.onUnitSphere
import fr.xgouchet.luxels.core.math.Vector3

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
        return delegate.position() + (RndGen.vector3.onUnitSphere() * radius)
        // TODO KMM + (RndGen.vector3().inGaussianBall() * radius)
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
