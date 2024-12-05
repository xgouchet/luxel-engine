package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.endProgress
import fr.xgouchet.luxels.core.log.info
import fr.xgouchet.luxels.core.log.startProgress
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.FrameInfo
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.exposure.LayeredFilm
import fr.xgouchet.luxels.core.simulation.worker.DefaultWorkerProvider
import fr.xgouchet.luxels.core.simulation.worker.WorkerProvider
import fr.xgouchet.luxels.core.system.SystemInfo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class ParallelFrameRunner(
    private val logHandler: LogHandler,
    private val workerProvider: WorkerProvider = DefaultWorkerProvider(),
) : FrameRunner {

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any> simulateFrame(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
        frameInfo: FrameInfo,
    ) {
        val fileName = getFilename(simulator, configuration, inputData, frameInfo)
        val layeredFilm = LayeredFilm(configuration.render.resolution)
        simulator.onFrameStart(configuration.simulation, frameInfo.frameTime, configuration.animation.duration)
        val frameStart = Clock.System.now()
        logHandler.startProgress()

        spawnAndRunWorkers(simulator, configuration, threadCount, frameInfo, layeredFilm)

        logHandler.endProgress()
        val elapsed = Clock.System.now() - frameStart
        simulator.onFrameEnd(frameInfo.frameTime, configuration.animation.duration)
        logHandler.info("✔ Frame $frameInfo simulation complete in $elapsed")
        logHandler.info("Saving frame -> $fileName")
        configuration.render.fixer.write(layeredFilm, fileName)
        SystemInfo.clearMemory()
    }

    // region Internal/Utils

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any> spawnAndRunWorkers(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        frameInfo: FrameInfo,
        layeredFilm: LayeredFilm,
    ) {
        val jobs = mutableMapOf<Job, Film>()
        val luxelsPerThread = configuration.simulation.quality.count / threadCount


        repeat(threadCount) { workerIdx ->
            val layer = configuration.render.createFilm()
            val worker = workerProvider.createWorker(
                simulator = simulator,
                film = layer,
                frameInfo = frameInfo,
                luxelCountPerThread = luxelsPerThread,
                configuration = configuration,
                logHandler = logHandler,
            )

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