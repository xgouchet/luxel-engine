package art.luxels.components.color

import art.luxels.core.color.ColorSource
import art.luxels.imageio.color.HDRColor

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
 *
 */
open class WLStaticColorSource(val waveLength: WaveLengthNanometer, val intensity: Double = 1.0) : WLColorSource() {

    private val color by lazy { computeColor(waveLength).scaleColorOnly(intensity) }

    // region ColorSource

    override fun color(): HDRColor {
        return color
    }

    // endregion
}
