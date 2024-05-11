package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A generic [NoiseGenerator], computing a [Vector3] noise from a [Vector2] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class Vector2ToVector3NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector2, Vector3> {
    // region NoiseGenerator

    override fun noise(input: Vector2): Vector3 {
        return Vector3.fromComponents(delegate.noise(input.components(), 3))
    }

    // endregion
}