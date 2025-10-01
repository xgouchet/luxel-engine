package art.luxels.components.io.wrapper

import art.luxels.components.color.ColorCurveTransformer
import art.luxels.components.color.sqrt
import art.luxels.components.io.converter.FilmConverter
import art.luxels.core.render.Film
import art.luxels.core.utils.IdentityTransformer
import art.luxels.core.utils.Transformer
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.data.RasterData

class ChannelCurveWrapper(film: Film) : FilmRasterDataWrapper(film) {

    private val histograms = Array(4) { Array(SDR_BUCKETS) { 0 } }
    private val maxBucketIndex = Array(4) { 0 }
    private val maxBucketValue = Array(4) { 0 }

    private var maxExposure = HDRColor.TRANSPARENT
    private var minExposure = HDRColor(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)
    private var avgExposure = HDRColor.TRANSPARENT
    private var sumSquareExposure = HDRColor.TRANSPARENT

    private var transformer: Transformer<HDRColor, HDRColor> = IdentityTransformer()

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
        val avgMinusStDevExposure = avgExposure - stDevExposure
        val avgPlusStDevExposure = avgExposure + stDevExposure

        println("minExposure: $minExposure")
        println("avg - stDev: $avgMinusStDevExposure")
        println("avgExposure: $avgExposure")
        println("avg + stDev: $avgPlusStDevExposure")
        println("maxExposure: $maxExposure")

        transformer = ColorCurveTransformer(
            listOf(
                minExposure to (HDRColor.BLACK * MAX_SDR_VALUE),
//                avgExposure to (HDRColor.WHITE * LEVEL_AVG),
                avgPlusStDevExposure to (HDRColor.GREY * MAX_SDR_VALUE),
                maxExposure to (HDRColor.WHITE * MAX_SDR_VALUE),
            ),
        )
//
//        for (j in 0..<height) {
//            for (i in 0..<width) {
//                val rawColor = film.getColor(i, j)
//                val indexes = ((rawColor / maxExposure) * MAX_SDR_VALUE).asSDR()
//                histograms[0][indexes.r]++
//                histograms[1][indexes.g]++
//                histograms[2][indexes.b]++
//                histograms[3][indexes.a]++
//                if (histograms[0][indexes.r] > maxBucketValue[0]) {
//                    maxBucketValue[0] = histograms[0][indexes.r]
//                    maxBucketIndex[0] = indexes.r
//                }
//                if (histograms[1][indexes.g] > maxBucketValue[1]) {
//                    maxBucketValue[1] = histograms[1][indexes.g]
//                    maxBucketIndex[1] = indexes.g
//                }
//                if (histograms[2][indexes.b] > maxBucketValue[2]) {
//                    maxBucketValue[2] = histograms[2][indexes.b]
//                    maxBucketIndex[2] = indexes.b
//                }
//                if (histograms[3][indexes.a] > maxBucketValue[3]) {
//                    maxBucketValue[3] = histograms[3][indexes.a]
//                    maxBucketIndex[3] = indexes.a
//                }
//            }
//        }
    }


    override fun convertColor(
        rawColor: HDRColor,
        i: Int,
        j: Int,
    ): Color {
        return transformer.transform(rawColor)
    }

    companion object {
        private const val SDR_BUCKETS = 256

        private const val LEVEL_MIN = 0.0
        private const val LEVEL_AVG_MINUS_STDEV = (1.0 / 4.0) * MAX_SDR_VALUE
        private const val LEVEL_AVG = (1.0 / 3.0) * MAX_SDR_VALUE
        private const val LEVEL_AVG_PLUS_STDEV = (2.0 / 3.0) * MAX_SDR_VALUE
        private const val LEVEL_MAX = MAX_SDR_VALUE

        val CONVERTER: FilmConverter = object : FilmConverter {
            override val name: String = "curve"

            override fun convert(film: Film): RasterData {
                return ChannelCurveWrapper(film)
            }
        }
    }
}