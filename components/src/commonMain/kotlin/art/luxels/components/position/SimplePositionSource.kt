package art.luxels.components.position

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.position.PositionSource

/**
 * A static immutable [art.luxels.core.position.PositionSource].
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
