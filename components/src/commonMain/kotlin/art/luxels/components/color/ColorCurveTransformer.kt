package art.luxels.components.color

import art.luxels.components.geometry.CurveTransformer
import art.luxels.core.utils.Transformer
import art.luxels.imageio.color.HDRColor

/**
 * Transforms an input color by applying a curve transform on each channel.
 * @param curvePoints a list of pair of points (each pair maps an input to the desired output)
 */
// TODO share code with Matrix class
class ColorCurveTransformer(curvePoints: List<Pair<HDRColor, HDRColor>>) : Transformer<HDRColor, HDRColor> {

    private val curveR = CurveTransformer(curvePoints.map { it.first.r to it.second.r }.filter { it.first >= 0.0 })
    private val curveG = CurveTransformer(curvePoints.map { it.first.g to it.second.g }.filter { it.first >= 0.0 })
    private val curveB = CurveTransformer(curvePoints.map { it.first.b to it.second.b }.filter { it.first >= 0.0 })
    private val curveA = CurveTransformer(curvePoints.map { it.first.r to it.second.a }.filter { it.first >= 0.0 })

    // region Transformer

    override fun transform(input: HDRColor): HDRColor {
        return HDRColor(
            curveR.transform(input.r),
            curveG.transform(input.g),
            curveB.transform(input.b),
            curveA.transform(input.a),
        )
    }

    // endregion
}
