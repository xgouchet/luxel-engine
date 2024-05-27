package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

/**
 * A generic [NoiseGenerator] converting from one Vector space to another..
 * @param F the dimension to create noise from
 * @param T the dimension to create noise into
 * @param delegate the underlying [DimensionalNoiseGenerator]
 * @param targetDimension an instance of the target dimension
 */
class VectorNoiseGenerator<F : Dimension, T : Dimension>(
    private val delegate: DimensionalNoiseGenerator,
    private val targetDimension: T,
) : NoiseGenerator<Vector<F>, Vector<T>> {

    // region NoiseGenerator

    override fun noise(input: Vector<F>): Vector<T> {
        return Vector(delegate.noise(input.components(), targetDimension.size))
    }

    // endregion
}
