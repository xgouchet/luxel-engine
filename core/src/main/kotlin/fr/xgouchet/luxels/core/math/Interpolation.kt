package fr.xgouchet.luxels.core.math

@Suppress("CommentRegions")
sealed class Interpolation {

    abstract fun factor(t: Double): Double

    fun lerp(a: Double, b: Double, t: Double): Double {
        return a + ((b - a) * t)
    }

    data object Linear : Interpolation() {
        override fun factor(t: Double): Double {
            return t.coerceIn(0.0, 1.0)
        }
    }

    data object Cubic : Interpolation() {
        override fun factor(t: Double): Double {
            val x = t.coerceIn(0.0, 1.0)
            return x * x * (3.0 - 2.0 * x)
        }
    }

    data object Quintic : Interpolation() {
        override fun factor(t: Double): Double {
            val x = t.coerceIn(0.0, 1.0)
            return x * x * x * (x * (x * 6.0 - 15.0) + 10.0)
        }
    }
}
