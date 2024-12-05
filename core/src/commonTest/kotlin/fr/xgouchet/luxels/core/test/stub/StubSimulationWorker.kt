package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.simulation.worker.SimulationWorker
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

class StubSimulationWorker : SimulationWorker, Stub {

    override val callRecorder = SimpleCallRecorder()

    override val responseHandler = SimpleResponseHandler()

    override suspend fun work() {
        handleUnitCall("work")
    }
}