package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Resolution

internal abstract class AbstractFilm(
    val resolution: Resolution,
) : Film {

    private val pixelCount = resolution.width * resolution.height
    private val exposure: Array<Color> = Array(pixelCount) { Color(0.0, 0.0, 0.0, 0.0) }
    private var exposedOutsideBounds = 0L
    private var exposedInsideBounds = 0L

    // region Film

    final override val width: Int = resolution.width
    final override val height: Int = resolution.height

    final override fun getColor(i: Int, j: Int): Color {
        if (i !in 0..<resolution.width) return Color.TRANSPARENT
        if (j !in 0..<resolution.height) return Color.TRANSPARENT

        val index = pixelIndex(i, j)
        return exposure[index]
    }

    // endregion

    // region Internal

    internal fun expose(i: Int, j: Int, color: Color, intensity: Double) {
        if (i in 0..<resolution.width && j in 0..<resolution.height) {
            val index = pixelIndex(i, j)
            exposure[index].a += color.a * intensity
            exposure[index].r += color.r * intensity
            exposure[index].g += color.g * intensity
            exposure[index].b += color.b * intensity
        }
    }

    // endregion

    // region Internal

    private fun pixelIndex(i: Int, j: Int): Int {
        return (j * resolution.width) + i
    }

    // endregion
}
