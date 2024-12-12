package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.endProgress
import fr.xgouchet.luxels.core.log.info
import fr.xgouchet.luxels.core.log.startProgress
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.asVolume
import fr.xgouchet.luxels.core.render.Film
import fr.xgouchet.luxels.core.render.LayeredFilm
import fr.xgouchet.luxels.core.system.SystemInfo
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.render.DefaultFilmProvider
import fr.xgouchet.luxels.engine.render.FilmProvider
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.render.ProjectionExposure
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration
import fr.xgouchet.luxels.engine.simulation.worker.DefaultWorkerProvider
import fr.xgouchet.luxels.engine.simulation.worker.WorkerProvider
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        configuration: InternalConfiguration<D, I>,
    ) {
        val fileName = getFilename(scene, configuration)
        val environment = scene.initEnvironment(
            configuration.simulationVolume,
            configuration.inputData,
            configuration.animationFrameInfo,
        )
        val projection = scene.getProjection(
            configuration.simulationVolume,
            configuration.outputResolution.asVector2().asVolume(),
            configuration.animationFrameInfo,
        )
        val layeredFilm = LayeredFilm(configuration.outputResolution)

        val frameStart = Clock.System.now()
        logHandler.startProgress()

        spawnAndRunWorkers(
            scene,
            environment,
            projection,
            configuration,
            layeredFilm,
        )

        logHandler.endProgress()
        val elapsed = Clock.System.now() - frameStart

        logHandler.info("✔ Frame #${configuration.animationFrameInfo.index} simulation complete in $elapsed")
        logHandler.info("Saving frame -> $fileName")
        configuration.outputFixer.write(layeredFilm, fileName)
        SystemInfo.clearMemory()
    }

    // endregion

    // region Internal

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> spawnAndRunWorkers(
        scene: Scene<D, L, I, E>,
        environment: E,
        projection: Projection<D>,
        configuration: InternalConfiguration<D, I>,
        layeredFilm: LayeredFilm,
    ) {
        val jobs = mutableMapOf<Job, Film>()
        val maxThreadsCount = threadCountComputer.getAvailableThreads(configuration)
        val workerConfiguration = configuration.copy(
            simulationLuxelCount = configuration.simulationLuxelCount / maxThreadsCount,
        )

        repeat(maxThreadsCount) { workerIdx ->
            val layer = filmProvider.createFilm(
                workerConfiguration.outputFilmType,
                workerConfiguration.outputResolution,
            )
            val simulator = scene.initSimulator()
            val worker = workerProvider.createWorker(simulator, workerConfiguration)
            val exposure = ProjectionExposure(layer, projection)

            val name = "worker-$workerIdx"
            val workerJob = CoroutineScope(newSingleThreadContext(name)).launch {
                withContext(CoroutineName(name)) {
                    worker.runSimulation(environment, exposure, workerConfiguration)
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

    private fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> getFilename(
        scene: Scene<D, L, I, E>,
        configuration: InternalConfiguration<D, I>,
    ): String {
        return buildString {
            append(scene.outputName())

            append("_")
            append(configuration.outputFilmType.name)

            append("_")
            append(configuration.inputData.id)

            if (configuration.animationDuration.inWholeNanoseconds > 0) {
                append("_")
                append(configuration.animationFrameInfo.name())
            }
        }
    }

    // endregion
}
