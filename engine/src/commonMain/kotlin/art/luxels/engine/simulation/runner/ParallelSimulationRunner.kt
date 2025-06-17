package art.luxels.engine.simulation.runner

import art.luxels.core.log.LogHandler
import art.luxels.core.log.endProgress
import art.luxels.core.log.info
import art.luxels.core.log.startProgress
import art.luxels.core.math.Dimension
import art.luxels.core.render.Film
import art.luxels.core.render.LayeredFilm
import art.luxels.core.system.SystemInfo
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.render.DefaultFilmProvider
import art.luxels.engine.render.FilmProvider
import art.luxels.engine.render.ProjectionExposure
import art.luxels.engine.simulation.CommonConfiguration
import art.luxels.engine.simulation.SceneConfiguration
import art.luxels.engine.simulation.worker.DefaultWorkerProvider
import art.luxels.engine.simulation.worker.WorkerProvider
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * Runs the simulation for a single frame on multiple parallel threads.
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param workerProvider the provider for the workers for each thread
 * @param filmProvider the provider for the film for each thread
 * @param threadCountComputer the utility to know how many threads to use
 */
class ParallelSimulationRunner(
    internal val logHandler: LogHandler,
    internal val workerProvider: WorkerProvider = DefaultWorkerProvider(logHandler),
    internal val filmProvider: FilmProvider = DefaultFilmProvider(),
    internal val threadCountComputer: ThreadCountComputer = DefaultThreadCountComputer(),
) : SimulationRunner {

    // region SimulationRunner

    @Suppress("InjectDispatcher")
    override suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    ) {
        val fileName = getFilename(scene, sceneConfiguration, commonConfiguration)

        val layeredFilm = LayeredFilm(commonConfiguration.outputResolution)

        val frameSimStart = Clock.System.now()

        spawnAndRunWorkers(
            scene,
            sceneConfiguration,
            commonConfiguration,
            layeredFilm,
        )

        val frameSimStop = Clock.System.now()
        val simDuration = frameSimStop - frameSimStart

        logHandler.info("                                                                                ")
        logHandler.info("✔ Frame #${commonConfiguration.animationFrameInfo.index} simulation complete in $simDuration")
        logHandler.info("  Saving frame -> $fileName")

        CoroutineScope(Dispatchers.IO).run {
            commonConfiguration.outputFixer.write(layeredFilm, fileName)
        }

        val imageWritten = Clock.System.now()
        val imageProcessingDuration = imageWritten - frameSimStop
        logHandler.info("✔ Frame #${commonConfiguration.animationFrameInfo.index} written in $imageProcessingDuration")

        SystemInfo.clearMemory()
    }

    // endregion

    // region Internal

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> spawnAndRunWorkers(
        scene: Scene<D, L, I, E>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
        layeredFilm: LayeredFilm,
    ) {
        val jobs = mutableMapOf<Job, Film>()
        val maxThreadsCount = threadCountComputer.getAvailableThreads(commonConfiguration)
        val workerConfiguration = commonConfiguration.copy(
            simulationLuxelCount = commonConfiguration.simulationLuxelCount / maxThreadsCount,
        )

        logHandler.info("Using $maxThreadsCount threads")
        logHandler.startProgress()
        val context = sceneConfiguration.context ?: error("Missing context")

        repeat(maxThreadsCount) { workerIdx ->
            val layer = filmProvider.createFilm(
                workerConfiguration.outputFilmType,
                workerConfiguration.outputResolution,
            )
            val simulator = scene.initSimulator(commonConfiguration.animationFrameInfo)
            val worker = workerProvider.createWorker(simulator, workerConfiguration)
            val exposure = ProjectionExposure(layer, context.projection)

            val name = "worker-$workerIdx"
            val workerJob = CoroutineScope(newSingleThreadContext(name)).launch {
                withContext(CoroutineName(name)) {
                    worker.runSimulation(exposure, sceneConfiguration, workerConfiguration)
                }
            }
            jobs[workerJob] = layer
        }

        // Wait for all workers
        for ((job, layer) in jobs) {
            job.join()
            layeredFilm.mergeLayer(layer)
        }
        logHandler.endProgress()
    }

    private fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> getFilename(
        scene: Scene<D, L, I, E>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    ): String {
        return buildString {
            append(scene.outputName())

            append("_")
            append(commonConfiguration.simulationType.name)

            append("_")
            append(commonConfiguration.outputFilmType.name)

            append("_")
            append(sceneConfiguration.inputData.id)

            if (commonConfiguration.animationDuration.inWholeNanoseconds > 0) {
                append("_")
                append(commonConfiguration.animationFrameInfo.name())
            }
        }
    }

    // endregion
}
