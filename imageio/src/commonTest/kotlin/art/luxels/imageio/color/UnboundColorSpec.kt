package art.luxels.imageio.color

import art.luxels.imageio.color.Color.Companion.LUMINANCE_BLUE
import art.luxels.imageio.color.Color.Companion.LUMINANCE_GREEN
import art.luxels.imageio.color.Color.Companion.LUMINANCE_RED
import art.luxels.imageio.color.HDRColor.Companion.BLACK
import art.luxels.imageio.color.HDRColor.Companion.BLUE
import art.luxels.imageio.color.HDRColor.Companion.GREEN
import art.luxels.imageio.color.HDRColor.Companion.GREY
import art.luxels.imageio.color.HDRColor.Companion.RED
import art.luxels.imageio.color.HDRColor.Companion.TEAL
import art.luxels.imageio.color.HDRColor.Companion.TRANSPARENT
import art.luxels.imageio.color.HDRColor.Companion.VIOLET
import art.luxels.imageio.color.HDRColor.Companion.WHITE
import art.luxels.imageio.color.HDRColor.Companion.YELLOW
import art.luxels.imageio.test.kotest.assertions.shouldBeCloseTo
import art.luxels.imageio.test.kotest.property.scalingDoubleArb
import art.luxels.imageio.test.kotest.property.unboundColorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

@Suppress("ktlint:standard:property-naming")
class UnboundColorSpec : DescribeSpec(
    {

        describe("addition") {
            it("is commutative: c + d = d + c") {
                checkAll(unboundColorArb(), unboundColorArb()) { c, d ->
                    val `c + d` = c + d
                    val `d + c` = d + c

                    `c + d` shouldBeCloseTo `d + c`
                }
            }

            it("is associative: c + (d + w) = (c + d) + w") {
                checkAll(unboundColorArb(), unboundColorArb(), unboundColorArb()) { c, d, w ->
                    val `c + _d + w_` = c + (d + w)
                    val `_c + d_ + w` = (c + d) + w

                    `c + _d + w_` shouldBeCloseTo `_c + d_ + w`
                }
            }

            it("is neutral with null vector: c + 0 = c") {
                checkAll(unboundColorArb()) { c ->
                    val `c + 0` = c + TRANSPARENT

                    `c + 0` shouldBeCloseTo c
                }
            }

            it("applies to each component") {
                checkAll(unboundColorArb()) { c ->
                    val `c + unit` = c + WHITE

                    `c + unit`.r shouldBeCloseTo (c.r + 1.0)
                    `c + unit`.g shouldBeCloseTo (c.g + 1.0)
                    `c + unit`.b shouldBeCloseTo (c.b + 1.0)
                    `c + unit`.a shouldBeCloseTo (c.a + 1.0)
                }
            }
        }

        describe("scalar multiplication") {
            it("is associative: c × (x × y) = (c × x) × y") {
                checkAll(unboundColorArb(), scalingDoubleArb(), scalingDoubleArb()) { c, x, y ->
                    val `c × _x × y_` = c * (x * y)
                    val `_c × x_ × y` = (c * x) * y

                    `c × _x × y_` shouldBeCloseTo `_c × x_ × y`
                }
            }

            it("is neutral with one: c × 1.0 = c") {
                checkAll(unboundColorArb()) { c ->
                    val `c × one` = c * 1.0

                    `c × one` shouldBeCloseTo c
                }
            }

            it("has zero property: c × 0.0 = null color") {
                checkAll(unboundColorArb()) { c ->
                    val `c × zero` = c * 0.0

                    `c × zero` shouldBeCloseTo TRANSPARENT
                }
            }

            it("applies to each component") {
                checkAll(unboundColorArb()) { c ->
                    val `c × two` = c * 2.0

                    `c × two`.r shouldBeCloseTo (c.r * 2.0)
                    `c × two`.g shouldBeCloseTo (c.g * 2.0)
                    `c × two`.b shouldBeCloseTo (c.b * 2.0)
                    `c × two`.a shouldBeCloseTo (c.a * 2.0)
                }
            }
        }

        describe("scalar multiplication (color only)") {
            it("is associative: c × (x × y) = (c × x) × y") {
                checkAll(unboundColorArb(), scalingDoubleArb(), scalingDoubleArb()) { c, x, y ->
                    val `c × _x × y_` = c.scaleColorOnly((x * y))
                    val `_c × x_ × y` = (c.scaleColorOnly(x)).scaleColorOnly(y)

                    `c × _x × y_` shouldBeCloseTo `_c × x_ × y`
                    `c × _x × y_`.a shouldBeCloseTo c.a
                }
            }

            it("is neutral with one: c × 1.0 = c") {
                checkAll(unboundColorArb()) { c ->
                    val `c × one` = c.scaleColorOnly(1.0)

                    `c × one` shouldBeCloseTo c
                }
            }

            it("has zero property: c × 0.0 = null color") {
                checkAll(unboundColorArb()) { c ->
                    val `c × zero` = c.scaleColorOnly(0.0)

                    `c × zero`.r shouldBeCloseTo 0.0
                    `c × zero`.g shouldBeCloseTo 0.0
                    `c × zero`.b shouldBeCloseTo 0.0
                    `c × zero`.a shouldBeCloseTo c.a
                }
            }

            it("applies to each component") {
                checkAll(unboundColorArb()) { c ->
                    val `c × two` = c.scaleColorOnly(2.0)

                    `c × two`.r shouldBeCloseTo (c.r * 2.0)
                    `c × two`.g shouldBeCloseTo (c.g * 2.0)
                    `c × two`.b shouldBeCloseTo (c.b * 2.0)
                    `c × two`.a shouldBeCloseTo c.a
                }
            }
        }

        describe("hue") {
            it("returns known hues for pure colors") {
                RED.hue() shouldBe 0.0
                YELLOW.hue() shouldBeCloseTo 0.16666666
                GREEN.hue() shouldBeCloseTo 0.33333333
                TEAL.hue() shouldBeCloseTo 0.5
                BLUE.hue() shouldBeCloseTo 0.66666666
                VIOLET.hue() shouldBeCloseTo 0.83333333

                WHITE.hue() shouldBe 0.0
                GREY.hue() shouldBe 0.0
                BLACK.hue() shouldBe 0.0
            }
        }

        describe("saturation") {
            it("returns known saturation for pure colors") {
                RED.saturation() shouldBe 1.0
                YELLOW.saturation() shouldBe 1.0
                GREEN.saturation() shouldBe 1.0
                TEAL.saturation() shouldBe 1.0
                BLUE.saturation() shouldBe 1.0
                VIOLET.saturation() shouldBe 1.0

                WHITE.saturation() shouldBe 0.0
                GREY.saturation() shouldBe 0.0
                BLACK.saturation() shouldBe 0.0
            }
        }

        describe("value") {
            it("returns known value for pure colors") {
                RED.value() shouldBe 1.0
                YELLOW.value() shouldBe 1.0
                GREEN.value() shouldBe 1.0
                TEAL.value() shouldBe 1.0
                BLUE.value() shouldBe 1.0
                VIOLET.value() shouldBe 1.0

                WHITE.value() shouldBe 1.0
                GREY.value() shouldBe 0.5
                BLACK.value() shouldBe 0.0
            }
        }

        describe("luminance") {
            it("returns known value for pure colors") {
                RED.luminance() shouldBe LUMINANCE_RED
                YELLOW.luminance() shouldBe (LUMINANCE_RED + LUMINANCE_GREEN)
                GREEN.luminance() shouldBe LUMINANCE_GREEN
                TEAL.luminance() shouldBe (LUMINANCE_GREEN + LUMINANCE_BLUE)
                BLUE.luminance() shouldBe LUMINANCE_BLUE
                VIOLET.luminance() shouldBe (LUMINANCE_RED + LUMINANCE_BLUE)

                WHITE.luminance() shouldBe 1.0
                GREY.luminance() shouldBe 0.5
                BLACK.luminance() shouldBe 0.0
            }
        }
    },
)
