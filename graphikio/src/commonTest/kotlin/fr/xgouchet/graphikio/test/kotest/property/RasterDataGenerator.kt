package fr.xgouchet.graphikio.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int

fun rasterDataArb() = arbitrary {
    val width = Arb.int(1, 512)
    val height = Arb.int(1, 512)
}
