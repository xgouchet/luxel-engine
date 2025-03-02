package art.luxels.core.color

import art.luxels.core.test.kotest.property.colorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class StaticColorSourceSpec : DescribeSpec({
    describe("color") {
        it("returns the constructor color") {
            checkAll(colorArb()) { c ->
                val source = StaticColorSource(c)

                source.color() shouldBe c
            }
        }
    }
})
