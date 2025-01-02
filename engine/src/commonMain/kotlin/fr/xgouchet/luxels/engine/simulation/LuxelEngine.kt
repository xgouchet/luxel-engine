package fr.xgouchet.luxels.engine.simulation

import fr.xgouchet.luxels.core.concurrency.mainDispatcher
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.configuration.Configuration
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.simulation.runner.SceneAnimationRunner
import fr.xgouchet.luxels.engine.simulation.runner.SimulationRunner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The main engine driving the Luxel simulation.
 *
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param simulationRunner the runner for the simulation
 */
class LuxelEngine internal constructor(
    internal val logHandler: LogHandler,
    internal val simulationRunner: SimulationRunner,
) {

    constructor(
        logHandler: LogHandler = StdOutLogHandler(),
    ) : this(logHandler, SceneAnimationRunner(logHandler))

    /**
     * Runs the simulation, using the provided simulator and configuration.
     * @param D the dimension of the space luxels evolve in
     * @param L the type of [Luxel] to simulate
     * @param I the type of data used as [InputData] for the simulation
     * @param E the type of the [Environment] luxels evolve in
     * @param scene the scene determining the environment and behavior of luxels
     * @param configuration the configuration detailing the input, simulation, animation and rendering options
     */
    fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        configuration: Configuration<D, I>,
    ) {
        val mainJob = CoroutineScope(mainDispatcher).launch {
            logHandler.startSection("Scene ${scene.outputName()}")
            configuration.input.source.forEach {
                val internalConfig = InternalConfiguration<D, I, E>(configuration, it)
                logHandler.startSection(
                    "Running scene “${scene.outputName()}” with input ${it.id} (${
                        it.seed.toString(
                            16,
                        )
                    })",
                )
                simulationRunner.runSimulation(scene, internalConfig)
                logHandler.endSection()
            }
            logHandler.endSection()
        }

        // Wait for completion
        runBlocking {
            mainJob.join()
        }
    }
}
