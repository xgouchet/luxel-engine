package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.FrameInfo
import fr.xgouchet.luxels.core.configuration.input.InputSource
import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.geometry.Space2
import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.Film
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
 */
@Suppress("CommentRegions")
class Configuration<I : Any> internal constructor(
    internal val input: Input<I>,
    internal val simulation: Simulation,
    internal val render: Render,
    internal val animation: Animation,
) {
    /**
     * The input options for the simulation run.
     * @param D the type of data used as input
     * @property source the source providing the inputs
     */
    data class Input<D : Any>(
        val source: InputSource<D>,
    )

    /**
     * The simulation options for the simulation run.
     * @property quality the quality of the simulation (default: [Quality.DEBUG])
     * @property space the bounds of the simulation 3D space (default [Space3.UNIT])
     * @property threadCount the number of threads to use in parallel for each computation (default: 4)
     * @property passType the kind of rendering to perform based on the simulation (default: [PassType.RENDER])
     */
    data class Simulation(
        val quality: Quality = Quality.DEBUG,
        val space: Space3 = Space3.UNIT,
        val threadCount: Int = 4,
        val passType: PassType = PassType.RENDER,
    ) {
        /** The number of luxels computed on each thread. */
        val luxelPerThread = (quality.count / threadCount)
    }

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

        /** The film space as a [Space2] instance. */
        val filmSpace: Space2 = resolution.asSpace2()
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

    internal fun <L : Luxel, I : Any> createWorker(
        simulator: Simulator<L, I>,
        film: Film,
        frameInfo: FrameInfo,
    ): SimulationWorker {
        val projection = simulator.getProjection(simulation.space, render.filmSpace, frameInfo.frameTime)

        return when (simulation.passType) {
            PassType.RENDER ->
                RenderSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                )

            PassType.SPAWN ->
                SpawnSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                )

            PassType.PATH ->
                PathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                )

            PassType.DEATH ->
                DeathSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                )

            PassType.ENV ->
                EnvSimulationWorker(
                    film = film,
                    simulator = simulator,
                    simulation = simulation,
                    projection = projection,
                    time = frameInfo.frameTime,
                )
        }
    }
}
