package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3

data class Box(
    val min: Vector3,
    val max: Vector3,
) {

    val size = Vector3(max.x - min.x, max.y - min.y, max.z - min.z)

    val center = (min + max) / 2.0

    init {
        require(min.x <= max.x)
        require(min.y <= max.y)
        require(min.z <= max.z)
    }

    // region Operators

    operator fun contains(vector: Vector3): Boolean {
        if ((vector.x < min.x) || (vector.y < min.y) || (vector.z < min.z)) {
            return false
        }
        if ((vector.x > max.x) || (vector.y > max.y) || (vector.z > max.z)) {
            return false
        }
        return true
    }

    operator fun times(scale: Double): Box {
        return Box(
            min * scale,
            max * scale,
        )
    }

    // endregion

    // region Utils

    fun expanded(percent: Double): Box {
        val offset = size * percent / 2.0
        return Box(
            min - offset,
            max + offset,
        )
    }

    // endregion

    companion object {
        val UNIT = Box(Vector3.NULL, Vector3.UNIT)
    }
}
