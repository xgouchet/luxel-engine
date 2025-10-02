package art.luxels.core.utils

/**
 * A basic [Transformer] that returns the input unchanged.
 */
class IdentityTransformer<T : Any> : Transformer<T, T> {
    // region Transformer

    override fun transform(input: T): T {
        return input
    }

    // endregion
}
