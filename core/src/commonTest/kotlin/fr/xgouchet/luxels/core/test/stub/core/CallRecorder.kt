package fr.xgouchet.luxels.core.test.stub.core

interface CallRecorder {

    fun recordCall(callIdentifier: CallIdentifier)

    fun verifyCall(callIdentifier: CallIdentifier, times: Int = 1)
}
