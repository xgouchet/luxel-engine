package fr.xgouchet.luxels.core.gen.noise.wrapper

import fr.xgouchet.luxels.core.gen.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.core.gen.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3

/**
 * A generic [NoiseGenerator], computing a [Vector3] noise from a [Vector2] input.
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class Vector2ToVector3NoiseGenerator(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector2, Vector3> {

    // region Noise

    override fun noise(input: Vector2): Vector3 {
        return Vector3.fromComponents(delegate.noise(input.components(), 3))
    }

    // endregion
}
