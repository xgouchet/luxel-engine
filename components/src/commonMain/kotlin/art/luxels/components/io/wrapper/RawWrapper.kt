package art.luxels.components.io.wrapper

import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor

/**
 * A no-op [FilmRasterDataWrapper] keeping the film raw colors as is.
 */
class RawWrapper(film: Film) : FilmRasterDataWrapper(film) {
    // region FilmRasterDataWrapper

    override fun analyzeSourceColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ) {
    }

    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return rawColor
    }

    // endregion
}
