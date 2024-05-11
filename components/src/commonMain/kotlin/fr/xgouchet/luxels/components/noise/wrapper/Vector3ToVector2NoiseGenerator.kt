package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A generic [NoiseGenerator], computing a [Vector2] noise from a [Vector3] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class Vector3ToVector2NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector3, Vector2> {
    // region NoiseGenerator

    override fun noise(input: Vector3): Vector2 {
        return Vector2.fromComponents(delegate.noise(input.components(), 2))
    }

    // endregion
}