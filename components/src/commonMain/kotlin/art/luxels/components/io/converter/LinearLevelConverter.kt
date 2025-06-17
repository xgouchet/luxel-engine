package art.luxels.components.io.converter

import art.luxels.components.io.wrapper.LinearLevelWrapper
import art.luxels.core.render.Film
import art.luxels.imageio.data.RasterData

/**
 * A [FilmConverter] that uses a [LinearLevelWrapper].
 */
class LinearLevelConverter : FilmConverter {
    // region FilmConverter

    override fun convert(film: Film): RasterData {
        return LinearLevelWrapper(film)
    }

    // endregion
}
