package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector2

data class Rectangle(
    val min: Vector2,
    val max: Vector2,
) {

    val size = Vector2(max.x - min.x, max.y - min.y)

    val center = (min + max) / 2.0

    init {
        require(min.x <= max.x)
        require(min.y <= max.y)
    }

    // region Operators

    operator fun contains(vector: Vector2): Boolean {
        if ((vector.x < min.x) || (vector.y < min.y)) {
            return false
        }
        if ((vector.x > max.x) || (vector.y > max.y)) {
            return false
        }
        return true
    }

    // endregion

    fun expanded(percent: Double): Rectangle {
        val offset = size * percent / 2.0
        return Rectangle(
            min - offset,
            max + offset,
        )
    }

    companion object {
        val UNIT = Rectangle(Vector2.NULL, Vector2.UNIT)
    }
}
