package fr.xgouchet.luxels.components.noise

/**
 * A generic Noise interface providing a deterministic noise output based on a given input.
 * @param I the type of the Input
 * @param O the type of the Output
 */
interface NoiseGenerator<in I : Any, out O : Any> {
    /**
     * @param input the input to compute the noise
     * @return a noise value
     */
    fun noise(input: I): O
}
