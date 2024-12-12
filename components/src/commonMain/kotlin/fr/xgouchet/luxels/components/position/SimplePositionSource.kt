package fr.xgouchet.luxels.components.position

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.position.PositionSource

/**
 * A static immutable [fr.xgouchet.luxels.core.position.PositionSource].
 * @param D the dimension of the space luxels evolve in
 * @property position the position of the luxel
 */
class SimplePositionSource<D : Dimension>(
    var position: Vector<D>,
) : PositionSource<D> {

    // region PositionSource

    override fun position(): Vector<D> {
        return position
    }

    // endregion
}
