package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.test.stub.core.CallRecorder
import fr.xgouchet.luxels.core.test.stub.core.ResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleCallWithReturn

class StubProjection<D : Dimension> : Projection<D>, Stub {

    override val callRecorder: CallRecorder = SimpleCallRecorder()
    override val responseHandler: ResponseHandler = SimpleResponseHandler()

    override val simulationSpace: Volume<D>
        get() = TODO("Not yet implemented")
    override val filmSpace: Volume<Dimension.D2>
        get() = TODO("Not yet implemented")

    override fun convertPosition(position: Vector<D>): Vector<Dimension.D2> {
        return handleCallWithReturn("convertPosition", mapOf("position" to position))
    }
}
