package art.luxels.components.engine

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.Color
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.color.asHDR

/**
 * A basic [Luxel] with a fixed position and color, and a single iteration lifespan.
 */
class StaticLuxel<D : Dimension>(
    private val position: Vector<D>,
    private val color: Color,
) : Luxel<D> {

    private var isAlive = true

    // region PositionSource

    override fun position(): Vector<D> {
        return position
    }

    // endregion

    // region ColorSource

    override fun color(): HDRColor {
        return color.asHDR()
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
}
