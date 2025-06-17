package art.luxels.components.io.converter

import art.luxels.core.render.Film
import art.luxels.imageio.data.RasterData

/**
 * A utility converting a given [Film] instance into a [RasterData].
 */
interface FilmConverter {
    /**
     * Converts the input [Film] into a [RasterData] for writing into an image file.
     * @param film the film to convert
     * @return the [RasterData]
     */
    fun convert(film: Film): RasterData
}
