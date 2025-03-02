package art.luxels.engine.simulation.runner

import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputData
import art.luxels.engine.simulation.CommonConfiguration

/**
 * Runs the simulation for a given Scene with a single input data.
 *
 * @param D the dimension of the space luxels evolve in
 */
interface SingleSimulationRunner<D : Dimension> {

    /**
     * Runs the simulation with the given parameters.
     *
     * @param L the type of simulated Luxels
     * @param I the expected Input
     * @param E the expected Environment
     *
     * @param scene the scene to run
     * @param commonConfiguration the simulation common configuration
     * @param inputData the input for this simulation
     * @param simulationVolume the volume in which the simulation is ran
     */
    suspend fun <L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        commonConfiguration: CommonConfiguration,
        inputData: InputData<I>,
        simulationVolume: Volume<D>,
    )
}
