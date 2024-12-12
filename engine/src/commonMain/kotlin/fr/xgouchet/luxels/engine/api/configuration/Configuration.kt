package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.input.InputSource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * The configuration for the simulation run.
 * @param D the dimension of the space luxels evolve in
 * @param I the type of data used as input
 * @param dimension the dimension instance to build the space
 * @param input the input configuration
 * @param simulation the simulation configuration
 * @param animation the animation configuration
 * @param render the render configuration
 */
@Suppress("CommentRegions")
data class Configuration<D : Dimension, I : Any>(
    internal val dimension: D,
    internal val input: Input<I>,
    internal val simulation: Simulation<D>,
    internal val animation: Animation,
    internal val render: Render,
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
     * @property volume the bounds of the simulation volume
     * @property quality the quality of the simulation (default: [Quality.DEBUG])
     * @property maxThreadCount the number of threads to use in parallel for each computation (default: 4)
     * @property simulationType the kind of rendering to perform based on the simulation (default: [SimulationType.RENDER])
     */
    data class Simulation<D : Dimension>(
        val volume: Volume<D>,
        val quality: Quality = Quality.DEBUG,
        val maxThreadCount: Int = 4,
        val simulationType: SimulationType = SimulationType.RENDER,
    )

    /**
     * The animation options for the simulation run.
     * @property duration the duration of the animation (default: 0, i.e.: a single still image)
     * @property fps the number of frames per second for the animation (default: 60 fps)
     */
    data class Animation(
        val duration: Duration = 0.seconds,
        val fps: Int = 24,
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
    )
}
