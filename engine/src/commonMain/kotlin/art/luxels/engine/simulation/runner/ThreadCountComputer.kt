package art.luxels.engine.simulation.runner

import art.luxels.engine.simulation.CommonConfiguration

/**
 * Computes the available number of threads to use for a simulation.
 */
interface ThreadCountComputer {

    /**
     * @param commonConfiguration the current configuration
     * @return the number of threads that can safely be used
     */
    fun getAvailableThreads(commonConfiguration: CommonConfiguration): Int
}
