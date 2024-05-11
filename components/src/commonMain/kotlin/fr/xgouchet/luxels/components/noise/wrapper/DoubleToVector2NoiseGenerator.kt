package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.geometry.Vector2

/**
 * A generic [NoiseGenerator], computing a [Vector2] noise from a [Double] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class DoubleToVector2NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Double, Vector2> {
    // region NoiseGenerator

    override fun noise(input: Double): Vector2 {
        return Vector2.fromComponents(delegate.noise(listOf(input), 2))
    }

    // endregion
}