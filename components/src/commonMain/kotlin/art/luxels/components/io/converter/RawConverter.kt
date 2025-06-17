package art.luxels.components.io.converter

import art.luxels.components.io.wrapper.RawWrapper
import art.luxels.core.render.Film
import art.luxels.imageio.data.RasterData

/**
 * A [FilmConverter] that uses a [RawConverter].
 */
class RawConverter : FilmConverter {
    // region FilmConverter

    override fun convert(film: Film): RasterData {
        return RawWrapper(film)
    }

    // endregion
}
