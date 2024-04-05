package fr.xgouchet.luxels.components.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.of

fun inputDoubleArb() = Arb.double(0.0, 1.0)

fun doubleArb() = Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false)

fun doublePositiveArb() = Arb.double(0.0, LARGE_DOUBLE, false)

fun doubleSmallArb() = Arb.double(-SMALL_DOUBLE, SMALL_DOUBLE, false)

fun doubleNaNArb() = Arb.of(Double.NaN)
