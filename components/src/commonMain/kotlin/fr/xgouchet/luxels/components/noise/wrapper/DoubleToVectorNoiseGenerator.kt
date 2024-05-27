package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

/**
 * A generic [NoiseGenerator], computing a [Vector] noise from a [Double] input.
 * @param T the dimension to create noise into
 * @param delegate the underlying [DimensionalNoiseGenerator]
 * @param targetDimension an instance of the target dimension
 */
class DoubleToVectorNoiseGenerator<T : Dimension>(
    private val delegate: DimensionalNoiseGenerator,
    private val targetDimension: T,
) : NoiseGenerator<Double, Vector<T>> {

    // region NoiseGenerator

    override fun noise(input: Double): Vector<T> {
        return Vector(delegate.noise(listOf(input), targetDimension.size))
    }

    // endregion
}
