package fr.xgouchet.luxels.engine.simulation

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

/**
 * Represents the common configuration used for the simulation.
 *
 * @property simulationLuxelCount the number of luxel to simulate
 * @property simulationMaxThreadCount the number of threads to use in parallel for each computation
 * @property simulationType the kind of rendering to perform based on the simulation
 * @property animationDuration the duration of the animation
 * @property animationFrameStep the time between two successive frames of the animation
 * @property animationFrameInfo the information for the current frame being rendered
 * @property outputFilmType the type of film to render on
 * @property outputResolution the resolution of film to render on
 * @property outputFixer the fixer used to write the film into an image file
 */
data class CommonConfiguration(
    val simulationLuxelCount: Long,
    val simulationMaxThreadCount: Int,
    val simulationType: SimulationType,
    val animationDuration: Duration,
    val animationFrameStep: Duration,
    val animationFrameInfo: FrameInfo,
    val outputFilmType: FilmType,
    val outputResolution: Resolution,
    val outputFixer: ImageFixer,
)
