package art.luxels.engine.simulation.worker

import art.luxels.core.math.Dimension
import art.luxels.core.render.Exposure
import art.luxels.engine.api.Environment
import art.luxels.engine.simulation.CommonConfiguration
import art.luxels.engine.simulation.SceneConfiguration

/**
 * A simple worker running part of the simulation (to parallelize computation).
 *
 * @param D the dimension of the space luxels evolve in
 * @param E the expected Environment
 */
interface SimulationWorker<D : Dimension, E : Environment<D>> {

    /**
     * Perform some work.
     * @param I the expected Input
     * @param exposure the target exposure
     * @param sceneConfiguration the simulation scene configuration
     * @param commonConfiguration the simulation common configuration
     */
    suspend fun <I : Any> runSimulation(
        exposure: Exposure<D>,
        sceneConfiguration: SceneConfiguration<D, I, E>,
        commonConfiguration: CommonConfiguration,
    )
}
