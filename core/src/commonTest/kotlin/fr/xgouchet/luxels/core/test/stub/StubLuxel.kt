package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleCallWithReturn
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

class StubLuxel<D : Dimension> : Luxel<D>, Stub {

    override val callRecorder = SimpleCallRecorder()

    override val responseHandler = SimpleResponseHandler()

    override fun color(): HDRColor {
        return handleCallWithReturn("color")
    }

    override fun position(): Vector<D> {
        return handleCallWithReturn("position")
    }

    override fun onStart() {
        handleUnitCall("onStart")
    }

    override fun onStep(step: Int) {
        handleUnitCall("onStep", mapOf("step" to step))
    }

    override fun onEnd() {
        handleUnitCall("onEnd")
    }

    override fun isAlive(): Boolean {
        return handleCallWithReturn("isAlive")
    }
}
