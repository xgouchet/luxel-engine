package fr.xgouchet.luxels.engine.simulation

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.info
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.api.input.InputSource
import fr.xgouchet.luxels.engine.simulation.runner.SceneAnimationRunner
import fr.xgouchet.luxels.engine.simulation.runner.SingleSimulationRunner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The main engine driving the Luxel simulation.
 *
 * @param D the dimension of the space luxels evolve in
 *
 * @param dimension the [Dimension] in which the luxels evolve
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param simulationRunner the runner for the simulation
 */
class LuxelEngine<D : Dimension> internal constructor(
    internal val dimension: D,
    internal val logHandler: LogHandler,
    internal val simulationRunner: SingleSimulationRunner<D>,
) {

    constructor(
        dimension: D,
        logHandler: LogHandler = StdOutLogHandler(),
    ) : this(dimension, logHandler, SceneAnimationRunner<D>(dimension, logHandler))

    /**
     * Runs the simulation, using the provided simulator and configuration.
     * @param L the type of [Luxel] to simulate
     * @param I the type of data used as [InputData] for the simulation
     * @param E the type of the [Environment] luxels evolve in
     *
     * @param scene the scene determining the environment and behavior of luxels
     * @param commonConfiguration the configuration detailing the simulation, animation and rendering options
     * @param inputSource the source data to use as input
     * @param simulationVolume the volume in which the simulation is ran
     */
    @Suppress("InjectDispatcher")
    fun <L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        commonConfiguration: CommonConfiguration,
        inputSource: InputSource<I>,
        simulationVolume: Volume<D>,
    ) {
        logHandler.info("Simulating scene ${scene.outputName()}")
        val mainJob = CoroutineScope(Dispatchers.Default).launch {
            logHandler.startSection("Scene ${scene.outputName()}")
            inputSource.forEach { inputData ->
                logHandler.startSection(
                    "Running scene “${scene.outputName()}” with input ${inputData.id} (${inputData.seed.toString(16)})",
                )
                simulationRunner.runSimulation(
                    scene,
                    commonConfiguration,
                    inputData,
                    simulationVolume,
                )
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
