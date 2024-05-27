package fr.xgouchet.luxels.components.position

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RandomGenerator
import fr.xgouchet.luxels.core.math.random.VectorRandomGenerator
import fr.xgouchet.luxels.core.position.PositionSource
import fr.xgouchet.luxels.core.position.UpdatablePositionSource

/**
 * A [PositionSource] based on Newton's First Law of Motion.
 *
 * This class keeps track of the momentum of the luxel, and allows applying forces to it.
 * @param D the dimension of the space luxels evolve in
 * @param P the type of the delegate [PositionSource]
 * @property rng the Random Number Generator to use to fuzz the delegate position
 * @property delegate a delegate [PositionSource]
 * @property radius the radius of the fuzziness in simulation space
 */
data class FuzzyPositionSource<D : Dimension, P : PositionSource<D>>(
    val rng: RandomGenerator<Vector<D>>,
    val delegate: P,
    var radius: Double,
) : UpdatablePositionSource<D> {

    constructor(
        dimension: D,
        delegate: P,
        radius: Double,
    ) : this(VectorRandomGenerator(dimension), delegate, radius)

    // region PositionSource

    override fun position(): Vector<D> {
        return delegate.position() + (rng.gaussian() * radius)
    }

    // endregion

    // region UpdatablePositionSource

    override fun updatePosition(strength: Double) {
        if (delegate is UpdatablePositionSource<*>) {
            delegate.updatePosition(strength)
        }
    }

    // endregion
}
