package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.log.endSection
import fr.xgouchet.luxels.core.log.startSection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel

internal class MultiFrameSimulationRunner(
    val logHandler: LogHandler,
    val frameRunner: FrameRunner = ParallelFrameRunner(logHandler),
) : SimulationRunner {

    override suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
    ) {
        RndGen.resetSeed(inputData.seed)
        simulator.initEnvironment(configuration.simulation, inputData, logHandler)

        var frameInfo = configuration.animation.frameInfo

        while (frameInfo.frameTime <= configuration.animation.duration) {
            logHandler.startSection("FRAME $frameInfo")
            frameRunner.simulateFrame(simulator, configuration, threadCount, inputData, frameInfo)
            configuration.animation.increment()
            frameInfo = configuration.animation.frameInfo
            logHandler.endSection()
        }
    }
}