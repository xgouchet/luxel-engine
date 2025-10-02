package art.luxels.components.io.wrapper

import art.luxels.components.io.converter.FilmConverter
import art.luxels.components.io.converter.ProxyFilmConverter
import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.data.RasterData

/**
 * A no-op [FilmRasterDataWrapper] keeping the film raw colors as is.
 */
class RawFilmWrapper(film: Film) : FilmRasterDataWrapper(film) {

    // region RasterData

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
        /**
         * A [FilmConverter] using a [RawFilmWrapper].
         */
        val CONVERTER: FilmConverter = ProxyFilmConverter("raw") { RawFilmWrapper(it) }
    }
}
