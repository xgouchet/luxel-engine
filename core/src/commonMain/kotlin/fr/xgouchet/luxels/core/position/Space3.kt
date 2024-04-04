package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3

/**
 * Represents a space in a 3D space coordinates.
 * @property min the coordinates of the lower corner
 * @property max the coordinates of the upper corner
 */
data class Space3(
    val min: Vector3,
    val max: Vector3,
) {
    /** The size of the space (i.e.: length of the diagonal from the lower to upper corner). */
    val size = Vector3(max.x - min.x, max.y - min.y, max.z - min.z)

    /** The position of the center of the space. */
    val center = (min + max) / 2.0

    init {
        require(min.x <= max.x)
        require(min.y <= max.y)
        require(min.z <= max.z)
    }

    // region Operators

    /**
     * @param vector a vector to test
     * @return true if the given vector is within the bounds of this space
     */
    operator fun contains(vector: Vector3): Boolean {
        if ((vector.x < min.x) || (vector.y < min.y) || (vector.z < min.z)) {
            return false
        }
        if ((vector.x > max.x) || (vector.y > max.y) || (vector.z > max.z)) {
            return false
        }
        return true
    }

    /**
     * @return a new space as a scaled version of this space, defined by the scaled version of both the
     * lower and upper corners.
     * On the contrary to the [Space3.expanded] function, the center of the returned space doesn't match
     * with the center of this space.
     */
    operator fun times(scale: Double): Space3 {
        return Space3(
            min * scale,
            max * scale,
        )
    }

    // endregion

    // region Utils

    /**
     * @param scale the scale by which to multiply this space's size
     * @return a new space as an expanded version of this space. The center of the result space is the same as this space,
     * and the size of the result space is the size of this space times the provided scale.
     */
    fun expanded(scale: Double): Space3 {
        val halfSize = size * scale / 2.0
        return Space3(
            center - halfSize,
            center + halfSize,
        )
    }

    // endregion

    companion object {
        /** A unit sized [Space3], i.e.: a [Space3] with all min component set to 0 and all max components set to 1. */
        val UNIT = Space3(Vector3.NULL, Vector3.UNIT)
    }
}
