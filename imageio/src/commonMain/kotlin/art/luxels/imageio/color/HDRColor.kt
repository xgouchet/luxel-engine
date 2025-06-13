package art.luxels.imageio.color

import art.luxels.imageio.color.Color.Companion.LUMINANCE_BLUE
import art.luxels.imageio.color.Color.Companion.LUMINANCE_GREEN
import art.luxels.imageio.color.Color.Companion.LUMINANCE_RED
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a Color in standard RGBA space.
 * Each channel holds a floating point (Double) value in an unbound range.
 * @property r the red component.
 * @property g the green component.
 * @property b the blue component.
 * @property a the alpha component.
 */
data class HDRColor(val r: Double, val g: Double, val b: Double, val a: Double = 1.0) : Color {
    // region Operators

    /**
     * @param other the color to add to this color
     * @return a color with each component as the component in this color plus the matching component in the given input
     */
    operator fun plus(other: HDRColor): HDRColor {
        return HDRColor(r + other.r, g + other.g, b + other.b, a + other.a)
    }

    /**
     * @param scale the factor by which to multiply the color
     * @return a color with each component multiplied by the given input
     */
    operator fun times(scale: Double): HDRColor {
        return HDRColor(r * scale, g * scale, b * scale, a * scale)
    }

    /**
     * @param scale the factor by which to multiply the color
     * @return a color with each component multiplied by the given input
     */
    operator fun div(scale: Double): HDRColor {
        return HDRColor(r / scale, g / scale, b / scale, a / scale)
    }

    /**
     * @param scale the factor by which to multiply the color
     * @return a color with each component multiplied (channel wise) by the given input
     */
    operator fun times(scale: HDRColor): HDRColor {
        return HDRColor(r * scale.r, g * scale.g, b * scale.b, a * scale.a)
    }

    // endregion

    // region Color operations

    /**
     * @return the luminance of this color (perceived luminosity)
     */
    fun luminance(): Double {
        return (LUMINANCE_RED * r) + (LUMINANCE_GREEN * g) + (LUMINANCE_BLUE * b)
    }

    /**
     * @return the hue of this color (in a [0…1] range)
     */
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

    /**
     * @return the saturation of this color (in a [0…1] range).
     * TODO ? @Deprecated("Does this even make sense for an unbound color?")
     */
    fun saturation(): Double {
        val min = min(min(r, g), b)
        val max = max(max(r, g), b)
        val delta = (max - min)
        val absMax = abs(max)
        return if (absMax <= 0.00001) {
            0.0
        } else {
            delta / absMax
        }
    }

    /**
     * @return the value of this color
     */
    fun value(): Double {
        return max(max(r, g), b)
    }

    // endregion

    companion object {
        /** Perfect White color. */
        val WHITE = HDRColor(1.0, 1.0, 1.0, 1.0)

        /** Perfect half grey color. */
        val GREY = HDRColor(0.5, 0.5, 0.5, 1.0)

        /** Perfect black color. */
        val BLACK = HDRColor(0.0, 0.0, 0.0, 1.0)

        /** Perfect red color. */
        val RED = HDRColor(1.0, 0.0, 0.0, 1.0)

        /** Perfect green color. */
        val GREEN = HDRColor(0.0, 1.0, 0.0, 1.0)

        /** Perfect blue color. */
        val BLUE = HDRColor(0.0, 0.0, 1.0, 1.0)

        /** Perfect yellow color. */
        val YELLOW = HDRColor(1.0, 1.0, 0.0, 1.0)

        /** Perfect teal color. */
        val TEAL = HDRColor(0.0, 1.0, 1.0, 1.0)

        /** Perfect violet color. */
        val VIOLET = HDRColor(1.0, 0.0, 1.0, 1.0)

        /** Perfect transparent color. */
        val TRANSPARENT = HDRColor(0.0, 0.0, 0.0, 0.0)
    }
}
