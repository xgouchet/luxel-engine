package fr.xgouchet.luxels.core.gen.random

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

    // endregion
}
