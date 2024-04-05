package fr.xgouchet.luxels.components.noise

// TODO Document and test the output range

/**
 * Provides a way to generate a deterministic noise, based on an n-dimensional input
 * into an m-dimensional output.
 */
interface DimensionalNoiseGenerator {
    /**
     * Generate a deterministic noise based on the input, with the output size dimension.
     * All values in the output are going to be in a [0…1] range.
     *
     * @param input the input
     * @param outputSize the size of the output list
     */
    fun noise(input: List<Double>, outputSize: Int): List<Double>
}
