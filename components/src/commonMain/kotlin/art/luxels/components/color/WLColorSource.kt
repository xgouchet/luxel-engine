package art.luxels.components.color

import art.luxels.core.color.ColorSource
import art.luxels.imageio.color.HDRColor

typealias WaveLengthNanometer = Double

/**
 * An abstract [ColorSource] based on light wavelength. As a reminder, here's a rough conversion table :
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
 *  This doesn't implement the [ColorSource.color] method, but only provides utility methods to compute a color from a
 *  [WaveLengthNanometer].
 *
 */
abstract class WLColorSource : ColorSource {

    // region WLColorSource

    /**
     * Computes a color based on light's wavelength.
     * @param waveLength the wavelength (in nanometer)
     */
    protected fun computeColor(waveLength: WaveLengthNanometer): HDRColor {
        val redHigh = lerpWithin(waveLength, PURE_GREEN, PURE_YELLOW, Double.MAX_VALUE, Double.MAX_VALUE)
        val green = lerpWithin(waveLength, PURE_BLUE, PURE_TEAL, PURE_YELLOW, PURE_RED)
        val blue = lerpWithin(waveLength, 0.0, 0.0, PURE_TEAL, PURE_GREEN)
        val redLow = lerpWithin(waveLength, 0.0, 0.0, PURE_VIOLET, PURE_BLUE)

        val falloff = lerpWithin(waveLength, MIN_VISIBLE_LIGHT, MIN_FALLOFF, MAX_FALLOFF, MAX_VISIBLE_LIGHT)
        val alpha = lerpWithin(waveLength, MIN_UV_LIGHT, MIN_VISIBLE_LIGHT, MAX_VISIBLE_LIGHT, MAX_IR_LIGHT)

        return HDRColor(
            ((redHigh + redLow) * falloff),
            (green * falloff),
            (blue * falloff),
            alpha,
        )
    }

    // endregion

    // region Internal

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
        const val MIN_UV_LIGHT: WaveLengthNanometer = 360.0

        /** Minimum wavelength of 'visible' light. */
        const val MIN_VISIBLE_LIGHT: WaveLengthNanometer = 380.0
        internal const val MIN_FALLOFF: WaveLengthNanometer = 420.0

        internal const val PURE_VIOLET: WaveLengthNanometer = 380.0
        internal const val PURE_BLUE: WaveLengthNanometer = 440.0
        internal const val PURE_TEAL: WaveLengthNanometer = 490.0
        internal const val PURE_GREEN: WaveLengthNanometer = 510.0
        internal const val PURE_YELLOW: WaveLengthNanometer = 580.0
        internal const val PURE_RED: WaveLengthNanometer = 645.0

        internal const val MAX_FALLOFF: WaveLengthNanometer = 700.0

        /** Maximum wavelength of 'visible' light. */
        const val MAX_VISIBLE_LIGHT: WaveLengthNanometer = 720.0

        /** Maximum wavelength of IR light. */
        const val MAX_IR_LIGHT: WaveLengthNanometer = 780.0

        /** Wavelength of dark red light. */
        const val MEAN_DARK_RED: WaveLengthNanometer = 710.0

        /** Wavelength of red light. */
        const val MEAN_RED: WaveLengthNanometer = 680.0

        /** Wavelength of dark orange light. */
        const val MEAN_DARK_ORANGE: WaveLengthNanometer = 650.0

        /** Wavelength of orange light. */
        const val MEAN_ORANGE: WaveLengthNanometer = 620.0

        /** Wavelength of yellow light. */
        const val MEAN_YELLOW: WaveLengthNanometer = 590.0

        /** Wavelength of lime green light. */
        const val MEAN_LIME_GREEN: WaveLengthNanometer = 560.0

        /** Wavelength of green light. */
        const val MEAN_GREEN: WaveLengthNanometer = 530.0

        /** Wavelength of cyan light. */
        const val MEAN_CYAN: WaveLengthNanometer = 500.0

        /** Wavelength of blue light. */
        const val MEAN_BLUE: WaveLengthNanometer = 470.0

        /** Wavelength of violet light. */
        const val MEAN_VIOLET: WaveLengthNanometer = 420.0
    }
}
