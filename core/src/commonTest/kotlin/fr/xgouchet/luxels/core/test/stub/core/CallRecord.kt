package fr.xgouchet.luxels.core.test.stub.core

import kotlinx.datetime.Instant

data class CallRecord(
    val timestamp: Instant,
    val callIdentifier: CallIdentifier,
    var verified: Boolean,
)
