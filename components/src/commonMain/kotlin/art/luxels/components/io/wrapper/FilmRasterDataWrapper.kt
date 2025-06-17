package art.luxels.components.io.wrapper

import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.data.MetadataAttribute
import art.luxels.imageio.data.RasterData

/**
 * A wrapper around a [Film] instance that can be used as a readable [RasterData]
 * to be written in an image file.
 *
 * @property film the film to wrap
 */
abstract class FilmRasterDataWrapper(val film: Film) : RasterData {

    // region RasterData

    override val width: Int = film.width

    override val height: Int = film.height

    override fun prepare() {
        for (j in 0..<height) {
            for (i in 0..<width) {
                val color = film.getColor(i, j)
                analyzeSourceColor(color, i, j)
            }
        }
    }

    override fun getColor(x: Int, y: Int): Color {
        return convertColor(film.getColor(x, y), x, y)
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return mapOf()
    }

    // endregion

    // region FilmRasterDataWrapper

    /**
     * Method to take into account the raw color at the given position.
     * Wrappers can use that to drive how the color should eventually be converted.
     * @param rawColor the raw color of the pixel
     * @param i the column index of the pixel
     * @param j the row index of the pixel
     */
    abstract fun analyzeSourceColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    )

    /**
     * Method to convert the raw color at the given position into the final color to use in the [RasterData].
     * @param rawColor the raw color of the pixel
     * @param i the column index of the pixel
     * @param j the row index of the pixel
     * @return the color of the pixel
     */
    abstract fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color

    // endregion

    companion object {
        internal const val MAX_SDR_VALUE = 255.0
    }
}
