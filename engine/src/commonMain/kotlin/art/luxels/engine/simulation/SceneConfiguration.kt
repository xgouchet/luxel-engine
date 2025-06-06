package art.luxels.engine.simulation

import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.engine.api.input.InputData

/**
 * Represents the scene configuration used for the simulation.
 *
 * @param D the dimension of the space luxels evolve in
 * @param I the type of data used as input
 * @param E the type of the Environment
 *
 * @property dimension the dimension instance to build the space
 * @property inputData the input data
 * @property simulationVolume the bounds of the simulation volume
 * @property context the simulation context
 */
data class SceneConfiguration<D : Dimension, I : Any, E : Environment<D>>(
    val dimension: D,
    val inputData: InputData<I>,
    val simulationVolume: Volume<D>,
    val context: SimulationContext<D, E>?,
)
