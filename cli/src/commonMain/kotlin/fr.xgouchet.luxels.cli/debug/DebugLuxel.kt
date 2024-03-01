package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.Luxel

class DebugLuxel(
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
        return position
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
