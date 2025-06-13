package art.luxels.engine.simulation.worker

import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.core.render.Exposure
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.configuration.SimulationType
import art.luxels.engine.simulation.runner.FrameInfo
import art.luxels.imageio.color.HDRColor

/**
 * A [SimulationWorker] showing where luxels start their lifespan.
 *
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the expected Environment
 *
 * @param simulator the simulator to use
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param progressionCallback a callback to handle progression events
 */
class SpawnSimulationWorker<D : Dimension, L : Luxel<D>, E : Environment<D>>(
    simulator: Simulator<D, L, E>,
    logHandler: LogHandler,
    progressionCallback: (Double) -> Unit = {},
) : AbstractSimulationWorker<D, L, E>(
    simulationType = SimulationType.SPAWN,
    simulator = simulator,
    logHandler = logHandler,
    progressionCallback = progressionCallback,
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
        exposure.expose(luxel.position(), SPAWN_COLOR)
    }

    // endregion

    companion object {
        private val SPAWN_COLOR = HDRColor.GREEN
    }
}
