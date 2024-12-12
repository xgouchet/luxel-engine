package fr.xgouchet.luxels.engine.simulation

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Configuration
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Represents the internal configuration used for the simulation.
 * It mostly is derived from the user provided [Configuration] object.
 *
 * @param D the dimension of the space luxels evolve in
 * @param I the type of data used as input
 *
 * @param dimension the dimension instance to build the space
 * @param inputData the input data
 * @param simulationVolume the bounds of the simulation volume
 * @param simulationLuxelCount the number of luxel to simulate
 * @param simulationMaxThreadCount the number of threads to use in parallel for each computation
 * @param simulationType the kind of rendering to perform based on the simulation
 * @property animationDuration the duration of the animation
 * @property animationFrameStep the time between two successive frames of the animation
 * @property animationFrameInfo the information for the current frame being rendered
 * @property outputFilmType the type of film to render on
 * @property outputResolution the resolution of film to render on
 * @property outputFixer the fixer used to write the film into an image file
 */
data class InternalConfiguration<D : Dimension, I : Any>(
    val dimension: D,
    val inputData: InputData<I>,
    val simulationVolume: Volume<D>,
    val simulationLuxelCount: Long,
    val simulationMaxThreadCount: Int,
    val simulationType: SimulationType,
    val animationDuration: Duration,
    val animationFrameStep: Duration,
    val animationFrameInfo: FrameInfo,
    val outputFilmType: FilmType,
    val outputResolution: Resolution,
    val outputFixer: ImageFixer,
) {
    /**
     * Build an internal configuration from the user provided configuration.
     * @param configuration the user provided configuration
     * @param input the current input to use
     */
    constructor(
        configuration: Configuration<D, I>,
        input: InputData<I>,
    ) : this(
        dimension = configuration.dimension,
        inputData = input,
        simulationVolume = configuration.simulation.volume,
        simulationLuxelCount = configuration.simulation.quality.count,
        simulationMaxThreadCount = configuration.simulation.maxThreadCount,
        simulationType = configuration.simulation.simulationType,
        animationDuration = configuration.animation.duration,
        animationFrameStep = 1.seconds / configuration.animation.fps,
        animationFrameInfo = FrameInfo(0, 0.nanoseconds),
        outputFilmType = configuration.render.filmType,
        outputResolution = configuration.render.resolution,
        outputFixer = configuration.render.fixer,
    )
}
