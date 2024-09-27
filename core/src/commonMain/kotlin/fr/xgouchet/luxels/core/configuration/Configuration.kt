package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.FrameInfo
import fr.xgouchet.luxels.core.configuration.input.InputSource
import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.VectorRandomGenerator
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import fr.xgouchet.luxels.core.simulation.worker.DeathSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.EnvSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.PathSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.RenderSimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.SimulationWorker
import fr.xgouchet.luxels.core.simulation.worker.SpawnSimulationWorker
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * The configuration for the simulation run.
 * @param D the dimension of the space luxels evolve in
 * @param I the type of data used as input
 * @param dimension the dimension instance to build the space
 * @param input the input configuration
 * @param simulation the simulation configuration
 * @param render the render configuration
 * @param animation the animation configuration
 */
@Suppress("CommentRegions")
class Configuration<D : Dimension, I : Any> internal constructor(
    internal val dimension: D,
    internal val input: Input<I>,
    internal val simulation: Simulation<D>,
    internal val render: Render,
    internal val animation: Animation,
) {
    /**
     * The input options for the simulation run.
     * @param I the type of data used as input
     * @property source the source providing the inputs
     */
    data class Input<I : Any>(
        val source: InputSource<I>,
    )

    /**
     * The simulation options for the simulation run.
     * @param D the dimension of the space luxels evolve in
     * @property volume the bounds of the simulation space
     * @property quality the quality of the simulation (default: [Quality.DEBUG])
     * @property maxThreadCount the number of threads to use in parallel for each computation (default: 4)
     * @property passType the kind of rendering to perform based on the simulation (default: [PassType.RENDER])
     */
    data class Simulation<D : Dimension>(
        val volume: Volume<D>,
        val quality: Quality = Quality.DEBUG,
        val maxThreadCount: Int = 4,
        val passType: PassType = PassType.RENDER,
    )

    /**
     * The rendering options for the simulation run.
     * @property filmType the type of film to render on (default: [FilmType.ROUGH])
     * @property resolution the resolution of film to render on (default: [Resolution.XGA])
     * @property fixer the fixer used to write the film into an image file (default: [NoOpFixer])
     */
    data class Render(
        val filmType: FilmType = FilmType.ROUGH,
        val resolution: Resolution = Resolution.XGA,
        val fixer: ImageFixer = NoOpFixer(),
    ) {
        internal fun createFilm(): Film {
            return filmType.create(resolution)
        }

        /** The film space as a [Volume] instance. */
        val filmSpace: Volume<Dimension.D2> = Volume(Vector.nul(Dimension.D2), resolution.asVector2())
    }

    /**
     * The animation options for the simulation run.
     * @property duration the duration of the animation (default: 0, i.e.: a single still image)
     * @property fps the number of frames per second for the animation (default: 60 fps)
     */
    data class Animation(
        val duration: Duration = 0.seconds,
        val fps: Int = 60,
    ) {
        private val timeStep = 1.seconds / fps

        internal val frameCount = (duration / timeStep).roundToInt() + 1

        internal fun increment(frameInfo: FrameInfo): FrameInfo {
            return FrameInfo(
                frameInfo.frameIndex + 1,
                frameInfo.frameTime + timeStep,
            )
        }
    }

    @Suppress("LongParameterList")
    internal fun <L : Luxel<D>, I : Any> createWorker(
        simulator: Simulator<D, L, I>,
        film: Film,
        frameInfo: FrameInfo,
        luxelCountPerThread: Long,
        projection: Projection<D>,
        logger: Logger,
    ): SimulationWorker {
        return when (simulation.passType) {
            PassType.RENDER ->
                RenderSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logger = logger,
                )

            PassType.SPAWN ->
                SpawnSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logger = logger,
                )

            PassType.PATH ->
                PathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logger = logger,
                )

            PassType.DEATH ->
                DeathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logger = logger,
                )

            PassType.ENV ->
                EnvSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                    luxelCountPerThread = luxelCountPerThread,
                    logger = logger,
                    rng = VectorRandomGenerator(dimension),
                )
        }
    }
}
