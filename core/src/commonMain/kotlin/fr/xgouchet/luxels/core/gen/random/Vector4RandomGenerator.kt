package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector4

/**
 * A [RandomGenerator] generating [Vector4] values.
 */
class Vector4RandomGenerator : RandomGenerator<Vector4> {

    private val doubleRng = RndGen.double()

    // region RandomGenerator

    override fun uniform(): Vector4 {
        return Vector4(
            doubleRng.uniform(),
            doubleRng.uniform(),
            doubleRng.uniform(),
            doubleRng.uniform(),
        )
    }

    override fun inRange(min: Vector4, max: Vector4): Vector4 {
        return Vector4(
            doubleRng.inRange(min.x, max.x),
            doubleRng.inRange(min.y, max.y),
            doubleRng.inRange(min.z, max.z),
            doubleRng.inRange(min.w, max.w),
        )
    }

    // endregion
}
