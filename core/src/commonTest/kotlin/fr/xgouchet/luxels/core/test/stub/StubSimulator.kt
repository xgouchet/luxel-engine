package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.LogHandler
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
        logHandler: LogHandler,
    ) {
        handleUnitCall(
            "initEnvironment",
            mapOf(
                "simulation" to simulation,
                "inputData" to inputData,
                "logHandler" to logHandler,
            ),
        )
    }

    override fun getProjection(
        simulationSpace: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        frameTime: Duration,
    ): Projection<D> {
        return handleCallWithReturn(
            "getProjection",
            mapOf(
                "simulationSpace" to simulationSpace,
                "filmSpace" to filmSpace,
                "frameTime" to frameTime,
            ),
        )
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<D>,
        time: Duration,
        animationDuration: Duration,
    ) {
        handleUnitCall(
            "onFrameStart",
            mapOf("simulation" to simulation, "time" to time, "animationDuration" to animationDuration),
        )
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
        handleUnitCall(
            "onFrameEnd",
            mapOf("time" to time, "animationDuration" to animationDuration),
        )
    }

    override fun outputName(): String {
        return handleCallWithReturn("outputName", mapOf()) { "TODO" }
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
