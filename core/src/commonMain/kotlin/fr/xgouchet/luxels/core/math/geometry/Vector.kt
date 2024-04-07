package fr.xgouchet.luxels.core.math.geometry

interface Vector {

    /**
     * @return the components as a list of Double, with size 3, in [x, y, z] order
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
