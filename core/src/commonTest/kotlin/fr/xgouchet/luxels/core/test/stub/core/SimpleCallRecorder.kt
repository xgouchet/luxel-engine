package fr.xgouchet.luxels.core.test.stub.core

import kotlinx.datetime.Clock

class SimpleCallRecorder : CallRecorder {

    private val calls = mutableListOf<CallRecord>()

    override fun recordCall(callIdentifier: CallIdentifier) {
        calls.add(
            CallRecord(
                timestamp = Clock.System.now(),
                callIdentifier = callIdentifier,
                verified = false,
            ),
        )
    }

    override fun verifyCall(callIdentifier: CallIdentifier, times: Int) {
        repeat(times) {
            val matchingRecord = calls.firstOrNull { record ->
                !record.verified && record.callIdentifier.matches(callIdentifier)
            }

            if (matchingRecord != null) {
                matchingRecord.verified = true
            } else {
                val mismatches = calls.filter { !it.verified }
                error(
                    "Can't find matching function call for $callIdentifier\n" +
                        "found ${mismatches.size} other calls: ${mismatches.joinToString()}",
                )
            }
        }
    }
}
