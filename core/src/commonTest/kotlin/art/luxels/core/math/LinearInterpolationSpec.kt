package art.luxels.core.math

import art.luxels.core.test.kotest.property.inputDoubleArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class LinearInterpolationSpec : DescribeSpec({
    include(abstractInterpolationSpec(Interpolation.Linear))

    describe("linear interpolation") {
        it("returns the input if between 0 and 1") {
            checkAll(inputDoubleArb()) { t ->
                Interpolation.Linear.factor(t) shouldBe t
            }
        }
    }
})
