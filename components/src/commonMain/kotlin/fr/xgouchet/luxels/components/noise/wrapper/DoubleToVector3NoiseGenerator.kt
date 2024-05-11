package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * A generic [NoiseGenerator], computing a [Vector3] noise from a [Double] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class DoubleToVector3NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Double, Vector3> {
    // region NoiseGenerator

    override fun noise(input: Double): Vector3 {
        return Vector3.fromComponents(delegate.noise(listOf(input), 3))
    }

    // endregion
}