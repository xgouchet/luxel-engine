package fr.xgouchet.luxels.engine.simulation.worker

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.CommonConfiguration

/**
 * A provider to create the [SimulationWorker] to use during a simulation.
 */
interface WorkerProvider {

    /**
     * @param D the dimension of the space luxels evolve in
     * @param L the type of simulated Luxels
     * @param E the expected Environment
     *
     * @param simulator the simulator to use
     * @param commonConfiguration the simulation configuration
     */
    fun <D : Dimension, L : Luxel<D>, E : Environment<D>> createWorker(
        simulator: Simulator<D, L, E>,
        commonConfiguration: CommonConfiguration,
    ): SimulationWorker<D, E>
}
