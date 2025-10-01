package art.luxels.components.io.wrapper

import art.luxels.components.color.ColorCurveTransformer
import art.luxels.components.color.sqrt
import art.luxels.components.io.converter.FilmConverter
import art.luxels.components.io.converter.ProxyFilmConverter
import art.luxels.core.render.Film
import art.luxels.core.utils.IdentityTransformer
import art.luxels.core.utils.Transformer
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor

/**
 * A [FilmRasterDataWrapper] which applies a [ColorCurveTransformer] on each channel to
 * get an automatic adjustment when writing HDR data to an SDR format.
 * @param film the film to wrap
 */
class ChannelCurveFilmWrapper(film: Film) : FilmRasterDataWrapper(film) {

    private var maxExposure = HDRColor.TRANSPARENT
    private var minExposure = HDRColor(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)
    private var avgExposure = HDRColor.TRANSPARENT
    private var sumSquareExposure = HDRColor.TRANSPARENT

    private var transformer: Transformer<HDRColor, HDRColor> = IdentityTransformer()

    // region RasterData

    override fun prepare() {
        for (j in 0..<height) {
            for (i in 0..<width) {
                val rawColor = film.getColor(i, j)
                maxExposure = HDRColor(
                    maxOf(rawColor.r, maxExposure.r),
                    maxOf(rawColor.g, maxExposure.g),
                    maxOf(rawColor.b, maxExposure.b),
                    maxOf(rawColor.a, maxExposure.a),
                )
                minExposure = HDRColor(
                    minOf(rawColor.r, minExposure.r),
                    minOf(rawColor.g, minExposure.g),
                    minOf(rawColor.b, minExposure.b),
                    minOf(rawColor.a, minExposure.a),
                )
                avgExposure += rawColor
                sumSquareExposure += (rawColor * rawColor)
            }
        }

        val n = (width * height).toDouble()
        avgExposure /= n
        val stDevExposure = ((sumSquareExposure / n) - (avgExposure * avgExposure)).sqrt()
        val avgPlusStDevExposure = avgExposure + stDevExposure

        transformer = ColorCurveTransformer(
            listOf(
                minExposure to (HDRColor.BLACK * MAX_SDR_VALUE),
                avgPlusStDevExposure to (HDRColor.GREY * MAX_SDR_VALUE),
                maxExposure to (HDRColor.WHITE * MAX_SDR_VALUE),
            ),
        )
    }

    // endregion

    // region FilmRasterDataWrapper

    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return transformer.transform(rawColor)
    }

    // endregion

    companion object {

        /**
         * A [FilmConverter] using a [ChannelCurveFilmWrapper].
         */
        val CONVERTER: FilmConverter = ProxyFilmConverter("curve") { ChannelCurveFilmWrapper(it) }
    }
}
