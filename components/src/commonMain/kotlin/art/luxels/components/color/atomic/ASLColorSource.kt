package art.luxels.components.color.atomic

import art.luxels.components.color.WLVaryingColorSource
import art.luxels.components.color.WaveLengthNanometer
import art.luxels.core.color.ColorSource
import art.luxels.core.math.random.RndGen
import art.luxels.imageio.color.HDRColor

/**
 * A [ColorSource] based on Atomic Spectral Line theory.
 *
 * Each atomic element will emit color along specific spectral lines, resulting in
 * specific color palettes.
 * @property name the name of the element (e.g.: "Mercury")
 * @property symbol the atomic symbol of the element (e.g.: "Hg")
 * @property number the atomic number of the element (e.g.: 80)
 * @param spectralLines the list of spectral lines for this element
 */
open class ASLColorSource(
    val name: String,
    val symbol: String,
    val number: Int,
    internal val spectralLines: List<SpectralLine>,
) : WLVaryingColorSource(0.0, 0.0) {
    /**
     * Describe one of the spectral lines a given element can emit.
     * @property waveLength the wavelength of the spectral line (in nm)
     * @property intensity the (relative) intensity of the spectral line
     */
    data class SpectralLine(val waveLength: WaveLengthNanometer, val intensity: Double)

    // region ColorSource

    override fun color(): HDRColor {
        val index = RndGen.int.inRange(0, spectralLines.size)
        val line = spectralLines[index]
        waveLength = line.waveLength
        intensity = line.intensity
        return super.color()
    }

    // endregion
}
