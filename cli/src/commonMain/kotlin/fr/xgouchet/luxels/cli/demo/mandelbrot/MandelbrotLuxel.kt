package fr.xgouchet.luxels.cli.demo.mandelbrot

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.Luxel

internal class MandelbrotLuxel(
    val c: Complex,
    val baseColor: HDRColor,
    maxIterations: Int,
) : Luxel<Dimension.D2> {

    private val iterationBeforeEscape: Int

    init {
        var i = 0
        var zn = Complex(0.0, 0.0)
        while (i < maxIterations) {
            zn = (zn * zn) + c
            if (zn.squaredMagnitude() > OUTSIDE_MAGNITUDE_THRESHOLD) {
                break
            }
            i++
        }
        iterationBeforeEscape = if (i < maxIterations) i else 0
    }

    override fun color(): HDRColor {
        return baseColor * iterationBeforeEscape.toDouble()
    }

    override fun position(): Vector<Dimension.D2> {
        return c.asVector()
    }

    override fun onStart() {

    }

    override fun onStep(step: Int) {
    }

    override fun isAlive(): Boolean {
        return false
    }

    override fun onEnd() {
    }

    companion object {

        private const val OUTSIDE_MAGNITUDE_THRESHOLD = 25.0
    }
}
