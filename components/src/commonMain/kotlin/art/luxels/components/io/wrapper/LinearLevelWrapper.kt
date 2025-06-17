package art.luxels.components.io.wrapper

import art.luxels.core.math.EPSILON
import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor

/**
 * A [FilmRasterDataWrapper] applying a basic Linear interpolation to ensure the HDR color maps to the allowed
 * SDR color range.
 * The same filter is applied to all channels.
 */
class LinearLevelWrapper(film: Film) : FilmRasterDataWrapper(film) {

    private var maxExposure: Double = 0.0
    private var reverseMaxExposure: Double = 1.0

    // region FilmRasterDataWrapper

    override fun analyzeSourceColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ) {
        maxExposure = maxOf(rawColor.r, rawColor.g, rawColor.b, maxExposure)
        if (maxExposure > EPSILON) {
            reverseMaxExposure = (1.0 / maxExposure)
        }
    }

    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return rawColor * reverseMaxExposure * MAX_SDR_VALUE
    }

    // endregion
}
