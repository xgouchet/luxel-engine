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
 *  Note that this [ColorSource] assumes the inputs can vary through time, meaning the final color is computed each time
 *  it is requested. If your color source is static for the Luxel's life span,consider using [WLStaticColorSource].
 *
 * @param waveLength the light's wave length in nanometer
 * @param intensity the color's intensity (default: 1.0)
 */
open class WLVaryingColorSource(waveLength: WaveLengthNanometer, intensity: Double = 1.0) : WLColorSource() {

    private var rawColor: HDRColor = HDRColor.BLACK
    private var scaledColor: HDRColor = HDRColor.BLACK

    /**
     * The light's wave length in nanometer.
     */
    var waveLength: WaveLengthNanometer = waveLength
        set(value) {
            field = value
            rawColor = computeColor(value)
            scaledColor = rawColor * intensity
        }

    /**
     * The color's intensity.
     */
    var intensity: Double = intensity
        set(value) {
            field = value
            scaledColor = rawColor * intensity
        }

    // region ColorSource

    @Suppress("NamedArguments")
    override fun color(): HDRColor {
        val baseColor = computeColor(waveLength)
        return baseColor.scaleColorOnly(intensity)
    }

    // endregion
}
