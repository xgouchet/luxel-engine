package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel

internal class DebugLuxel(
    private val position: Vector<Dimension.D3>,
    private val color: Color,
) : Luxel<Dimension.D3> {
    private var isAlive = true

    // region ColorSource

    override fun color(): HDRColor {
        return color.asHDR()
    }

    // endregion

    // region PositionSource

    override fun position(): Vector<Dimension.D3> {
        return position + (RndGen.vector3.gaussian() * 5.0)
    }

    // endregion

    // region LifespanSource

    override fun onStart() {
        isAlive = true
    }

    override fun onStep(step: Int) {
        isAlive = step < 2
    }

    override fun isAlive(): Boolean {
        return isAlive
    }

    override fun onEnd() {
    }

    // endregion
}
