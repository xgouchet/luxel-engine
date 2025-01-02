package fr.xgouchet.luxels.engine.simulation.runner

import fr.xgouchet.luxels.engine.test.kotest.property.durationArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class FrameInfoSpec : DescribeSpec(
    {
        describe("name") {

            it("is based on its index (0-9)") {
                checkAll(Arb.int(0, 9), durationArb(), Arb.double(0.0, 1.0)) { index, duration, progress ->
                    val frameInfo = FrameInfo(index, duration, progress)

                    frameInfo.name() shouldBe "000$index"
                }
            }

            it("is based on its index (10-99)") {
                checkAll(Arb.int(10, 99), durationArb(), Arb.double(0.0, 1.0)) { index, duration, progress ->
                    val frameInfo = FrameInfo(index, duration, progress)

                    frameInfo.name() shouldBe "00$index"
                }
            }

            it("is based on its index (100-999)") {
                checkAll(Arb.int(100, 999), durationArb(), Arb.double(0.0, 1.0)) { index, duration, progress ->
                    val frameInfo = FrameInfo(index, duration, progress)

                    frameInfo.name() shouldBe "0$index"
                }
            }

            it("is on its index (1000-9999)") {
                checkAll(Arb.int(1000, 9999), durationArb(), Arb.double(0.0, 1.0)) { index, duration, progress ->
                    val frameInfo = FrameInfo(index, duration, progress)

                    frameInfo.name() shouldBe "$index"
                }
            }
        }
    },
)
