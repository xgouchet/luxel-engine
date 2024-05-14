package fr.xgouchet.luxels.core.math

data class Volume<D : Dimension>(
    val min: Vector<D>,
    val max: Vector<D>,
) {

    /** The size of the space (i.e.: length of the diagonal from the lower to upper corner). */
    val size: Vector<D> = max - min

    /** The position of the center of the space. */
    val center: Vector<D> = (min + max) / 2.0

    init {
        check(min isLessThanOrEqual max)
        print("$min < $max ")
    }

    // region Operator

    operator fun contains(vector: Vector<D>): Boolean {
        return ((vector isGreaterThanOrEqual min) and (vector isLessThanOrEqual max))
    }

    // endregion
}
