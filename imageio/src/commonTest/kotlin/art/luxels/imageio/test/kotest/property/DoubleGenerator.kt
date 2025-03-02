package art.luxels.imageio.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.of

const val SMALL_DOUBLE = 128.0
const val LARGE_DOUBLE = 65536.0

fun inputDoubleArb() = Arb.double(0.0, 1.0)

fun scalingDoubleArb() = Arb.double(0.01, 100.0)

fun doubleArb() = Arb.double(-LARGE_DOUBLE, LARGE_DOUBLE, false)

fun colorChannelDoubleArb() = Arb.double(0.0, 255.0, false)

fun doublePositiveArb() = Arb.double(0.0, LARGE_DOUBLE, false)

fun doubleSmallArb() = Arb.double(-SMALL_DOUBLE, SMALL_DOUBLE, false)

fun doubleNaNArb() = Arb.of(Double.NaN)
