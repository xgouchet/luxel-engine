package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A generic [NoiseGenerator], computing a [Double] noise from a [Vector3] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class Vector3ToDoubleNoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector3, Double> {
    // region NoiseGenerator

    override fun noise(input: Vector3): Double {
        return delegate.noise(input.components(), 1)[0]
    }

    // endregion
}
