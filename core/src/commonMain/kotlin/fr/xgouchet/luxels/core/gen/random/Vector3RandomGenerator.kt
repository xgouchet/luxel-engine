package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector3

/**
 * A [RandomGenerator] generating [Vector3] values.
 */
class Vector3RandomGenerator : RandomGenerator<Vector3> {

    private val doubleRng = RndGen.double()

    // region RandomGenerator

    override fun uniform(): Vector3 {
        return Vector3(
            doubleRng.uniform(),
            doubleRng.uniform(),
            doubleRng.uniform(),
        )
    }

    override fun inRange(min: Vector3, max: Vector3): Vector3 {
        return Vector3(
            doubleRng.inRange(min.x, max.x),
            doubleRng.inRange(min.y, max.y),
            doubleRng.inRange(min.z, max.z),
        )
    }

    // endregion
}
