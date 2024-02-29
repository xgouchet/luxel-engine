package fr.xgouchet.luxels.core.gen.random

import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * A [RandomGenerator] generating [Int] values.
 */
class IntRandomGenerator(
    private val random: Random,
) : RandomGenerator<Int> {

    // region RandomGenerator

    override fun uniform(): Int {
        return (random.nextDouble() * 100).roundToInt()
    }

    override fun inRange(min: Int, max: Int): Int {
        return if (max == min) {
            min
        } else {
            random.nextInt(min, max)
        }
    }

    // endregion
}
