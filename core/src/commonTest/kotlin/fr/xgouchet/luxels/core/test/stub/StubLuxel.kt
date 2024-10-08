package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.Luxel

class StubLuxel<D : Dimension> : Luxel<D> {
    override fun color(): HDRColor {
        TODO("Not yet implemented")
    }

    override fun position(): Vector<D> {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStep(step: Int) {
        TODO("Not yet implemented")
    }

    override fun isAlive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onEnd() {
        TODO("Not yet implemented")
    }
}
