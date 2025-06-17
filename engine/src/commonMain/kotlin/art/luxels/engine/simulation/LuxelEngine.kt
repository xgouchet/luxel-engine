package art.luxels.engine.simulation

import art.luxels.core.log.LogHandler
import art.luxels.core.log.StdOutLogHandler
import art.luxels.core.log.endSection
import art.luxels.core.log.info
import art.luxels.core.log.startSection
import art.luxels.core.log.warning
import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputData
import art.luxels.engine.api.input.InputSource
import art.luxels.engine.simulation.runner.SceneAnimationRunner
import art.luxels.engine.simulation.runner.SingleSimulationRunner
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
        val sceneName = scene.outputName()
        if (inputSource.isEmpty()) {
            logHandler.warning("Can't simulate scene $sceneName: input source is empty")
        }

        logHandler.info("Simulating scene $sceneName")
        val mainJob = CoroutineScope(Dispatchers.Default).launch {
            logHandler.startSection("Scene $sceneName")
            inputSource.forEach { inputData ->
                logHandler.startSection(
                    "Running scene “$sceneName” with input ${inputData.id} (seed:0x${inputData.seed.toString(16)})",
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
