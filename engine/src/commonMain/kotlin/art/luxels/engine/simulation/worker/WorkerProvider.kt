package art.luxels.engine.simulation.worker

import art.luxels.core.math.Dimension
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.CommonConfiguration

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
