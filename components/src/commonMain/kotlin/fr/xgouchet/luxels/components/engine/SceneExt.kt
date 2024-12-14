package fr.xgouchet.luxels.components.engine

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.configuration.Configuration
import fr.xgouchet.luxels.engine.simulation.LuxelEngine

/**
 * Runs a scene with the given configuration.
 *
 * @param D the dimension of the space Luxels evolve in
 * @param L the type of simulated Luxels
 * @param I the type of expected Inputs
 * @param E the type of the Environment
 *
 ** @param configuration the configuration detailing the input, simulation, animation and rendering options
 */
infix fun <D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> Scene<D, L, I, E>.runWith(
    configuration: Configuration<D, I>,
) {
    LuxelEngine().runSimulation(this, configuration)
}
