package fr.xgouchet.luxels.core.gen.noise.wrapper

import fr.xgouchet.luxels.core.gen.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.Vector2

/**
 * A generic [NoiseGenerator], computing a [Vector2] noise from a [Vector2] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class Vector2ToVector2NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector2, Vector2> {

    // region NoiseGenerator

    override fun noise(input: Vector2): Vector2 {
        return Vector2.fromComponents(delegate.noise(input.components(), 2))
    }

    // endregion
}
