package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.test.kotest.property.colorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class StaticColorSourceTest : DescribeSpec({
    describe("color") {
        it("returns the constructor color") {
            checkAll(colorArb()) { c ->
                val source = StaticColorSource(c)

                source.color() shouldBe c
            }
        }
    }
})
