package art.luxels.engine.simulation.runner

import art.luxels.core.log.LogHandler
import art.luxels.core.log.endSection
import art.luxels.core.log.startSection
import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputData
import art.luxels.engine.simulation.CommonConfiguration
import art.luxels.engine.simulation.SceneConfiguration
import art.luxels.engine.simulation.SimulationContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * A simulation runner that runs simulation for all frames of a simulation.
 *
 * @param D the dimension of the space luxels evolve in
 *
 * @property dimension the [Dimension] in which the luxels evolve
 * @property logHandler the [LogHandler] to use throughout the simulation
 * @property simulationRunnerProvider a provider for the runner for the simulation of each frame
 */
class SceneAnimationRunner<D : Dimension>(
    val dimension: D,
    val logHandler: LogHandler,
    val simulationRunnerProvider: (CommonConfiguration) -> SimulationRunner,
) : SingleSimulationRunner<D> {

    constructor(
        dimension: D,
        logHandler: LogHandler,
    ) : this(
        dimension = dimension,
        logHandler = logHandler,
        simulationRunnerProvider = {
            if (it.simulationMaxThreadCount <= 1) {
                ProgressSimulationRunner(logHandler)
            } else {
                ParallelSimulationRunner(logHandler)
            }
        },
    )

    // region SingleSimulationRunner

    override suspend fun <L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        commonConfiguration: CommonConfiguration,
        inputData: InputData<I>,
        simulationVolume: Volume<D>,
    ) {
        RndGen.resetSeed(inputData.seed)

        val simulationRunner = simulationRunnerProvider.invoke(commonConfiguration)

        val environment = scene.getEnvironment(
            simulationVolume,
            inputData,
            commonConfiguration.animationDuration,
        )

        var frameInfo: FrameInfo? = FrameInfo(0, 0.milliseconds, 0.0)

        while (frameInfo != null) {
            logHandler.startSection("Frame $frameInfo")

            val projection = scene.getProjection(
                simulationVolume,
                commonConfiguration.outputResolution.asVector2().asVolume(),
                frameInfo,
            )
            val frameSceneConfig = SceneConfiguration(
                dimension,
                inputData,
                simulationVolume,
                SimulationContext(environment, projection),
            )
            val frameCommonConfig = commonConfiguration.copy(animationFrameInfo = frameInfo)
            simulationRunner.runSimulation(scene, frameSceneConfig, frameCommonConfig)
            frameInfo = increment(frameInfo, frameCommonConfig)

            logHandler.endSection()
        }
    }

    // endregion

    // region Internal

    internal fun increment(frameInfo: FrameInfo, configuration: CommonConfiguration): FrameInfo? {
        val nextTime = frameInfo.time + configuration.animationFrameStep

        return if (nextTime > configuration.animationDuration) {
            null
        } else {
            val progression = nextTime / configuration.animationDuration
            FrameInfo(
                frameInfo.index + 1,
                nextTime,
                progression,
            )
        }
    }

    // endregion
}
