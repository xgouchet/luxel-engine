package art.luxels.components.color

import art.luxels.core.color.ColorSource
import art.luxels.imageio.color.HDRColor
import kotlin.math.pow

typealias WavelengthNanometer = Double

/**
 * A [ColorSource] based on light wavelength. As a reminder, here's a rough conversion table :
 *
 *  - 100 nm = ULTRA VIOLET
 *  - 380–450 nm = VIOLET
 *  - 450–495 nm = BLUE
 *  - 495–570 nm = GREEN
 *  - 570–590 nm = YELLOW
 *  - 590–620 nm = ORANGE
 *  - 620–750 nm = RED
 *  - 1000 nm = INFRA RED
 *
 *  Computation based on https://www.baeldung.com/cs/rgb-color-light-frequency
 *
 * @property waveLength the light's wave length in nanometer
 * @property intensity the color's intensity (default: 1.0)
 * @property gamma the gamma correction factor (default : 0.8)
 *
 */
open class EMSColorSource(
    var waveLength: WavelengthNanometer,
    var intensity: Double = 1.0,
    var gamma: Double = 0.8,
) : ColorSource {
    // TODO add memoization

    // region ColorSource

    @Suppress("NamedArguments")
    override fun color(): HDRColor {
        val redHigh = lerpWithin(waveLength, PURE_GREEN, PURE_YELLOW, Double.MAX_VALUE, Double.MAX_VALUE)
        val green = lerpWithin(waveLength, PURE_BLUE, PURE_TEAL, PURE_YELLOW, PURE_RED)
        val blue = lerpWithin(waveLength, 0.0, 0.0, PURE_TEAL, PURE_GREEN)
        val redLow = lerpWithin(waveLength, 0.0, 0.0, PURE_VIOLET, PURE_BLUE)

        val falloff = lerpWithin(waveLength, MIN_VISIBLE_LIGHT, MIN_FALLOFF, MAX_FALLOFF, MAX_VISIBLE_LIGHT)
        val alpha = lerpWithin(waveLength, MIN_UV_LIGHT, MIN_VISIBLE_LIGHT, MAX_VISIBLE_LIGHT, MAX_IR_LIGHT)

        return HDRColor(
            channel(redHigh + redLow, falloff),
            channel(green, falloff),
            channel(blue, falloff),
            alpha,
        )
    }

    // endregion

    // region Internal

    private fun channel(value: Double, falloff: Double): Double {
        return if (value == 0.0) {
            0.0
        } else {
            intensity * (value * falloff).pow(gamma)
        }
    }

    private fun lerpWithin(
        x: Double,
        min: Double,
        plateauMin: Double,
        plateauMax: Double,
        max: Double,
    ): Double {
        return when {
            x < min -> 0.0
            x < plateauMin -> (x - min) / (plateauMin - min)
            x < plateauMax -> 1.0
            x < max -> (max - x) / (max - plateauMax)
            else -> 0.0
        }
    }

    // endregion

    companion object {
        /** Minimum wavelength of UV light. */
        const val MIN_UV_LIGHT: WavelengthNanometer = 360.0

        /** Minimum wavelength of 'visible' light. */
        const val MIN_VISIBLE_LIGHT: WavelengthNanometer = 380.0
        internal const val MIN_FALLOFF: WavelengthNanometer = 420.0

        internal const val PURE_VIOLET: WavelengthNanometer = 380.0
        internal const val PURE_BLUE: WavelengthNanometer = 440.0
        internal const val PURE_TEAL: WavelengthNanometer = 490.0
        internal const val PURE_GREEN: WavelengthNanometer = 510.0
        internal const val PURE_YELLOW: WavelengthNanometer = 580.0
        internal const val PURE_RED: WavelengthNanometer = 645.0

        internal const val MAX_FALLOFF: WavelengthNanometer = 700.0

        /** Maximum wavelength of 'visible' light. */
        const val MAX_VISIBLE_LIGHT: WavelengthNanometer = 720.0

        /** Maximum wavelength of IR light. */
        const val MAX_IR_LIGHT: WavelengthNanometer = 780.0

        /** Wavelength of dark red light. */
        const val MEAN_DARK_RED: WavelengthNanometer = 710.0

        /** Wavelength of red light. */
        const val MEAN_RED: WavelengthNanometer = 680.0

        /** Wavelength of dark orange light. */
        const val MEAN_DARK_ORANGE: WavelengthNanometer = 650.0

        /** Wavelength of orange light. */
        const val MEAN_ORANGE: WavelengthNanometer = 620.0

        /** Wavelength of yellow light. */
        const val MEAN_YELLOW: WavelengthNanometer = 590.0

        /** Wavelength of lime green light. */
        const val MEAN_LIME_GREEN: WavelengthNanometer = 560.0

        /** Wavelength of green light. */
        const val MEAN_GREEN: WavelengthNanometer = 530.0

        /** Wavelength of cyan light. */
        const val MEAN_CYAN: WavelengthNanometer = 500.0

        /** Wavelength of blue light. */
        const val MEAN_BLUE: WavelengthNanometer = 470.0

        /** Wavelength of violet light. */
        const val MEAN_VIOLET: WavelengthNanometer = 420.0
    }
}
