package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration

/**
 * Runs the simulation for a single frame.
 */
interface SimulationRunner {

    /**
     * Runs the simulation with the given parameters.
     *
     * @param D the dimension of the space luxels evolve in
     * @param L the type of simulated Luxels
     * @param I the expected Input
     * @param E the expected Environment
     *
     * @param scene the scene to run
     * @param configuration the simulation configuration
     */
    suspend fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> runSimulation(
        scene: Scene<D, L, I, E>,
        configuration: InternalConfiguration<D, I>,
    )
}
