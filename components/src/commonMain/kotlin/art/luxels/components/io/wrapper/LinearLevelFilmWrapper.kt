package art.luxels.components.io.wrapper

import art.luxels.components.io.converter.FilmConverter
import art.luxels.components.io.converter.ProxyFilmConverter
import art.luxels.core.math.EPSILON
import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor

/**
 * A [FilmRasterDataWrapper] applying a basic Linear interpolation to ensure the HDR color maps to the allowed
 * SDR color range.
 * The same filter is applied to all channels.
 */
class LinearLevelFilmWrapper(film: Film) : FilmRasterDataWrapper(film) {

    private var reverseMaxExposure: Double = 1.0

    // region RasterData

    override fun prepare() {
        var maxExposure = 0.0

        for (j in 0..<height) {
            for (i in 0..<width) {
                val rawColor = film.getColor(i, j)
                maxExposure = maxOf(rawColor.r, rawColor.g, rawColor.b, maxExposure)
            }
        }

        if (maxExposure > EPSILON) {
            reverseMaxExposure = (1.0 / maxExposure)
        }
    }

    // endregion

    // region FilmRasterDataWrapper

    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return rawColor * reverseMaxExposure * MAX_SDR_VALUE
    }

    // endregion

    companion object {
        /**
         * A [FilmConverter] using a [LinearLevelFilmWrapper].
         */
        val CONVERTER: FilmConverter = ProxyFilmConverter("linear") { LinearLevelFilmWrapper(it) }
    }
}
