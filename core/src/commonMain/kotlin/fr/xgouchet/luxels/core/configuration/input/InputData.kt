package fr.xgouchet.luxels.core.configuration.input

/**
 * An input parameter for a simulation run.
 * @param D the type of data held in this input
 * @property id an identifier for this input, used in the generated image files
 * @property seed a long seed derived from this input, used to seed the Randomness of the simulation
 * @property data the input data
 */
data class InputData<D : Any>(
    val id: String,
    val seed: Long,
    val data: D,
)
