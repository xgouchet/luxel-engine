package art.luxels.components.color

import art.luxels.imageio.color.HDRColor
import kotlin.math.pow
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

/**
 *
 */
fun HDRColor.gammaCorrect(intensity: Double = 1.0, gamma: Double = 0.8): HDRColor {
    return HDRColor(
        r.gammaCorrectChannel(intensity, gamma),
        g.gammaCorrectChannel(intensity, gamma),
        b.gammaCorrectChannel(intensity, gamma),
        a,
    )
}

private fun Double.gammaCorrectChannel(intensity: Double, gamma: Double): Double {
    return if (this == 0.0) {
        0.0
    } else {
        intensity * this.pow(gamma)
    }
}
