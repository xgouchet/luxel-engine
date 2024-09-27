package fr.xgouchet.luxels.core.simulation.worker

/**
 * A simple worker running part of the simulation (to parallelize computation).
 */
internal interface SimulationWorker {
    /**
     * Perform some work.
     */
    suspend fun work()
}
