package fr.xgouchet.luxels.core.color

import kotlin.math.ln
import kotlin.math.pow

typealias Kelvin = Double

/**
 * A [ColorSource] based on black body radiation theory.
 *
 * Computation based on http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
 *
 * @param temperature the temperature of the black body in Kelvin
 * @param magnitude the magnitude of the color (default: 1.0)
 */
open class BBRColorSource(
    var temperature: Kelvin,
    var magnitude: Double = 1.0,
) : ColorSource {

    // TODO write tests
    // TODO add memoization

    // region ColorSource

    override fun color(): Color {
        return temperatureToRGB(temperature)
    }

    // endregion

    // region Internal

    private fun temperatureToRGB(kelvin: Double): Color {
        val temp = kelvin / 100.0

        val red: Double = if (temp <= 66.0) {
            1.0
        } else {
            1.29293618606 * (temp - 60).pow(-0.1332047592)
        }

        val green: Double = if (temp <= 66.0) {
            (0.390081578769 * ln(temp)) - 0.631841443788
        } else {
            1.12989086089 * (temp - 60).pow(-0.0755148492)
        }

        val blue: Double = if (temp < 19.0) {
            0.0
        } else if (temp < 66.0) {
            (0.54320678911 * ln(temp - 10.0)) - 1.19625408914
        } else {
            1.0
        }

        val alpha = if (temp < 10.0) (temp / 10.0).pow(3.0) else 1.0

//        val intensity = kelvin.pow(0.10418521566)

        return Color(
            red.coerceIn(0.0, 1.0),
            green.coerceIn(0.0, 1.0),
            blue.coerceIn(0.0, 1.0),
            alpha.coerceIn(0.0, 1.0),
        ) * magnitude
    }

    // endregion

    companion object {
        /** The absolute zero temperature in Kelvin (i.e.: 0). */
        const val ABSOLUTE_ZERO: Kelvin = 0.0
    }
}
