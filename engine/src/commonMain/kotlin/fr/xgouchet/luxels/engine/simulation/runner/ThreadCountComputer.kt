package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration

/**
 * Computes the available number of threads to use for a simulation.
 */
interface ThreadCountComputer {

    /**
     * @param D the dimension of the space luxels evolve in
     * @param I the type of data used as input
     * @param E the expected Environment
     *
     * @param configuration the current configuration
     * @return the number of threads that can safely be used
     */
    fun <D : Dimension, I : Any, E : Environment<D>> getAvailableThreads(
        configuration: InternalConfiguration<D, I, E>,
    ): Int
}
