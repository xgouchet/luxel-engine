package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector2

/**
 * A [RandomGenerator] generating [Vector2] values.
 */
class Vector2RandomGenerator : RandomGenerator<Vector2> {

    private val doubleRng = RndGen.double()

    // region RandomGenerator

    override fun uniform(): Vector2 {
        return Vector2(
            doubleRng.uniform(),
            doubleRng.uniform(),
        )
    }

    override fun inRange(min: Vector2, max: Vector2): Vector2 {
        return Vector2(
            doubleRng.inRange(min.x, max.x),
            doubleRng.inRange(min.y, max.y),
        )
    }

    // endregion
}
