package fr.xgouchet.luxels.components.noise.wrapper

import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

/**
 * A generic [NoiseGenerator] converting from a Vector space to a double.
 * @param F the dimension to create noise from
 * @param delegate the underlying [DimensionalNoiseGenerator]
 */
class VectorToDoubleNoiseGenerator<F : Dimension>(
    private val delegate: DimensionalNoiseGenerator,
) : NoiseGenerator<Vector<F>, Double> {

    // region NoiseGenerator

    override fun noise(input: Vector<F>): Double {
        return delegate.noise(input.components(), 1).first()
    }

    // endregion
}