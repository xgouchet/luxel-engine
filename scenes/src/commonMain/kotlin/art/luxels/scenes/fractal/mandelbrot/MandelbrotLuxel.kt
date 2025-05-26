package art.luxels.scenes.fractal.mandelbrot

import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.HDRColor
import art.luxels.scenes.fractal.Complex

class MandelbrotLuxel(
    val c: Complex,
    val baseColor: HDRColor,
    maxIterations: Int,
) : Luxel<D2> {

    private val iterationBeforeEscape: Int
    private var isAlive = true

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

    // region ColorSource

    override fun color(): HDRColor {
        return baseColor * iterationBeforeEscape.toDouble()
    }

    // endregion

    // region PositionSource

    override fun position(): Vector<D2> {
        return c.asVector()
    }

    // endregion

    // region LifespanSource

    override fun onStart() {
        isAlive = true
    }

    override fun onStep(step: Int) {
        isAlive = false
    }

    override fun isAlive(): Boolean {
        return isAlive
    }

    override fun onEnd() {
    }

    // endregion

    companion object {
        private const val OUTSIDE_MAGNITUDE_THRESHOLD = 25.0
    }
}
