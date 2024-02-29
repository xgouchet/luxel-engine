package fr.xgouchet.luxels.core.gen.noise

import kotlin.math.abs

/**
 * Provides a Ping-Pong noise based on a delegate [DimensionalNoiseGenerator].
 * @param delegate the delegate [DimensionalNoiseGenerator] used to generate each layer (default: [PerlinNoiseGenerator])
 * @param strength the strength of the Ping-Pong effect (default: 2)
 */
class PingPongNoiseGenerator(
    private val delegate: DimensionalNoiseGenerator = PerlinNoiseGenerator(),
    private val strength: Double = 2.0,
) : DimensionalNoiseGenerator {

    // region DimensionalNoise

    override fun noise(input: List<Double>, outputSize: Int): List<Double> {
        return delegate.noise(input, outputSize).map {
            val s = (it * strength * 2.0)
            1.0 - abs(s.mod(2.0) - 1.0)
        }
    }

    // endregion
}
