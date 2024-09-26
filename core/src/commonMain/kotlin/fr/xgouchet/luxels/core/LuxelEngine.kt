package fr.xgouchet.luxels.core

import fr.xgouchet.luxels.core.concurrent.mainDispatcher
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.exposure.LayeredFilm
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.system.SystemInfo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.ContinuationInterceptor
import kotlin.math.log
import kotlin.time.Duration.Companion.seconds

/**
 * The main engine driving the Luxel simulation.
 */
object LuxelEngine {

    private val logger: Logger = Logger(StdOutLogHandler())

    /**
     * Runs the simulation, using the provided simulator and configuration.
     * @param D the dimension of the space luxels evolve in
     * @param L the type of [Luxel] to simulate
     * @param I the type of data used as [InputData] for the simulation
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
                logger.startSection("Running simulation with input ${it.id}")
                runSimulationWithInput(simulator, configuration, threadCount, it)
                logger.endSection()
            }
        }

        // Wait for completion
        runBlocking {
            mainJob.join()
        }
    }

    // region Internal

    private fun <D : Dimension, I : Any> computeThreadCount(configuration: Configuration<D, I>): Int {
        logger.startSection("Simulation Preparation")
        val parallelThreads = SystemInfo.getParallelCapacity()
        logger.debug("System allows $parallelThreads parallel cores")
        val availableMemory = SystemInfo.getAvailableMemory()
        val layerSize = configuration.render.resolution.pixelCount * Double.SIZE_BYTES * 8
        val layerCapacity = (availableMemory / layerSize).toInt()
        logger.debug("Memory allows $layerCapacity parallel layers")
        logger.debug("User allows ${configuration.simulation.maxThreadCount} threads")
        val threadCount = minOf(
            configuration.simulation.maxThreadCount,
            parallelThreads,
            layerCapacity - 1
        )
        logger.info("Running simulation on $threadCount threads")
        logger.endSection()
        return threadCount
    }

    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
    ) {
        RndGen.resetSeed(inputData.seed)
        simulator.initEnvironment(configuration.simulation, inputData, logger)

        var frameInfo = FrameInfo(0, 0.seconds)

        while (frameInfo.frameTime <= configuration.animation.duration) {
            logger.startSection("FRAME $frameInfo")
            simulateFrame(simulator, configuration, threadCount, inputData, frameInfo)
            frameInfo = configuration.animation.increment(frameInfo)
            logger.endSection()
        }
    }

    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrame(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<*>,
        frameInfo: FrameInfo,
    ) {
        val fileName = getFilename(simulator, configuration, inputData, frameInfo)
        val layeredFilm = LayeredFilm(configuration.render.resolution)

        simulateFrameParallel(simulator, configuration, threadCount, frameInfo, layeredFilm)

        logger.info("Saving frame -> $fileName")
        configuration.render.fixer.write(layeredFilm, fileName)
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrameParallel(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        frameInfo: FrameInfo,
        layeredFilm: LayeredFilm,
    ) {
        simulator.onFrameStart(configuration.simulation, frameInfo.frameTime, configuration.animation.duration)

        val jobs = mutableMapOf<Job, Film>()
        val frameStart = Clock.System.now()
        val luxelsPerThread = configuration.simulation.quality.count / threadCount

        val projection = simulator.getProjection(
            configuration.simulation.volume,
            configuration.render.filmSpace,
            frameInfo.frameTime
        )

        // Start all workers
        logger.startProgress()
        repeat(threadCount) { workerIdx ->
            val layer = configuration.render.createFilm()
            val worker = configuration.createWorker(simulator, layer, frameInfo, luxelsPerThread, projection, logger)

            val name = "worker-$workerIdx"
            val workerJob = CoroutineScope(newSingleThreadContext(name)).launch {
                withContext(CoroutineName(name)) {
                    worker.work()
                }
            }
            jobs[workerJob] = layer
        }

        // Wait for all workers
        for ((job, layer) in jobs) {
            job.join()
            layeredFilm.mergeLayer(layer)
        }
        logger.endProgress()

        val elapsed = Clock.System.now() - frameStart
        simulator.onFrameEnd(frameInfo.frameTime, configuration.animation.duration)
        logger.info("✔ Frame $frameInfo simulation complete in $elapsed")
        SystemInfo.gc()
    }

    // endregion

    // region Internal/Utils

    private fun chooseThreadCount() {

    }

    private fun getFilename(
        simulator: Simulator<*, *, *>,
        configuration: Configuration<*, *>,
        inputData: InputData<*>,
        frameInfo: FrameInfo,
    ): String {
        return buildString {
            append(simulator.outputName())

            append("_")
            append(configuration.render.filmType.name)

            append("_")
            append(inputData.id)

            if (configuration.animation.frameCount > 1) {
                append("_")
                append(frameInfo.frameName())
            }
        }
    }

    // endregion
}
