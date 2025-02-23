package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.simulation.CommonConfiguration
import fr.xgouchet.luxels.engine.simulation.SceneConfiguration
import fr.xgouchet.luxels.engine.simulation.SimulationContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * A simulation runner that runs simulation for all frames of a simulation.
 *
 * @param D the dimension of the space luxels evolve in
 *
 * @property dimension the [Dimension] in which the luxels evolve
 * @property logHandler the [LogHandler] to use throughout the simulation
 * @property simulationRunner the runner for the simulation of each frame
 */
class SceneAnimationRunner<D : Dimension>(
    val dimension: D,
    val logHandler: LogHandler,
    val simulationRunner: SimulationRunner = ParallelSimulationRunner(logHandler),
) : SingleSimulationRunner<D> {

    // region SingleSimulationRunner

    override suspend fun <L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        commonConfiguration: CommonConfiguration,
        inputData: InputData<I>,
        simulationVolume: Volume<D>,
    ) {
        RndGen.resetSeed(inputData.seed)

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
