package art.luxels.engine.simulation

import art.luxels.core.math.Dimension
import art.luxels.engine.api.Environment
import art.luxels.engine.render.Projection

/**
 * Represents the context of the simulation shared by all threads.
 *
 * @param D the dimension of the space luxels evolve in
 * @param E the type of the Environment
 * @property environment the environment
 * @property projection the projection
 */
data class SimulationContext<D : Dimension, E : Environment<D>>(
    val environment: E,
    val projection: Projection<D>,
)
