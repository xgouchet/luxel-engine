package fr.xgouchet.luxels.core.test.stub.core

sealed class CallResponse {

    abstract val callIdentifier: CallIdentifier

    var used: Boolean = false

    data class ReturnValue(
        override val callIdentifier: CallIdentifier,
        val value: Any?,
    ) : CallResponse()

    data class ThrowException(
        override val callIdentifier: CallIdentifier,
        val exception: Exception,
    ) : CallResponse()
}
