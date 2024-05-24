package fr.xgouchet.luxels.core.math

/**
 * Represents an axis aligned volume delimited by two vector corner in an n-dimensional space coordinates.
 * @param D the dimension of the Volume.
 * @property min the min corner of the volume
 * @property max the max corner of the volume
 */
data class Volume<D : Dimension>(
    val min: Vector<D>,
    val max: Vector<D>,
) {

    /** The size of the volume (i.e.: length of the diagonal from the lower to upper corner). */
    val size: Vector<D> = max - min

    /** The position of the center of the volume. */
    val center: Vector<D> = (min + max) / 2.0

    init {
        check(min isLessThanOrEqual max)
        print("$min < $max ")
    }

    // region Operators

    /**
     * @param vector a vector to test
     * @return true if the vector is within the boundaries of this volume
     */
    operator fun contains(vector: Vector<D>): Boolean {
        return ((vector isGreaterThanOrEqual min) and (vector isLessThanOrEqual max))
    }

    // endregion

    companion object {
        /**
         * Create a volume of size 1.
         * @param D the dimension of the vectors
         * @param d an instance of the dimension
         * @return a volume of size one centered around zero
         */
        fun <D : Dimension> unit(d: D): Volume<D> {
            val halfUnit = Vector.unit(d) / 2.0
            return Volume(-halfUnit, halfUnit)
        }
    }
}
