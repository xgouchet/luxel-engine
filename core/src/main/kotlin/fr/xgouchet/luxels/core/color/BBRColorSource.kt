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
 */
data class BBRColorSource(
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

    enum class StarClass(
        val temperature: Kelvin,
        val magnitude: Double,
    ) {

        O5(54000.0, -10.0),
        O6(45000.0, -8.8),
        O7(43300.0, -8.6),
        O8(40600.0, -8.2),
        O9(37800.0, -7.7),
        B0(29200.0, -6.0),
        B1(23000.0, -4.4),
        B2(21000.0, -3.8),
        B3(17600.0, -2.6),
        B5(15200.0, -1.6),
        B6(14300.0, -1.2),
        B7(13500.0, -0.84),
        B8(12300.0, -0.23),
        B9(11400.0, 0.29),
        A0(9600.0, 1.4),
        A1(9330.0, 1.6),
        A2(9040.0, 1.8),
        A3(8750.0, 2.1),
        A4(8480.0, 2.3),
        A5(8310.0, 2.4),
        A7(7920.0, 2.7),
        F0(7350.0, 3.2),
        F2(7050.0, 3.5),
        F3(6850.0, 3.7),
        F5(6700.0, 3.8),
        F6(6550.0, 4.0),
        F7(6400.0, 4.1),
        F8(6300.0, 4.2),
        G0(6050.0, 4.5),
        G1(5930.0, 4.6),
        G2(5800.0, 4.8),
        G5(5660.0, 4.9),
        G8(5440.0, 5.2),
        K0(5240.0, 5.4),
        K1(5110.0, 5.6),
        K2(4960.0, 5.8),
        K3(4800.0, 6.0),
        K4(4600.0, 6.3),
        K5(4400.0, 6.6),
        K7(4000.0, 7.3),
        M0(3750.0, 7.7),
        M1(3700.0, 7.8),
        M2(3600.0, 7.9),
        M3(3500.0, 8.1),
        M4(3400.0, 8.3),
        M5(3200.0, 8.7),
        M6(3100.0, 8.9),
        M7(2900.0, 9.4),
        M8(2700.0, 9.9),
        L0(2600.0, 1.0),
        L3(2200.0, 1.0),
        L8(1500.0, 1.0),
        T2(1400.0, 1.0),
        T6(1000.0, 1.0),
        T8(800.0, 1.0),
    }

    companion object {
        const val ABSOLUTE_ZERO: Kelvin = 0.0
    }
}
