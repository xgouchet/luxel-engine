package art.luxels.core.math.random

import art.luxels.core.math.TAU
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A [RandomGenerator] generating [Int] values.
 */
class IntRandomGenerator(
    private val random: Random,
) : RandomGenerator<Int> {
    // region RandomGenerator

    override fun uniform(): Int {
        return floor(random.nextDouble() * 100).roundToInt()
    }

    override fun inRange(min: Int, max: Int): Int {
        return if (max <= min) {
            min
        } else {
            random.nextInt(min, max)
        }
    }

    override fun gaussian(): Int {
        return gaussian(50, 20)
    }

    override fun gaussian(mean: Int, standardDeviation: Int): Int {
        // Box muller works better when returning both values
        val u = 1 - random.nextDouble()
        val v = random.nextDouble()

        val z1 = sqrt(-2.0 * ln(u)) * cos(TAU * v)
        // TODO val z2 = sqrt(-2.0 * ln(u)) * sin(TAU * v)

        return (mean + (z1 * standardDeviation)).roundToInt()
    }

    // endregion
}
