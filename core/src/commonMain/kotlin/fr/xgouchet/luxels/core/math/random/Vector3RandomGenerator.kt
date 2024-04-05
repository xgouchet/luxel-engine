package fr.xgouchet.luxels.core.math.random

import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.geometry.Vector3
import kotlin.math.PI

/**
 * A [RandomGenerator] generating [Vector3] values.
 */
class Vector3RandomGenerator : RandomGenerator<Vector3> {
    // region RandomGenerator

    override fun uniform(): Vector3 {
        var attempt = Vector3(
            RndGen.double.uniform(),
            RndGen.double.uniform(),
            RndGen.double.uniform(),
        )
        while (attempt.squaredLength() > 1) {
            attempt = Vector3(
                RndGen.double.uniform(),
                RndGen.double.uniform(),
                RndGen.double.uniform(),
            )
        }
        return attempt
    }

    override fun inRange(min: Vector3, max: Vector3): Vector3 {
        return Vector3(
            RndGen.double.inRange(min.x, max.x),
            RndGen.double.inRange(min.y, max.y),
            RndGen.double.inRange(min.z, max.z),
        )
    }

    override fun gaussian(): Vector3 {
        return gaussian(Vector3.NULL, Vector3.UNIT)
    }

    override fun gaussian(mean: Vector3, standardDeviation: Vector3): Vector3 {
        val polar = RndGen.double.inRange(0.0, TAU)
        val azimuth = RndGen.double.inRange(-PI, PI)
        val radius = RndGen.double.gaussian(0.0, 1.0)
        val gaussian = Vector3.fromSpherical(polar, azimuth, radius)

        return mean + (gaussian * standardDeviation)
    }

    // endregion
}
