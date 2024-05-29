package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.system.printSystemMemInfo

internal abstract class AbstractFilm(
    val resolution: Resolution,
) : Film {

    private val pixelCount = resolution.width * resolution.height
    private val exposure: Array<MutableColor> = Array(pixelCount) { MutableColor(0.0, 0.0, 0.0, 0.0) }

    // region Film

    final override val width: Int = resolution.width
    final override val height: Int = resolution.height

    final override fun getColor(i: Int, j: Int): HDRColor {
        if (i !in 0..<resolution.width) return HDRColor.TRANSPARENT
        if (j !in 0..<resolution.height) return HDRColor.TRANSPARENT

        val index = pixelIndex(i, j)
        return exposure[index].asHDR()
    }

    // endregion

    // region Internal

    internal fun expose(
        i: Int,
        j: Int,
        color: Color,
        intensity: Double,
    ) {
        val inColor = color.asHDR()
        if (i in 0..<resolution.width && j in 0..<resolution.height) {
            val index = pixelIndex(i, j)
            exposure[index].r += inColor.r * intensity
            exposure[index].g += inColor.g * intensity
            exposure[index].b += inColor.b * intensity
            exposure[index].a += inColor.a * intensity
        }
    }

    // endregion

    // region Internal

    private fun pixelIndex(i: Int, j: Int): Int {
        return (j * resolution.width) + i
    }

    // endregion
}
