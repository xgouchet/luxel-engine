package fr.xgouchet.luxels.components.noise

import kotlin.math.abs

/**
 * Provides a Ridged noise based on a delegate [DimensionalNoiseGenerator].
 * @param delegate the delegate [DimensionalNoiseGenerator] used to generate each layer (default: [PerlinNoiseGenerator])
 */
class RidgeNoiseGenerator(
    private val delegate: DimensionalNoiseGenerator = PerlinNoiseGenerator(),
) : DimensionalNoiseGenerator {
    // region DimensionalNoiseGenerator

    override fun noise(input: List<Double>, outputSize: Int): List<Double> {
        return delegate.noise(input, outputSize).map {
            1.0 - abs((it * 2.0) - 1.0)
        }
    }

    // endregion
}
