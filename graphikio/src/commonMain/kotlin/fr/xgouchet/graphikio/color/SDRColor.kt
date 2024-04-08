package fr.xgouchet.graphikio.color

import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_BLUE
import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_GREEN
import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_RED
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Represents a Color in standard RGBA space.
 * Each channel holds an integer value in [0…255] range.
 *
 * @property r the red component.
 * @property g the green component.
 * @property b the blue component.
 * @property a the alpha component.
 * @param isCoerced whether the value had to be coerced within the [0…255] range.
 */
data class SDRColor(
    val r: Int,
    val g: Int,
    val b: Int,
    val a: Int,
    private val isCoerced: Boolean,
) : Color {

    init {
        check(r in CHANNEL_RANGE)
        check(g in CHANNEL_RANGE)
        check(b in CHANNEL_RANGE)
        check(a in CHANNEL_RANGE)
    }

    // region Operators

    /**
     * @param other the color to add to this color
     * @return a color with each component as the component in this color plus the matching component in the given input
     */
    operator fun plus(other: SDRColor): SDRColor {
        return bindInRange(r + other.r, g + other.g, b + other.b, a + other.a)
    }

    /**
     * @param scale the factor by which to multiply the color
     * @return a color with each component multiplied by the given input
     */
    operator fun times(scale: Double): SDRColor {
        return bindInRange(r * scale, g * scale, b * scale, a * scale)
    }

    // endregion

    // region Color operations

    /**
     * @return the luminance of this color (perceived luminosity) in a [0…255] range
     */
    fun luminance(): Int {
        return ((LUMINANCE_RED * r) + (LUMINANCE_GREEN * g) + (LUMINANCE_BLUE * b)).roundToInt()
    }

    /**
     * @return the hue of this color (in a [0…360] range)
     */
    fun hue(): Int {
        val min = min(min(r, g), b)
        val max = max(max(r, g), b)
        val delta = (max - min).toDouble()

        val h = when (max) {
            min -> 0.0
            r -> (g - b) / delta
            g -> 2.0 + ((b - r) / delta)
            b -> 4.0 + ((r - g) / delta)
            else -> 0.0
        }

        return if (h < 0.0) {
            ((h + 6.0) * 60).roundToInt()
        } else {
            (h * 60).roundToInt()
        }
    }

    /**
     * @return the saturation of this color (in a [0…255] range).
     */
    fun saturation(): Int {
        val min = min(min(r, g), b)
        val max = max(max(r, g), b)
        val delta = (max - min).toDouble()
        return if (max == 0) {
            0
        } else {
            ((delta * MAX_VALUE) / max).roundToInt()
        }
    }

    /**
     * @return the value of this color (in a [0…255] range).
     */
    fun value(): Int {
        return max(max(r, g), b)
    }

    // endregion

    // region Any

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SDRColor

        if (r != other.r) return false
        if (g != other.g) return false
        if (b != other.b) return false
        if (a != other.a) return false

        return true
    }

    override fun hashCode(): Int {
        var result = r
        result = 31 * result + g
        result = 31 * result + b
        result = 31 * result + a
        return result
    }

    // endregion

    companion object {
        internal const val MAX_VALUE = 255
        internal const val HALF_VALUE = 128
        internal val CHANNEL_RANGE = 0..MAX_VALUE

        // region Internal

        internal fun bindInRange(r: Double, g: Double, b: Double, a: Double): SDRColor {
            return bindInRange(
                r.roundToInt(),
                g.roundToInt(),
                b.roundToInt(),
                a.roundToInt(),
            )
        }

        internal fun bindInRange(r: Int, g: Int, b: Int, a: Int): SDRColor {
            var isCoerced = false
            val ri = if (r in CHANNEL_RANGE) {
                r
            } else {
                isCoerced = true
                r.coerceIn(CHANNEL_RANGE)
            }
            val gi = if (g in CHANNEL_RANGE) {
                g
            } else {
                isCoerced = true
                g.coerceIn(CHANNEL_RANGE)
            }
            val bi = if (b in CHANNEL_RANGE) {
                b
            } else {
                isCoerced = true
                b.coerceIn(CHANNEL_RANGE)
            }
            val ai = if (a in CHANNEL_RANGE) {
                a
            } else {
                isCoerced = true
                a.coerceIn(CHANNEL_RANGE)
            }
            return SDRColor(ri, gi, bi, ai, isCoerced)
        }

        // endregion

        /** Perfect White color. */
        val WHITE = SDRColor(MAX_VALUE, MAX_VALUE, MAX_VALUE, MAX_VALUE)

        /** Perfect half grey color. */
        val GREY = SDRColor(HALF_VALUE, HALF_VALUE, HALF_VALUE, MAX_VALUE)

        /** Perfect black color. */
        val BLACK = SDRColor(0, 0, 0, MAX_VALUE)

        /** Perfect red color. */
        val RED = SDRColor(MAX_VALUE, 0, 0, MAX_VALUE)

        /** Perfect green color. */
        val GREEN = SDRColor(0, MAX_VALUE, 0, MAX_VALUE)

        /** Perfect blue color. */
        val BLUE = SDRColor(0, 0, MAX_VALUE, MAX_VALUE)

        /** Perfect yellow color. */
        val YELLOW = SDRColor(MAX_VALUE, MAX_VALUE, 0, MAX_VALUE)

        /** Perfect teal color. */
        val TEAL = SDRColor(0, MAX_VALUE, MAX_VALUE, MAX_VALUE)

        /** Perfect violet color. */
        val VIOLET = SDRColor(MAX_VALUE, 0, MAX_VALUE, MAX_VALUE)

        /** Perfect transparent color. */
        val TRANSPARENT = SDRColor(0, 0, 0, 0)
    }
}
