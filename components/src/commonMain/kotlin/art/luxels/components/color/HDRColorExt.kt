package art.luxels.components.color

import art.luxels.imageio.color.HDRColor
import kotlin.math.sqrt

/**
 * Applies the `sqrt` function on each component.
 */
fun HDRColor.sqrt(): HDRColor {
    return HDRColor(
        r = sqrt(r),
        g = sqrt(g),
        b = sqrt(b),
        a = sqrt(a),
    )
}
