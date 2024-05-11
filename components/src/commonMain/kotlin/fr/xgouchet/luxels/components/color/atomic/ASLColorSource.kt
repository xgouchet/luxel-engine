package fr.xgouchet.luxels.components.color.atomic

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.random.RndGen

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
abstract class ASLColorSource(
    val name: String,
    val symbol: String,
    val number: Int,
    private val spectralLines: List<SpectralLine>,
) : EMSColorSource(0.0, 0.0, 0.8) {
    /**
     * Describe one of the spectral lines a given element can emit.
     * @property waveLength the wavelength of the spectral line (in nm)
     * @property intensity the (relative) intensity of the spectral line
     */
    data class SpectralLine(
        val waveLength: WavelengthNanometer,
        val intensity: Double,
    )

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
