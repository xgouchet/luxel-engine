package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.VectorRandomGenerator
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.CommonConfiguration
import fr.xgouchet.luxels.engine.simulation.SceneConfiguration
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

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
