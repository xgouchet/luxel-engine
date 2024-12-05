package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.simulation.FrameRunner
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall

internal class StubFrameRunner : FrameRunner, Stub {

    override val callRecorder = SimpleCallRecorder()

    override val responseHandler = SimpleResponseHandler()

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrame(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
        frameInfo: FrameInfo,
    ) {
        handleUnitCall(
            "simulateFrame",
            mapOf(
                "simulator" to simulator,
                "configuration" to configuration,
                "threadCount" to threadCount,
                "inputData" to inputData,
                "frameInfo" to frameInfo,
            ),
        )
    }
}
