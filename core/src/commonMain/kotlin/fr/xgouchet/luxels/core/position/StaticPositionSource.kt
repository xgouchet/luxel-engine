package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3

/**
 * A static immutable [PositionSource].
 */
class StaticPositionSource(
    private val position: Vector3,
) : PositionSource {
    // region PositionSource

    override fun position(): Vector3 {
        return position
    }

    // endregion
}
