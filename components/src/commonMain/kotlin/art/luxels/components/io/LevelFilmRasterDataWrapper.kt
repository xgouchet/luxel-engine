package art.luxels.components.io

import art.luxels.components.color.sqrt
import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.data.MetadataAttribute
import art.luxels.imageio.data.RasterData

internal class LevelFilmRasterDataWrapper(val film: Film) : RasterData {

    private val reverseMaxExposure: Double

    init {
        // TODO ? val min = 0.0
        var max = 1.0
        val width = film.width
        val height = film.height
        for (j in 0..<height) {
            for (i in 0..<width) {
                val color = film.getColor(i, j)
                max = maxOf(color.r, color.g, color.b, max)
            }
        }
        reverseMaxExposure = 1.0 / max
    }

    // region RasterData

    override val width: Int = film.width

    override val height: Int = film.height

    override fun getColor(x: Int, y: Int): Color {
        return (film.getColor(x, y) * reverseMaxExposure).sqrt() * MAX_SDR_VALUE
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return mapOf()
    }

    // endregion

    companion object {
        internal const val MAX_SDR_VALUE = 255.0
    }
}
