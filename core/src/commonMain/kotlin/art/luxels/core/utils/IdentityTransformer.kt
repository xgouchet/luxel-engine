package art.luxels.core.utils

class IdentityTransformer<T : Any> : Transformer<T, T> {
    override fun transform(input: T): T {
        return input
    }
}