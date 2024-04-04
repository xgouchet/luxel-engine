package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector2

/**
 * A [RandomGenerator] generating [Vector2] values.
 */
class Vector2RandomGenerator : RandomGenerator<Vector2> {

    // region RandomGenerator

    override fun uniform(): Vector2 {
        var attempt = Vector2(
            RndGen.double.uniform(),
            RndGen.double.uniform(),
        )
        while (attempt.squaredLength() > 1) {
            attempt = Vector2(
                RndGen.double.uniform(),
                RndGen.double.uniform(),
            )
        }
        return attempt
    }

    override fun inRange(min: Vector2, max: Vector2): Vector2 {
        return Vector2(
            RndGen.double.inRange(min.x, max.x),
            RndGen.double.inRange(min.y, max.y),
        )
    }

    override fun gaussian(): Vector2 {
        return gaussian(Vector2.NULL, Vector2.UNIT)
    }

    override fun gaussian(mean: Vector2, standardDeviation: Vector2): Vector2 {
        val polar = RndGen.double.inRange(0.0, TAU)
        val radius = RndGen.double.gaussian(0.0, 1.0)
        val gaussian = Vector2.fromCircular(polar, radius)

        return mean + (gaussian * standardDeviation)
    }

    // endregion
}
