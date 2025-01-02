package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration

/**
 * A default [WorkerProvider].
 *
 * @param logHandler the [LogHandler] to use throughout the simulation
 */
class DefaultWorkerProvider(
    val logHandler: LogHandler,
) : WorkerProvider {

    // region WorkerProvider

    override fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> createWorker(
        simulator: Simulator<D, L, E>,
        configuration: InternalConfiguration<D, I, E>,
    ): SimulationWorker<D, E> {
        return when (configuration.simulationType) {
            SimulationType.RENDER -> RenderSimulationWorker(simulator, logHandler)
            SimulationType.SPAWN -> SpawnSimulationWorker(simulator, logHandler)
            SimulationType.PATH -> PathSimulationWorker(simulator, logHandler)
            SimulationType.DEATH -> DeathSimulationWorker(simulator, logHandler)
            SimulationType.ENV -> EnvSimulationWorker(simulator, logHandler)
        }
    }

    // endregion
}
