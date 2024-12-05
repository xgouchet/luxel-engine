package fr.xgouchet.luxels.core.test.stub.core

// TODO create mockito-like DSL
class SimpleResponseHandler : ResponseHandler {

    private val stubbedResponses = mutableListOf<CallResponse>()

    override fun addResponse(response: CallResponse) {
        stubbedResponses.add(response)
    }

    override fun getResponse(callIdentifier: CallIdentifier): CallResponse? {
        val matchingResponse = stubbedResponses.firstOrNull { response ->
            !response.used && callIdentifier.matches(response.callIdentifier)
        }
        if (matchingResponse != null) {
            matchingResponse.used = true
            return matchingResponse
        }

        val usedMatchingResponse = stubbedResponses.lastOrNull { response ->
            callIdentifier.matches(response.callIdentifier)
        }

        return usedMatchingResponse

    }
}
