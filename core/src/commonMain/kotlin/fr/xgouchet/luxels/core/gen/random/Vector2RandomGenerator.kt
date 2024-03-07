package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector2

/**
 * A [RandomGenerator] generating [Vector2] values.
 */
class Vector2RandomGenerator : RandomGenerator<Vector2> {

    // region RandomGenerator

    override fun uniform(): Vector2 {
        return Vector2(
            RndGen.double.uniform(),
            RndGen.double.uniform(),
        )
    }

    override fun inRange(min: Vector2, max: Vector2): Vector2 {
        return Vector2(
            RndGen.double.inRange(min.x, max.x),
            RndGen.double.inRange(min.y, max.y),
        )
    }

    // endregion
}
