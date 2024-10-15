package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.test.stub.core.CallRecorder
import fr.xgouchet.luxels.core.test.stub.core.ResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleCallWithReturn
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

class StubFilm(resolution: Resolution) : Film, Stub {

    override val callRecorder: CallRecorder = SimpleCallRecorder()
    override val responseHandler: ResponseHandler = SimpleResponseHandler()

    override val width: Int = resolution.width
    override val height: Int = resolution.height

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        handleUnitCall(
            "expose",
            mapOf(
                "position" to position,
                "color" to color,
            ),
        )
    }

    override fun getColor(i: Int, j: Int): HDRColor {
        return handleCallWithReturn("getColor", mapOf("i" to i, "j" to j))
    }
}
