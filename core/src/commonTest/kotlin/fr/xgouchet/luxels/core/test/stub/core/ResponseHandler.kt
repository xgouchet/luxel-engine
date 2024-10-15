package fr.xgouchet.luxels.core.test.stub.core

interface ResponseHandler {

    fun getResponse(callIdentifier: CallIdentifier): CallResponse?

    fun addResponse(response: CallResponse)
}
