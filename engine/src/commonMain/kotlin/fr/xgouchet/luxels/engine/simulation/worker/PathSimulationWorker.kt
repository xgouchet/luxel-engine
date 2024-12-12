package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

/**
 * A [SimulationWorker] showing the path followed by luxels during their lifespan.
 *
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the expected Environment
 *
 * @param simulator the simulator to use
 * @param logHandler the [LogHandler] to use throughout the simulation
 */
class PathSimulationWorker<D : Dimension, L : Luxel<D>, E : Environment<D>>(
    simulator: Simulator<D, L, E>,
    logHandler: LogHandler,
) : AbstractSimulationWorker<D, L, E>(
    simulationType = SimulationType.PATH,
    simulator = simulator,
    logHandler = logHandler,
) {

    // region AbstractSimulationWorker

    override fun simulateSingleLuxel(
        environment: E,
        exposure: Exposure<D>,
        luxelIndex: Long,
        frameInfo: FrameInfo,
    ) {
        val luxel = simulator.spawnLuxel(environment, frameInfo)
        luxel.onStart()

        var step = 0
        while (luxel.isAlive()) {
            luxel.onStep(step)
            exposure.expose(luxel.position(), PATH_COLOR)
            simulator.updateLuxel(luxel)
            step++
        }

        luxel.onEnd()
    }

    // endregion

    companion object {
        private val PATH_COLOR = HDRColor.BLUE
    }
}
