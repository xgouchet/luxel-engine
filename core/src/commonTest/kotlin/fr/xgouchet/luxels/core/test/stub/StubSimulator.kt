package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.test.stub.core.CallRecorder
import fr.xgouchet.luxels.core.test.stub.core.ResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.SimpleCallRecorder
import fr.xgouchet.luxels.core.test.stub.core.SimpleResponseHandler
import fr.xgouchet.luxels.core.test.stub.core.Stub
import fr.xgouchet.luxels.core.test.stub.core.handleCallWithReturn
import fr.xgouchet.luxels.core.test.stub.core.handleUnitCall
import kotlin.time.Duration

class StubSimulator<D : Dimension, I : Any>() : Simulator<D, StubLuxel<D>, I>, Stub {

    override val callRecorder: CallRecorder = SimpleCallRecorder()

    override val responseHandler: ResponseHandler = SimpleResponseHandler()

    override fun initEnvironment(
        simulation: Configuration.Simulation<D>,
        inputData: InputData<I>,
        logger: Logger,
    ) {
        TODO("Not yet implemented")
    }

    override fun getProjection(
        simulationSpace: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<D> {
        TODO("Not yet implemented")
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<D>,
        time: Duration,
        animationDuration: Duration,
    ) {
        TODO("Not yet implemented")
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
        TODO("Not yet implemented")
    }

    override fun outputName(): String {
        TODO("Not yet implemented")
    }

    override fun updateLuxel(luxel: StubLuxel<D>, time: Duration) {
        handleUnitCall("updateLuxel", mapOf("luxel" to luxel, "time" to time))
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<D>, time: Duration): StubLuxel<D> {
        return handleCallWithReturn(
            "spawnLuxel",
            mapOf(
                "simulation" to simulation,
                "time" to time,
            ),
        )
    }
}
