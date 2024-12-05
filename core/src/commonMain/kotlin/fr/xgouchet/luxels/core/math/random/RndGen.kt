package fr.xgouchet.luxels.core.math.random

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import kotlin.random.Random

/**
 * The basis Random Generator utility object.
 */
object RndGen {
    internal var seed: Long = 0L
        private set

    private var random = Random(0L)

    /**
     * Resets the underlying random number generator with the given seed.
     * @param newSeed the long to use as the seed for the number generator
     */
    fun resetSeed(newSeed: Long) {
        seed = newSeed
        random = Random(newSeed)
        println("--- Reset Seed: $newSeed")

        int = IntRandomGenerator(Random(seed))
        double = DoubleRandomGenerator(Random(seed))
    }

    /**
     * @return a [RandomGenerator] generating integer numbers
     */
    var int: RandomGenerator<Int> = IntRandomGenerator(Random(seed))
        private set

    /**
     * @return a [RandomGenerator] generating double numbers
     */
    var double: RandomGenerator<Double> = DoubleRandomGenerator(Random(seed))
        private set

    /**
     * @return a [RandomGenerator] generating 2D vectors
     */
    var vector2: RandomGenerator<Vector<Dimension.D2>> = VectorRandomGenerator(Dimension.D2)
        private set

    /**
     * @return a [RandomGenerator] generating 3D vectors
     */
    var vector3: RandomGenerator<Vector<Dimension.D3>> = VectorRandomGenerator(Dimension.D3)
        private set

    /**
     * @return a [RandomGenerator] generating 4D vectors
     */
    var vector4: RandomGenerator<Vector<Dimension.D4>> = VectorRandomGenerator(Dimension.D4)
        private set

//  TODO  inline fun <reified E : Enum<E>> enum() = EnumRandomGenerator(E::class)
}
