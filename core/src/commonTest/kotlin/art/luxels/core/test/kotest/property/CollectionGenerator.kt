package art.luxels.core.test.kotest.property

import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string

fun nonEmptyStringCollectionArb(): Arb<List<String>> {
    return Arb.list(Arb.string(), 1..32)
}
