package fr.xgouchet.luxels.core.test.stub.core

import kotlinx.datetime.Clock
import kotlin.jvm.Synchronized

class SimpleCallRecorder : CallRecorder {

    private val calls = mutableListOf<CallRecord>()

    @Synchronized
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
                    .map { it.callIdentifier.getParamsDiff(callIdentifier) }
                error(
                    "Can't find matching function call for \n\t$callIdentifier\n" +
                        "found ${mismatches.size} other calls: \n\t${mismatches.joinToString(separator = "\n\t")}",
                )
            }
        }
    }
}
