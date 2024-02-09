package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.math.EPSILON
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a Color in standard RGBA space.
 * Each channel holds a value in a [0…1] range.
 * @property r the red component in a [0…1] range.
 * @property g the green component in a [0…1] range.
 * @property b the blue component in a [0…1] range.
 * @property a the alpha component in a [0…1] range.
 */
data class Color(
    var r: Double,
    var g: Double,
    var b: Double,
    var a: Double = 1.0,
) {
    // TODO Color(0xFFEA88EO)
    // TODO Color("#FFEA88EO")

    // region Operators

    operator fun plus(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b, a + other.a)
    }

    operator fun times(scale: Double): Color {
        return Color(r * scale, g * scale, b * scale, a * scale)
    }

    // endregion

    // region Color operations

    fun luminance(): Double {
        return (LUMINANCE_RED * r) + (LUMINANCE_GREEN * g) + (LUMINANCE_BLUE * b)
    }

    fun hue(): Double {
        val min = min(min(r, g), b)
        val max = max(max(r, g), b)
        val delta = (max - min)

        val h = when (max) {
            min -> 0.0
            r -> (g - b) / delta
            g -> 2.0 + ((b - r) / delta)
            b -> 4.0 + ((r - g) / delta)
            else -> 0.0
        }

        return if (h < 0.0) {
            (h + 6.0) / 6.0
        } else {
            h / 6.0
        }
    }

    fun saturation(): Double {
        val min = min(min(r, g), b)
        val max = max(max(r, g), b)
        return if (max <= EPSILON) {
            0.0
        } else {
            val delta = (max - min)
            delta / max
        }
    }

    fun value(): Double {
        return max(max(r, g), b)
    }

    // endregion

    companion object {
        val WHITE = Color(1.0, 1.0, 1.0, 1.0)
        val GREY = Color(0.5, 0.5, 0.5, 1.0)
        val BLACK = Color(0.0, 0.0, 0.0, 1.0)

        // Primary colors
        val RED = Color(1.0, 0.0, 0.0, 1.0)
        val GREEN = Color(0.0, 1.0, 0.0, 1.0)
        val BLUE = Color(0.0, 0.0, 1.0, 1.0)

        // Secondary colors
        val YELLOW = Color(1.0, 1.0, 0.0, 1.0)
        val TEAL = Color(0.0, 1.0, 1.0, 1.0)
        val VIOLET = Color(1.0, 0.0, 1.0, 1.0)

        val TRANSPARENT = Color(0.0, 0.0, 0.0, 0.0)

        internal const val LUMINANCE_RED = 0.2126
        internal const val LUMINANCE_GREEN = 0.7152
        internal const val LUMINANCE_BLUE = 0.0722
    }
}
