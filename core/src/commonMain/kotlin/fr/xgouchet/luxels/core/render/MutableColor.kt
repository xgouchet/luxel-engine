package fr.xgouchet.luxels.core.render

import fr.xgouchet.graphikio.color.HDRColor

internal data class MutableColor(
    var r: Double,
    var g: Double,
    var b: Double,
    var a: Double,
) {
    fun asHDR(): HDRColor {
        return HDRColor(r, g, b, a)
    }
}
