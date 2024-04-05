package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator

/**
 * A generic [NoiseGenerator], computing a [Double] noise from a [Double] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class DoubleToDoubleNoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Double, Double> {
    // region NoiseGenerator

    override fun noise(input: Double): Double {
        return delegate.noise(listOf(input), 1)[0]
    }

    // endregion
}
