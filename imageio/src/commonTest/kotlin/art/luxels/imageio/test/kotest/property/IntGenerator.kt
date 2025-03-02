package art.luxels.imageio.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.int

fun colorChannelIntArb() = Arb.int(0, 255)

fun imageSizeIntArb() = Arb.int(8, 512)

fun intArb() = Arb.int(-65536, 65536)
