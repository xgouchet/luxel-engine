package art.luxels.components.noise

import kotlin.math.abs

/**
 * Provides a Billowed noise based on a delegate [DimensionalNoiseGenerator].
 * @param delegate the delegate [DimensionalNoiseGenerator] used to generate each layer (default: [PerlinNoiseGenerator])
 */
class BillowNoiseGenerator(
    private val delegate: DimensionalNoiseGenerator = PerlinNoiseGenerator(),
) : DimensionalNoiseGenerator {
    // region DimensionalNoiseGenerator

    // x             0 .. .5 .. 1
    // 2x            0 .. 1 ... 2
    // 2x - 1       -1 .. 0 ... 1
    // |2x - 1|      1 .. 0 ... 1
    override fun noise(input: List<Double>, outputSize: Int): List<Double> {
        return delegate.noise(input, outputSize).map {
            abs((it * 2.0) - 1.0)
        }
    }

    // endregion
}
