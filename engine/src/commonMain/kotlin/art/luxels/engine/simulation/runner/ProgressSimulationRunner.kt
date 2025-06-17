package art.luxels.engine.simulation.runner

import art.luxels.core.log.LogHandler
import art.luxels.core.log.endProgress
import art.luxels.core.log.info
import art.luxels.core.log.startProgress
import art.luxels.core.math.Dimension
import art.luxels.core.render.Film
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * Runs the simulation for a single frame on a single thread, saving the progress image regularly.
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param workerProvider the provider for the workers for each thread
 * @param filmProvider the provider for the film for each thread
 */
class ProgressSimulationRunner(
    internal val logHandler: LogHandler,
    internal val workerProvider: WorkerProvider = DefaultWorkerProvider(logHandler),
    internal val filmProvider: FilmProvider = DefaultFilmProvider(),
) : SimulationRunner {

    // region SimulationRunner

    @Suppress("InjectDispatcher")
    override suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    ) {
        val fileName = getFilename(scene, sceneConfiguration, commonConfiguration)

        val layerFilm = filmProvider.createFilm(
            commonConfiguration.outputFilmType,
            commonConfiguration.outputResolution,
        )

        val frameSimStart = Clock.System.now()

        spawnAndRunWorker(
            scene,
            sceneConfiguration,
            commonConfiguration,
            layerFilm,
        ) {
            commonConfiguration.outputFixer.write(layerFilm, fileName)
        }

        val frameSimStop = Clock.System.now()
        val simDuration = frameSimStop - frameSimStart

        logHandler.info("                                                                                ")
        logHandler.info("✔ Frame #${commonConfiguration.animationFrameInfo.index} simulation complete in $simDuration")
        logHandler.info("  Saving frame -> $fileName")

        CoroutineScope(Dispatchers.IO).run {
            commonConfiguration.outputFixer.write(layerFilm, fileName)
        }

        val imageWritten = Clock.System.now()
        val imageProcessingDuration = imageWritten - frameSimStop
        logHandler.info("✔ Frame #${commonConfiguration.animationFrameInfo.index} written in $imageProcessingDuration")

        SystemInfo.clearMemory()
    }

    // endregion

    // region Internal

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> spawnAndRunWorker(
        scene: Scene<D, L, I, E>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
        layerFilm: Film,
        progressionCallback: (Double) -> Unit,
    ) {
        logHandler.startProgress()
        val context = sceneConfiguration.context ?: error("Missing context")

        val simulator = scene.initSimulator(commonConfiguration.animationFrameInfo)
        val worker = workerProvider.createWorker(simulator, commonConfiguration, progressionCallback)
        val exposure = ProjectionExposure(layerFilm, context.projection)

        val name = "worker"
        val workerJob = CoroutineScope(newSingleThreadContext(name)).launch {
            withContext(CoroutineName(name)) {
                worker.runSimulation(exposure, sceneConfiguration, commonConfiguration)
            }
        }

        // Wait for all workers
        workerJob.join()
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
