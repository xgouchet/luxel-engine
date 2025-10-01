package art.luxels.components.color

import art.luxels.components.geometry.CurveTransformer
import art.luxels.core.utils.Transformer
import art.luxels.imageio.color.HDRColor

// TODO share code with Matrix class
class ColorCurveTransformer(
    curvePoints: List<Pair<HDRColor, HDRColor>>,
) : Transformer<HDRColor, HDRColor> {

    val curveR = CurveTransformer(curvePoints.map { it.first.r to it.second.r }.filter { it.first >= 0.0 })
    val curveG = CurveTransformer(curvePoints.map { it.first.g to it.second.g }.filter { it.first >= 0.0 })
    val curveB = CurveTransformer(curvePoints.map { it.first.b to it.second.b }.filter { it.first >= 0.0 })
    val curveA = CurveTransformer(curvePoints.map { it.first.r to it.second.a }.filter { it.first >= 0.0 })

//    init {
//
//        println("RED")
//        curvePoints.forEach {
//            println("${it.first.r} -> ${it.second.r}")
//        }
//        curveR.debugPrint()
//
//
//        println("GREEN")
//        curvePoints.forEach {
//            println("${it.first.g} -> ${it.second.g}")
//        }
//        curveG.debugPrint()
//    }

    override fun transform(input: HDRColor): HDRColor {
        return HDRColor(
            curveR.transform(input.r),
            curveG.transform(input.g),
            curveB.transform(input.b),
            curveA.transform(input.a),
        )
    }

}
