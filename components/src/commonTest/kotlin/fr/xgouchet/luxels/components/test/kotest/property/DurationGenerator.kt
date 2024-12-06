package fr.xgouchet.luxels.components.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

fun durationArb() = arbitrary {
    Arb.long(0, Long.MAX_VALUE / 2).bind().nanoseconds
}

fun shortDurationArb() = arbitrary {
    Arb.long(1, 10000).bind().milliseconds
}
