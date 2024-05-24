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

    override fun uniform(): Vector<D> {
        return Vector(DoubleArray(dimension.size) { RndGen.double.uniform() })
    }

    override fun inRange(min: Vector<D>, max: Vector<D>): Vector<D> {
        return Vector(min.data.zip(max.data) { a, b -> RndGen.double.inRange(a, b) })
    }

    override fun gaussian(): Vector<D> {
        return gaussian(Vector.nul(dimension), Vector.unit(dimension))
    }

    override fun gaussian(mean: Vector<D>, standardDeviation: Vector<D>): Vector<D> {
        return Vector(mean.data.zip(standardDeviation.data) { m, s -> RndGen.double.gaussian(m, s) })
    }

    // endregion
}
