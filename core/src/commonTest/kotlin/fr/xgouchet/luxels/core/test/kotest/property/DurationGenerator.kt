package fr.xgouchet.luxels.core.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import kotlin.time.Duration.Companion.milliseconds

fun durationArb() = arbitrary {
    Arb.long(-Long.MAX_VALUE / 2, Long.MAX_VALUE / 2).bind().milliseconds
}