package fr.xgouchet.luxels.core

import fr.xgouchet.luxels.core.concurrent.mainDispatcher
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.exposure.LayeredFilm
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

// TODO use SLF4J or other lib to log instead of println

/**
 * The main engine driving the Luxel simulation.
 */
object LuxelEngine {
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
        val mainJob = CoroutineScope(mainDispatcher).launch {
            configuration.input.source.forEach {
                runSimulationWithInput(simulator, configuration, it)
            }
        }

        // Wait for completion
        runBlocking {
            mainJob.join()
        }
    }

    // region Internal

    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        inputData: InputData<I>,
    ) {
        RndGen.resetSeed(inputData.seed)
        simulator.initEnvironment(configuration.simulation, inputData)

        var frameInfo = FrameInfo(0, 0.seconds)

        while (frameInfo.frameTime <= configuration.animation.duration) {
            println("--- FRAME $frameInfo")
            simulateFrame(simulator, configuration, inputData, frameInfo)
            frameInfo = configuration.animation.increment(frameInfo)
        }
    }

    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrame(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        inputData: InputData<*>,
        frameInfo: FrameInfo,
    ) {
        val fileName = getFilename(simulator, configuration, inputData, frameInfo)
        val layeredFilm = LayeredFilm(configuration.render.resolution)

        simulateFrameParallel(simulator, configuration, frameInfo, layeredFilm)

        println("  Saving frame -> $fileName")
        configuration.render.fixer.write(layeredFilm, fileName)
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrameParallel(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        frameInfo: FrameInfo,
        layeredFilm: LayeredFilm,
    ) {
        simulator.onFrameStart(configuration.simulation, frameInfo.frameTime, configuration.animation.duration)
        val threadCount = configuration.simulation.threadCount

        val jobs = mutableMapOf<Job, Film>()
        val frameStart = Clock.System.now()

        // Start all workers
        repeat(threadCount) {
            val layer = configuration.render.createFilm()
            val worker = configuration.createWorker(simulator, layer, frameInfo)
            val workerJob = CoroutineScope(newSingleThreadContext("worker-$it")).launch {
                worker.work()
            }
            jobs[workerJob] = layer
        }

        // Wait for all workers
        for ((job, layer) in jobs) {
            job.join()
            layeredFilm.mergeLayer(layer)
        }

        val elapsed = Clock.System.now() - frameStart
        simulator.onFrameEnd(frameInfo.frameTime, configuration.animation.duration)
        println("\r    ✔ Frame $frameInfo simulation complete in $elapsed")
    }

    // endregion

    // region Internal/Utils

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
