package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel

/**
 * The piece of logic that will run the simulation.
 */
interface SimulationRunner {

    /**
     * Runs the simulation with the given parameters.
     *
     * @param D the dimension of the space luxels evolve in
     * @param L the type of simulated Luxels
     * @param I the expected Input
     * @param simulator the simulator driving the luxels
     * @param configuration the simulation configuration
     * @param threadCount the number of threads to use
     * @param inputData the simulation input
     */
    suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
    )
}
