package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector4
import kotlin.random.Random

/**
 * The basis Random Generator utility object.
 */
object RndGen {

    private var seed: Long = 0L

    private var random = Random(0L)

    /**
     * Resets the underlying random number generator with the given seed.
     * @param newSeed the long to use as the seed for the number generator
     */
    fun resetSeed(newSeed: Long) {
        seed = newSeed
        random = Random(newSeed)
    }

    /**
     * @return a [RandomGenerator] generating integer numbers
     */
    fun int(): RandomGenerator<Int> = IntRandomGenerator(Random(seed))

    /**
     * @return a [RandomGenerator] generating double numbers
     */
    fun double(): RandomGenerator<Double> = DoubleRandomGenerator(Random(seed))

    /**
     * @return a [RandomGenerator] generating 2D vectors
     */
    fun vector2(): RandomGenerator<Vector2> = Vector2RandomGenerator()

    /**
     * @return a [RandomGenerator] generating 3D vectors
     */
    fun vector3(): RandomGenerator<Vector3> = Vector3RandomGenerator()

    /**
     * @return a [RandomGenerator] generating 4D vectors
     */
    fun vector4(): RandomGenerator<Vector4> = Vector4RandomGenerator()


//  TODO  inline fun <reified E : Enum<E>> enum() = EnumRandomGenerator(E::class)
}
