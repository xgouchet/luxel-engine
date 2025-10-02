package art.luxels.core.utils

/**
 * Interface to transform data from one type to another.
 * @param I the input type
 * @param O the output type
 */
interface Transformer<in I : Any, out O : Any> {

    /**
     * Transforms the input data.
     * @param input the input
     * @return the transformed data
     */
    fun transform(input: I): O
}
