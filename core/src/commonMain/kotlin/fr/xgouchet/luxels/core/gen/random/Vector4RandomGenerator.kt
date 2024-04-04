package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector4

/**
 * A [RandomGenerator] generating [Vector4] values.
 */
class Vector4RandomGenerator : RandomGenerator<Vector4> {
    // region RandomGenerator

    override fun uniform(): Vector4 {
        return Vector4(
            RndGen.double.uniform(),
            RndGen.double.uniform(),
            RndGen.double.uniform(),
            RndGen.double.uniform(),
        )
    }

    override fun inRange(min: Vector4, max: Vector4): Vector4 {
        return Vector4(
            RndGen.double.inRange(min.x, max.x),
            RndGen.double.inRange(min.y, max.y),
            RndGen.double.inRange(min.z, max.z),
            RndGen.double.inRange(min.w, max.w),
        )
    }

    override fun gaussian(): Vector4 {
        return gaussian(Vector4.NULL, Vector4.UNIT)
    }

    override fun gaussian(mean: Vector4, standardDeviation: Vector4): Vector4 {
        throw UnsupportedOperationException("Random gaussian is not available for Vector4")
    }

    // endregion
}
