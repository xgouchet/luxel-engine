package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A static immutable [PositionSource].
 * @property position the position of the luxel
 */
class SimplePositionSource(
    var position: Vector3,
) : PositionSource {
    // region PositionSource

    override fun position(): Vector3 {
        return position
    }

    // endregion
}
