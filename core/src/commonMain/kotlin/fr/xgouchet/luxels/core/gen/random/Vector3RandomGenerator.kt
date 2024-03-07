package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector3

/**
 * A [RandomGenerator] generating [Vector3] values.
 */
class Vector3RandomGenerator : RandomGenerator<Vector3> {

    // region RandomGenerator

    override fun uniform(): Vector3 {
        return Vector3(
            RndGen.double.uniform(),
            RndGen.double.uniform(),
            RndGen.double.uniform(),
        )
    }

    override fun inRange(min: Vector3, max: Vector3): Vector3 {
        return Vector3(
            RndGen.double.inRange(min.x, max.x),
            RndGen.double.inRange(min.y, max.y),
            RndGen.double.inRange(min.z, max.z),
        )
    }

    // endregion
}
