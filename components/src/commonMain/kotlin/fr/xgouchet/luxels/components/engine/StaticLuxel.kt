package fr.xgouchet.luxels.components.engine

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.engine.api.Luxel

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
