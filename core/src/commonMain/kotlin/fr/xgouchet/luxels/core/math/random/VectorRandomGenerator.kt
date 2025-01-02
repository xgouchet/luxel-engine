package fr.xgouchet.luxels.core.math.random

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

/**
 * A [RandomGenerator] generating [Vector] values.
 * @param D the dimension of the vectors
 * @property dimension an instance of the dimension
 */
class VectorRandomGenerator<D : Dimension>(
    val dimension: D,
) : RandomGenerator<Vector<D>> {

    // region RandomGenerator

    /**
     * @return a [Vector] where each component is a double random value
     * uniformly distributed between 0 (inclusive) and 1 (exclusive).
     */
    override fun uniform(): Vector<D> {
        return Vector(DoubleArray(dimension.size) { RndGen.double.uniform() })
    }

    /**
     * @return a [Vector] where each component is a double random value uniformly
     * distributed between the specified min (inclusive) and max (exclusive) bounds.
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     */
    override fun inRange(min: Vector<D>, max: Vector<D>): Vector<D> {
        return Vector(min.data.zip(max.data) { a, b -> RndGen.double.inRange(a, b) })
    }

    /**
     * @return a [Vector] where each component is a double random value "normally"
     * distributed with mean 0.0 and standard deviation 1.0.
     */
    override fun gaussian(): Vector<D> {
        return gaussian(Vector.nul(dimension), Vector.unit(dimension))
    }

    /**
     * @return a [Vector] where each component is a double random value "normally"
     * distributed with the given mean and standard deviation.
     * @param mean the mean value for the gaussian distribution
     * @param standardDeviation the standard deviation from the mean for the gaussian distribution
     */
    override fun gaussian(mean: Vector<D>, standardDeviation: Vector<D>): Vector<D> {
        return Vector(mean.data.zip(standardDeviation.data) { m, s -> RndGen.double.gaussian(m, s) })
    }

    // endregion
}
