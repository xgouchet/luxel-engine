package fr.xgouchet.luxels.core.math.geometry

/**
 * A basic interface around Vector types, i.e. a list of numbers
 * representing coordinates in a multidimensional space.
 */
interface Vector {

    /**
     * A builder to convert from a list of components to a Vector.
     */
    interface Builder<V : Vector> {
        /**
         * Builds an instance of [Vector] from numeric components.
         */
        fun buildFromComponents(components: List<Double>): V
    }

    /**
     * @return the components as a list of Double
     */
    fun components(): List<Double>

    /**
     * @return true if at least one component of this vector is NaN
     */
    fun isNaN(): Boolean {
        return components().any { it.isNaN() }
    }

    /**
     * @return true if at least one component of this vector is plus or minus infinity
     */
    fun isInfinite(): Boolean {
        return components().any { it.isInfinite() }
    }
}
