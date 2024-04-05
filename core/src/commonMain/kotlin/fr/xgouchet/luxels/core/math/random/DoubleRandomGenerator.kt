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

    override fun uniform(): Double {
        return random.nextDouble()
    }

    override fun inRange(min: Double, max: Double): Double {
        return if (max == min) {
            min
        } else {
            random.nextDouble(min, max)
        }
    }

    override fun gaussian(): Double {
        return gaussian(0.0, 1.0)
    }

    override fun gaussian(mean: Double, standardDeviation: Double): Double {
        val u = 1 - uniform()
        val v = uniform()

        val z = sqrt(-2.0 * ln(u)) * cos(TAU * v)

        return mean + (z * standardDeviation)
    }

    // endregion
}
