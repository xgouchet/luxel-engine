package art.luxels.components.io.converter

import art.luxels.components.io.wrapper.FilmRasterDataWrapper
import art.luxels.core.render.Film
import art.luxels.imageio.data.RasterData

/**
 * A [FilmConverter] using a lambda to convert the [Film].
 * @param name the name of the converter
 * @property wrapperProvider a lambda to create a [FilmRasterDataWrapper]
 */
class ProxyFilmConverter(override val name: String, val wrapperProvider: (Film) -> FilmRasterDataWrapper) :
    FilmConverter {

    // region FilmConverter

    override fun convert(film: Film): RasterData {
        return wrapperProvider(film)
    }

    // endregion
}
