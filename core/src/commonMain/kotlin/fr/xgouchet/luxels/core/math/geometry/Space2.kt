package fr.xgouchet.luxels.core.math.geometry

/**
 * Represents a space in a 2D space coordinates.
 * @property min the coordinates of the lower corner
 * @property max the coordinates of the upper corner
 */
data class Space2(
    val min: Vector2,
    val max: Vector2,
) {
    /** The size of the space (i.e.: length of the diagonal from the lower to upper corner). */
    val size = Vector2(max.x - min.x, max.y - min.y)

    /** The position of the center of the space. */
    val center = (min + max) / 2.0

    init {
        require(min.x <= max.x)
        require(min.y <= max.y)
    }

    // region Operators

    /**
     * @param vector a vector to test
     * @return true if the given vector is within the bounds of this space
     */
    operator fun contains(vector: Vector2): Boolean {
        if ((vector.x < min.x) || (vector.y < min.y)) {
            return false
        }
        if ((vector.x > max.x) || (vector.y > max.y)) {
            return false
        }
        return true
    }

    /**
     * @return a new space as a scaled version of this space, defined by the scaled version of both the
     * lower and upper corners.
     * On the contrary to the [Space2.expanded] function, the center of the returned space doesn't match
     * with the center of this space.
     */
    operator fun times(scale: Double): Space2 {
        return Space2(
            min * scale,
            max * scale,
        )
    }

    // endregion

    /**
     * @param scale the scale by which to multiply this space's size
     * @return a Space as an expanded version of this space. The center of the result space is the same as this space,
     * and the size of the result space is the size of this space times the provided scale.
     */
    fun expanded(scale: Double): Space2 {
        val halfSize = size * scale / 2.0
        return Space2(
            center - halfSize,
            center + halfSize,
        )
    }

    companion object {
        /** A unit sized [Space2], i.e.: a [Space2] with all min component set to 0 and all max components set to 1. */
        val UNIT = Space2(Vector2.NULL, Vector2.UNIT)
    }
}
