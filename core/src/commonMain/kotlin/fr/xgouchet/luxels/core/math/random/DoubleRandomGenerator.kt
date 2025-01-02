package fr.xgouchet.luxels.core.math.random

import fr.xgouchet.luxels.core.math.TAU
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A [RandomGenerator] generating [Double] values.
 */
class DoubleRandomGenerator(
    private val random: Random,
) : RandomGenerator<Double> {

    // region RandomGenerator

    /**
     * @return a double random value uniformly distributed between 0 (inclusive) and 1 (exclusive).
     */
    override fun uniform(): Double {
        return random.nextDouble()
    }

    /**
     * @return a double random value uniformly distributed between the specified
     * min (inclusive) and max (exclusive) bounds.
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     */
    override fun inRange(min: Double, max: Double): Double {
        return if (max <= min) {
            min
        } else {
            random.nextDouble(min, max)
        }
    }

    /**
     * @return a double random value "normally" distributed with mean 0.0 and standard deviation 1.0.
     */
    override fun gaussian(): Double {
        return gaussian(0.0, 1.0)
    }

    /**
     * @return a double random value "normally" distributed with the given mean and standard deviation.
     * @param mean the mean value for the gaussian distribution
     * @param standardDeviation the standard deviation from the mean for the gaussian distribution
     */
    override fun gaussian(mean: Double, standardDeviation: Double): Double {
        val u = 1 - uniform()
        val v = uniform()

        val z = sqrt(-2.0 * ln(u)) * cos(TAU * v)

        return mean + (z * standardDeviation)
    }

    // endregion
}
