package art.luxels.core.render

import art.luxels.imageio.color.HDRColor

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
