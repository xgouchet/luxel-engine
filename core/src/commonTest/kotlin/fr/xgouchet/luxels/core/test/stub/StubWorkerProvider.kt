package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.simulation.worker.SimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.WorkerProvider
import fr.xgouchet.luxels.core.test.stub.core.CallRecorder
import fr.xgouchet.luxels.core.test.stub.core.ResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleCallWithReturn

internal class StubWorkerProvider : WorkerProvider, Stub {
    override val callRecorder: CallRecorder = SimpleCallRecorder()
    override val responseHandler: ResponseHandler = SimpleResponseHandler()

    override fun <D : Dimension, L : Luxel<D>, I : Any> createWorker(
        simulator: Simulator<D, L, I>,
        film: Film,
        frameInfo: FrameInfo,
        luxelCountPerThread: Long,
        configuration: Configuration<D, I>,
        logHandler: LogHandler,
    ): SimulationWorker {
        return handleCallWithReturn(
            "createWorker",
            mapOf(
                "simulator" to simulator,
                "film" to film,
                "frameInfo" to frameInfo,
                "luxelCountPerThread" to luxelCountPerThread,
                "configuration" to configuration,
                "logHandler" to logHandler,
            ),
        )
    }
}
