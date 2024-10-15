package fr.xgouchet.luxels.core.test.stub.core

fun Stub.verifyCall(
    name: String,
    params: Map<String, Any?> = emptyMap(),
    times: Int = 1,
) {
    callRecorder.verifyCall(CallIdentifier(name, params), times)
}

fun <O : Any?> Stub.handleCallWithReturn(name: String, params: Map<String, Any?> = emptyMap()): O {
    callRecorder.recordCall(CallIdentifier(name, params))

    val response = responseHandler.getResponse(CallIdentifier(name, params))

    @Suppress("UNCHECKED_CAST")
    when (response) {
        is CallResponse.ReturnValue -> return response.value as O

        is CallResponse.ThrowException -> throw response.exception

        else -> TODO()
    }
}

fun Stub.handleUnitCall(name: String, params: Map<String, Any?> = emptyMap()) {
    callRecorder.recordCall(CallIdentifier(name, params))

    val response = responseHandler.getResponse(CallIdentifier(name, params))

    when (response) {
        is CallResponse.ThrowException -> throw response.exception

        null -> {
            // No-Op
        }

        else -> {
            TODO()
        }
    }
}

fun Stub.stubResponse(name: String, params: Map<String, Any?> = emptyMap()): OngoingResponse {
    return OngoingResponse(responseHandler, CallIdentifier(name, params))
}
