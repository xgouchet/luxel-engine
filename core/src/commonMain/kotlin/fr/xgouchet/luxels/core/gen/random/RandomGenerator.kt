package fr.xgouchet.luxels.core.gen.random

/**
 * Provides a way to generate random values of the required type.
 * @param T the type of values generated.
 */
interface RandomGenerator<T> {
    /**
     * Returns a value uniformly distributed in the default range for [T].
     */
    fun uniform(): T

    /**
     * Returns a value uniformly distributed between the given min and max.
     */
    fun inRange(min: T, max: T): T

    /**
     * Returns a value following a default gaussian distribution.
     */
    fun gaussian(): T

    /**
     * Returns a value following a gaussian distribution around the provided mean,
     * with the given standard deviation.
     */
    fun gaussian(mean: T, standardDeviation: T): T
}
