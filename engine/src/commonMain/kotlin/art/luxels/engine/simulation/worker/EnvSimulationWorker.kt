package art.luxels.engine.simulation.worker

import art.luxels.core.log.LogHandler
import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.core.math.random.VectorRandomGenerator
import art.luxels.core.math.random.inVolume
import art.luxels.core.render.Exposure
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.configuration.SimulationType
import art.luxels.engine.simulation.CommonConfiguration
import art.luxels.engine.simulation.SceneConfiguration
import art.luxels.engine.simulation.runner.FrameInfo

/**
 * A [SimulationWorker] showing information about the environment.
 *
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the expected Environment
 *
 * @param simulator the simulator to use
 * @param logHandler the [LogHandler] to use throughout the simulation
 * @param density the density of the env simulation pass
 */
class EnvSimulationWorker<D : Dimension, L : Luxel<D>, E : Environment<D>>(
    simulator: Simulator<D, L, E>,
    logHandler: LogHandler,
    val density: Int,
) : AbstractSimulationWorker<D, L, E>(
    simulationType = SimulationType.ENV,
    simulator = simulator,
    logHandler = logHandler,
) {

    @Suppress("LateinitUsage")
    private lateinit var rng: VectorRandomGenerator<D>

    @Suppress("LateinitUsage")
    private lateinit var simulationVolume: Volume<D>

    // region SimulationWorker

    override suspend fun <I : Any> runSimulation(
        exposure: Exposure<D>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    ) {
        rng = VectorRandomGenerator(sceneConfiguration.dimension)
        simulationVolume = sceneConfiguration.simulationVolume
        super.runSimulation(exposure, sceneConfiguration, commonConfiguration)
    }

    // endregion

    // region AbstractSimulationWorker

    override fun simulateSingleLuxel(
        environment: E,
        exposure: Exposure<D>,
        luxelIndex: Long,
        frameInfo: FrameInfo,
    ) {
        repeat(density) {
            val simulationPosition = rng.inVolume(simulationVolume.expanded(2.0))
            val color = environment.environmentColor(simulationPosition, frameInfo.time)
            exposure.expose(simulationPosition, color)
        }
    }

    // endregion
}
