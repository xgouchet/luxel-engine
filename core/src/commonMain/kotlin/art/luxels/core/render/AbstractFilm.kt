package art.luxels.core.render

import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.color.asHDR

/**
 * A basic abstract [Film].
 * @param resolution the resolution of the film
 */
abstract class AbstractFilm(
    val resolution: Resolution,
) : Film {

    private val pixelCount = resolution.pixelCount

    private val exposure: Array<MutableColor> by lazy {
        Array(pixelCount) { MutableColor(0.0, 0.0, 0.0, 0.0) }
    }

    private var isExposed = false

    // region Film

    final override val width: Int = resolution.width
    final override val height: Int = resolution.height

    final override fun getColor(i: Int, j: Int): HDRColor {
        if (i !in 0..<resolution.width) return HDRColor.TRANSPARENT
        if (j !in 0..<resolution.height) return HDRColor.TRANSPARENT

        val index = pixelIndex(i, j)
        return exposure[index].asHDR()
    }

    override fun hasData(): Boolean {
        return isExposed
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
        isExposed = true
    }

    // endregion

    // region Internal

    private fun pixelIndex(i: Int, j: Int): Int {
        return (j * resolution.width) + i
    }

    // endregion
}
