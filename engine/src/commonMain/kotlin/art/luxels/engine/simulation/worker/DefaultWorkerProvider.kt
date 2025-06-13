package art.luxels.engine.simulation.worker

import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.configuration.SimulationType
import art.luxels.engine.simulation.CommonConfiguration

/**
 * A default [WorkerProvider].
 *
 * @param logHandler the [LogHandler] to use throughout the simulation
 */
class DefaultWorkerProvider(val logHandler: LogHandler) : WorkerProvider {

    // region WorkerProvider

    override fun <D : Dimension, L : Luxel<D>, E : Environment<D>> createWorker(
        simulator: Simulator<D, L, E>,
        commonConfiguration: CommonConfiguration,
        progressionCallback: (Double) -> Unit,
    ): SimulationWorker<D, E> {
        return when (commonConfiguration.simulationType) {
            SimulationType.RENDER -> RenderSimulationWorker(simulator, logHandler, progressionCallback)
            SimulationType.SPAWN -> SpawnSimulationWorker(simulator, logHandler, progressionCallback)
            SimulationType.PATH -> PathSimulationWorker(simulator, logHandler, progressionCallback)
            SimulationType.DEATH -> DeathSimulationWorker(simulator, logHandler, progressionCallback)
            SimulationType.ENV -> EnvSimulationWorker(simulator, logHandler, 4096, progressionCallback)
        }
    }

    // endregion
}
