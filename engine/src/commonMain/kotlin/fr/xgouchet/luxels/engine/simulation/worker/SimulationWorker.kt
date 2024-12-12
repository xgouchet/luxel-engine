package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Exposure
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration

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
     * @param environment the environment for the simulation
     * @param exposure the target exposure
     * @param configuration the configuration
     */
    suspend fun <I : Any> runSimulation(
        environment: E,
        exposure: Exposure<D>,
        configuration: InternalConfiguration<D, I>,
    )
}
