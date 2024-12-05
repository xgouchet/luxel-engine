package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.simulation.SimulationRunner
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.test.stub.core.CallRecorder
import fr.xgouchet.luxels.core.test.stub.core.ResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

class StubSimulationRunner : SimulationRunner, Stub {

    override val callRecorder: CallRecorder = SimpleCallRecorder()
    override val responseHandler: ResponseHandler = SimpleResponseHandler()

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
    ) {
        return handleUnitCall(
            "runSimulationWithInput",
            mapOf(
                "simulator" to simulator,
                "configuration" to configuration,
                "threadCount" to threadCount,
                "inputData" to inputData,
            ),
        )
    }
}
