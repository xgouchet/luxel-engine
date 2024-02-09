package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.color.Color.Companion.LUMINANCE_BLUE
import fr.xgouchet.luxels.core.color.Color.Companion.LUMINANCE_GREEN
import fr.xgouchet.luxels.core.color.Color.Companion.LUMINANCE_RED
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.colorArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.inputDoubleArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.any
import io.kotest.matchers.doubles.beGreaterThanOrEqualTo
import io.kotest.matchers.doubles.beLessThanOrEqualTo
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.math.abs
import java.awt.Color as AwtColor

class ColorTest : DescribeSpec({

    describe("addition") {
        it("is commutative: c + d = d + c") {
            checkAll(colorArb(), colorArb()) { c, d ->
                val `c + d` = c + d
                val `d + c` = d + c

                `c + d` shouldBeCloseTo `d + c`
            }
        }

        it("is associative: c + (d + w) = (c + d) + w") {
            checkAll(colorArb(), colorArb(), colorArb()) { c, d, w ->
                val `c + (d + w)` = c + (d + w)
                val `(c + d) + w` = (c + d) + w

                `c + (d + w)` shouldBeCloseTo `(c + d) + w`
            }
        }

        it("is neutral with null vector: c + 0 = c") {
            checkAll(colorArb()) { c ->
                val `c + 0` = c + Color.TRANSPARENT

                `c + 0` shouldBeCloseTo c
            }
        }

        it("applies to each component") {
            checkAll(colorArb()) { c ->
                val `c + unit` = c + Color.WHITE

                `c + unit`.r shouldBeCloseTo (c.r + 1.0)
                `c + unit`.g shouldBeCloseTo (c.g + 1.0)
                `c + unit`.b shouldBeCloseTo (c.b + 1.0)
                `c + unit`.a shouldBeCloseTo (c.a + 1.0)
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: c × (x × y) = (c × x) × y") {
            checkAll(colorArb(), doubleArb(), doubleArb()) { c, x, y ->
                val `c × (x × y)` = c * (x * y)
                val `(c × x) × y` = (c * x) * y

                `c × (x × y)` shouldBeCloseTo `(c × x) × y`
            }
        }

        it("is neutral with one: c × 1.0 = c") {
            checkAll(colorArb()) { c ->
                val `c × one` = c * 1.0

                `c × one` shouldBeCloseTo c
            }
        }

        it("has zero property: c × 0.0 = null color") {
            checkAll(colorArb()) { c ->
                val `c × zero` = c * 0.0

                `c × zero` shouldBeCloseTo Color.TRANSPARENT
            }
        }

        it("applies to each component") {
            checkAll(colorArb()) { c ->
                val `c × two` = c * 2.0

                `c × two`.r shouldBeCloseTo (c.r * 2.0)
                `c × two`.g shouldBeCloseTo (c.g * 2.0)
                `c × two`.b shouldBeCloseTo (c.b * 2.0)
                `c × two`.a shouldBeCloseTo (c.a * 2.0)
            }
        }
    }

    describe("hue") {
        it("returns the hue for a saturated color") {
            // avoid imprecise conversions
            checkAll(Arb.double(0.05, 0.95), Arb.double(0.75, 1.0), Arb.double(0.75, 1.0)) { h, s, v ->
                val awtColor = AwtColor.getHSBColor(h.toFloat(), s.toFloat(), v.toFloat())
                val color = awtColor.asColor()

                val delta = (abs(color.hue() - h) * 360.0)
                delta should Matcher.any(
                    beLessThanOrEqualTo(1.0),
                    beGreaterThanOrEqualTo(359.0),
                )
            }
        }

        it("returns known hues for pure colors") {
            Color.RED.hue() shouldBe 0.0
            Color.YELLOW.hue() shouldBeCloseTo 0.16666666
            Color.GREEN.hue() shouldBeCloseTo 0.33333333
            Color.TEAL.hue() shouldBeCloseTo 0.5
            Color.BLUE.hue() shouldBeCloseTo 0.66666666
            Color.VIOLET.hue() shouldBeCloseTo 0.83333333

            Color.WHITE.hue() shouldBe 0.0
            Color.GREY.hue() shouldBe 0.0
            Color.BLACK.hue() shouldBe 0.0
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

        it("returns known saturation for pure colors") {
            Color.RED.saturation() shouldBe 1.0
            Color.YELLOW.saturation() shouldBe 1.0
            Color.GREEN.saturation() shouldBe 1.0
            Color.TEAL.saturation() shouldBe 1.0
            Color.BLUE.saturation() shouldBe 1.0
            Color.VIOLET.saturation() shouldBe 1.0

            Color.WHITE.saturation() shouldBe 0.0
            Color.GREY.saturation() shouldBe 0.0
            Color.BLACK.saturation() shouldBe 0.0
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

        it("returns known value for pure colors") {
            Color.RED.value() shouldBe 1.0
            Color.YELLOW.value() shouldBe 1.0
            Color.GREEN.value() shouldBe 1.0
            Color.TEAL.value() shouldBe 1.0
            Color.BLUE.value() shouldBe 1.0
            Color.VIOLET.value() shouldBe 1.0

            Color.WHITE.value() shouldBe 1.0
            Color.GREY.value() shouldBe 0.5
            Color.BLACK.value() shouldBe 0.0
        }
    }

    describe("luminance") {
        it("returns known value for pure colors") {
            Color.RED.luminance() shouldBe LUMINANCE_RED
            Color.YELLOW.luminance() shouldBe (LUMINANCE_RED + LUMINANCE_GREEN)
            Color.GREEN.luminance() shouldBe LUMINANCE_GREEN
            Color.TEAL.luminance() shouldBe (LUMINANCE_GREEN + LUMINANCE_BLUE)
            Color.BLUE.luminance() shouldBe LUMINANCE_BLUE
            Color.VIOLET.luminance() shouldBe (LUMINANCE_RED + LUMINANCE_BLUE)

            Color.WHITE.luminance() shouldBe 1.0
            Color.GREY.luminance() shouldBe 0.5
            Color.BLACK.luminance() shouldBe 0.0
        }
    }
})
