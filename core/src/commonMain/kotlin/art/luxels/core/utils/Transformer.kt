package art.luxels.core.utils

interface Transformer<I : Any, O : Any> {

    fun transform(input: I): O
}