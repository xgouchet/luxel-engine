package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel

internal class DebugLuxel(
    private val position: Vector3,
    private val color: Color,
) : Luxel {
    private var isAlive = true

    // region ColorSource

    override fun color(): Color {
        return color
    }

    // endregion

    // region PositionSource

    override fun position(): Vector3 {
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
