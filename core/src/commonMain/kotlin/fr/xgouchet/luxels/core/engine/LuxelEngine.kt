package fr.xgouchet.luxels.core.engine

import fr.xgouchet.luxels.core.concurrent.mainDispatcher
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.log.debug
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.info
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.simulation.MultiFrameSimulationRunner
import fr.xgouchet.luxels.core.simulation.SimulationRunner
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.system.SystemInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The main engine driving the Luxel simulation.
 *
 * @param logHandler the [fr.xgouchet.luxels.core.log.LogHandlerExt] to use throughout the simulation
 * @param simulationRunner the runner for the simulation
 */
class LuxelEngine(
    private val logHandler: LogHandler,
    private val simulationRunner: SimulationRunner,
) {

    constructor(
        logHandler: LogHandler = StdOutLogHandler(),
    ) : this(logHandler, MultiFrameSimulationRunner(logHandler))

    /**
     * Runs the simulation, using the provided simulator and configuration.
     * @param D the dimension of the space luxels evolve in
     * @param L the type of [fr.xgouchet.luxels.core.model.Luxel] to simulate
     * @param I the type of data used as [fr.xgouchet.luxels.core.configuration.input.InputData] for the simulation
     * @param simulator the simulator determining the behavior of luxels
     * @param configuration the configuration detailing the input, simulation, animation and rendering options
     */
    fun <D : Dimension, L : Luxel<D>, I : Any> runSimulation(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
    ) {
        val threadCount = computeThreadCount(configuration)

        val mainJob = CoroutineScope(mainDispatcher).launch {
            configuration.input.source.forEach {
                logHandler.startSection("Running simulation with input ${it.id}")
                simulationRunner.runSimulationWithInput(simulator, configuration, threadCount, it)
                logHandler.endSection()
            }
        }

        // Wait for completion
        runBlocking {
            mainJob.join()
        }
    }

    // region Internal

    private fun <D : Dimension, I : Any> computeThreadCount(configuration: Configuration<D, I>): Int {
        logHandler.startSection("Simulation Preparation")
        val parallelThreads = SystemInfo.getParallelCapacity()
        logHandler.debug("System allows $parallelThreads parallel cores")
        val availableMemory = SystemInfo.getAvailableMemory()
        val layerSize = configuration.render.resolution.pixelCount * Double.SIZE_BYTES * 8
        val layerCapacity = (availableMemory / layerSize).toInt()
        logHandler.debug("Memory allows $layerCapacity parallel layers")
        logHandler.debug("User allows ${configuration.simulation.maxThreadCount} threads")
        val threadCount = minOf(
            configuration.simulation.maxThreadCount,
            parallelThreads,
            layerCapacity - 1,
        )
        logHandler.info("Running simulation on $threadCount threads")
        logHandler.endSection()
        return threadCount
    }

    // endregion
}
