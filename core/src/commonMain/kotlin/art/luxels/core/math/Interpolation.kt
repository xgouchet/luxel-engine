package art.luxels.core.math

@Suppress("CommentRegions")
/**
 * A generic interpolation class.
 */
sealed class Interpolation {
    /**
     * Converts a linear factor between 0 and 1 into a factor following this
     * interpolation's formula.
     */
    abstract fun factor(t: Double): Double

    /**
     * Interpolates between a and b, using t as a factor.
     * @param a the first value to interpolate between
     * @param b the second value to interpolate between
     * @param t the percentage between a and b to interpolate.
     * A value of 0.0 will return `a`. A value of 1.0 will return `b`.
     * A value of 0.5 will return the midpoint between `a` and `b`.
     */
    fun interpolate(
        a: Double,
        b: Double,
        t: Double,
    ): Double {
        return a + ((b - a) * factor(t))
    }

    /**
     * A linear interpolation.
     */
    data object Linear : Interpolation() {
        override fun factor(t: Double): Double {
            return t.coerceIn(0.0, 1.0)
        }
    }

    /**
     * A cubic interpolation, fast and smooth.
     */
    data object Cubic : Interpolation() {
        override fun factor(t: Double): Double {
            val x = t.coerceIn(0.0, 1.0)
            return x * x * (3.0 - 2.0 * x)
        }
    }

    /**
     * A Quintic interpolation, slow and extra smooth.
     */
    data object Quintic : Interpolation() {
        override fun factor(t: Double): Double {
            val x = t.coerceIn(0.0, 1.0)
            return x * x * x * (x * (x * 6.0 - 15.0) + 10.0)
        }
    }
}
