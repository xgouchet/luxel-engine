package art.luxels.components.io.wrapper

import art.luxels.components.io.converter.FilmConverter
import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.data.RasterData

/**
 * A no-op [FilmRasterDataWrapper] keeping the film raw colors as is.
 */
class RawWrapper(film: Film) : FilmRasterDataWrapper(film) {

    // region Raster Data

    override fun prepare() {
    }

    // endregion

    // region FilmRasterDataWrapper

    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return rawColor
    }

    // endregion

    companion object {
        val CONVERTER: FilmConverter = object : FilmConverter {
            override val name: String = "raw"

            override fun convert(film: Film): RasterData {
                return RawWrapper(film)
            }
        }
    }
}
