package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.test.kotest.property.inputDoubleArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.any
import io.kotest.matchers.doubles.beGreaterThanOrEqualTo
import io.kotest.matchers.doubles.beLessThanOrEqualTo
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.math.abs
import java.awt.Color as AwtColor

class ColorJVMTest : DescribeSpec({

    describe("hue") {
        it("returns the hue for a saturated color") {
            // avoid imprecise conversions
            checkAll(Arb.double(0.05, 0.95), Arb.double(0.75, 1.0), Arb.double(0.75, 1.0)) { h, s, v ->
                val awtColor = AwtColor.getHSBColor(h.toFloat(), s.toFloat(), v.toFloat())
                val color = awtColor.asColor()

                val delta = (abs(color.hue() - h) * 360.0)
                delta should
                    Matcher.any(
                        beLessThanOrEqualTo(1.0),
                        beGreaterThanOrEqualTo(359.0),
                    )
            }
        }
    }

    describe("saturation") {
        it("returns the saturation") {
            // avoid imprecise conversions
            checkAll(inputDoubleArb(), inputDoubleArb(), Arb.double(0.75, 1.0)) { h, s, v ->
                val awtColor = AwtColor.getHSBColor(h.toFloat(), s.toFloat(), v.toFloat())
                val color = awtColor.asColor()

                val delta = abs(color.saturation() - s)
                delta shouldBeLessThanOrEqual 0.01
            }
        }
    }

    describe("value") {
        it("returns the value") {
            checkAll(inputDoubleArb(), inputDoubleArb(), inputDoubleArb()) { h, s, v ->
                val awtColor = AwtColor.getHSBColor(h.toFloat(), s.toFloat(), v.toFloat())
                val color = awtColor.asColor()

                val delta = (abs(color.value() - v) * 256.0)
                delta shouldBeLessThanOrEqual 1.0
            }
        }
    }
})
