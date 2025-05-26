package art.luxels.scenes.fractal

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Vector2

/**
 * Representation of a Complex Number (thanks Gerolamo Cardano).
 * @param real the real component
 * @param imaginary the imaginary component
 */
data class Complex(val real: Double, val imaginary: Double) {

    // region Operators

    /**
     * @param other the complex number to add to this
     */
    operator fun plus(other: Complex): Complex {
        return Complex(real + other.real, imaginary + other.imaginary)
    }

    /**
     * @param scale the real number to multiply this with
     */
    operator fun times(scale: Double): Complex {
        return Complex(real * scale, imaginary * scale)
    }

    /**
     * @param other the complex number to multiply this with
     */
    operator fun times(other: Complex): Complex {
        return Complex(
            (real * other.real) - (imaginary * other.imaginary),
            (real * other.imaginary) + (imaginary * other.real),
        )
    }

    // endregion

    // region Math

    /**
     * @return the square of the magnitude of this complex number
     */
    fun squaredMagnitude(): Double {
        return (real * real) + (imaginary * imaginary)
    }

    // endregion

    // region Compatibility

    /**
     * @return a 2D vector corresponding to this complex number
     */
    fun asVector(): Vector<Dimension.D2> {
        return Vector2(real, imaginary)
    }

    // endregion
}
