package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.VectorRandomGenerator
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.simulation.Simulator

internal class DefaultWorkerProvider : WorkerProvider {
    override fun <D : Dimension, L : Luxel<D>, I : Any> createWorker(
        simulator: Simulator<D, L, I>,
        film: Film,
        frameInfo: FrameInfo,
        luxelCountPerThread: Long,
        configuration: Configuration<D, I>,
        logHandler: LogHandler,
    ): SimulationWorker {


        val projection = simulator.getProjection(
            configuration.simulation.volume,
            configuration.render.filmSpace,
            frameInfo.frameTime,
        )

        return when (configuration.simulation.passType) {
            PassType.RENDER ->
                RenderSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = configuration.simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logHandler = logHandler,
                )

            PassType.SPAWN ->
                SpawnSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = configuration.simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logHandler = logHandler,
                )

            PassType.PATH ->
                PathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = configuration.simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logHandler = logHandler,
                )

            PassType.DEATH ->
                DeathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = configuration.simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logHandler = logHandler,
                )

            PassType.ENV ->
                EnvSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = configuration.simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logHandler = logHandler,
                    rng = VectorRandomGenerator(configuration.dimension),
                )
        }
    }
}