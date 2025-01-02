package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.asVolume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration
import fr.xgouchet.luxels.engine.simulation.SimulationContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * A simulation runner that runs simulation for all frames of a simulation.
 *
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param simulationRunner the runner for the simulation of each frame
 */
class SceneAnimationRunner(
    val logHandler: LogHandler,
    val simulationRunner: SimulationRunner = ParallelSimulationRunner(logHandler),
) : SimulationRunner {

    // region SimulationRunner

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        configuration: InternalConfiguration<D, I, E>,
    ) {
        RndGen.resetSeed(configuration.inputData.seed)

        val environment = scene.getEnvironment(
            configuration.simulationVolume,
            configuration.animationDuration,
            configuration.inputData,
        )

        var frameInfo: FrameInfo? = FrameInfo(0, 0.milliseconds, 0.0)

        while (frameInfo != null) {
            logHandler.startSection("Frame $frameInfo")

            val projection = scene.getProjection(
                configuration.simulationVolume,
                configuration.outputResolution.asVector2().asVolume(),
                frameInfo,
            )
            val frameConfig = configuration.copy(
                animationFrameInfo = frameInfo,
                context = SimulationContext(environment, projection),
            )
            simulationRunner.runSimulation(scene, frameConfig)
            frameInfo = increment(frameInfo, configuration)

            logHandler.endSection()
        }
    }

    // endregion

    // region Internal

    internal fun <D : Dimension, I : Any, E : Environment<D>> increment(
        frameInfo: FrameInfo,
        configuration: InternalConfiguration<D, I, E>,
    ): FrameInfo? {
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
