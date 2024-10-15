package fr.xgouchet.luxels.core.test.stub.core

class OngoingResponse(
    private val responseHandler: ResponseHandler,
    private val callIdentifier: CallIdentifier,
) {
    fun <T : Any?> withValue(value: T) {
        responseHandler.addResponse(CallResponse.ReturnValue(callIdentifier, value))
    }

    fun <T : Any?> withValues(vararg values: T) {
        values.forEach { withValue(it) }
    }
}
