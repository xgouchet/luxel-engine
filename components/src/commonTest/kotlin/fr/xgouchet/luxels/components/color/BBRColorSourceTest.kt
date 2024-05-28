package fr.xgouchet.luxels.components.color

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.math.EPSILON
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class BBRColorSourceTest : DescribeSpec({

    describe("color") {

        it("is transparent red at absolute zero") {
            val color = BBRColorSource(0.0).color()

            color shouldBeCloseTo HDRColor.RED.copy(a = 0.0)
        }

        it("is bright white at 6600K") {
            val color = BBRColorSource(6600.0).color()

            color shouldBeCloseTo HDRColor.WHITE
        }

        it("is infinite blue at High' temperature") {
            val color = BBRColorSource(100000.0).color()

            color shouldBeCloseTo HDRColor(0.51945, 0.67378, 1.0)
        }

        it("has full red for temperature below 6600k") {
            checkAll(Arb.double(0.0, 6600.0)) { k ->
                val color = BBRColorSource(k).color()

                color.r shouldBe 1.0
            }
        }

        it("has decreasing red for temperature above 6600k") {
            checkAll(Arb.double(6600.0)) { k ->
                val color1 = BBRColorSource(k).color()
                val color2 = BBRColorSource(k + EPSILON).color()

                val delta = color1.r - color2.r

                delta shouldBeGreaterThanOrEqual 0.0
                delta shouldBeLessThan 0.01
            }
        }

        it("has increasing green for temperature below 6600k") {
            checkAll(Arb.double(EPSILON, 6600.0)) { k ->
                val color1 = BBRColorSource(k).color()
                val color2 = BBRColorSource(k - EPSILON).color()

                val delta = color1.g - color2.g

                delta shouldBeGreaterThanOrEqual 0.0
                delta shouldBeLessThan 0.01
            }
        }

        it("has decreasing green for temperature above 6600k") {
            checkAll(Arb.double(6600.0)) { k ->
                val color1 = BBRColorSource(k).color()
                val color2 = BBRColorSource(k + EPSILON).color()

                val delta = color1.g - color2.g

                delta shouldBeGreaterThanOrEqual 0.0
                delta shouldBeLessThan 0.02
            }
        }

        it("has zero blue for temperature below 1900k") {
            checkAll(Arb.double(0.0, 1900.0)) { k ->
                val color = BBRColorSource(k).color()

                color.b shouldBe 0.0
            }
        }

        it("has increasing blue for temperature between 1900k and 6600k") {
            checkAll(Arb.double(1900.0, 6600.0)) { k ->
                val color1 = BBRColorSource(k).color()
                val color2 = BBRColorSource(k + EPSILON).color()

                val delta = color2.b - color1.b

                delta shouldBeGreaterThanOrEqual 0.0
                delta shouldBeLessThan 0.01
            }
        }

        it("has full blue for temperature above 6600k") {
            checkAll(Arb.double(6600.0)) { k ->
                val color = BBRColorSource(k).color()

                color.b shouldBe 1.0
            }
        }
    }
})
