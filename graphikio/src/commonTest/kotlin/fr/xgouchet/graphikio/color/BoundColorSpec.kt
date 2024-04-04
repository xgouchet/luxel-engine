package fr.xgouchet.graphikio.color

import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_BLUE
import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_GREEN
import fr.xgouchet.graphikio.color.Color.Companion.LUMINANCE_RED
import fr.xgouchet.graphikio.color.SDRColor.Companion.BLACK
import fr.xgouchet.graphikio.color.SDRColor.Companion.BLUE
import fr.xgouchet.graphikio.color.SDRColor.Companion.CHANNEL_RANGE
import fr.xgouchet.graphikio.color.SDRColor.Companion.GREEN
import fr.xgouchet.graphikio.color.SDRColor.Companion.GREY
import fr.xgouchet.graphikio.color.SDRColor.Companion.HALF_VALUE
import fr.xgouchet.graphikio.color.SDRColor.Companion.MAX_VALUE
import fr.xgouchet.graphikio.color.SDRColor.Companion.RED
import fr.xgouchet.graphikio.color.SDRColor.Companion.TEAL
import fr.xgouchet.graphikio.color.SDRColor.Companion.TRANSPARENT
import fr.xgouchet.graphikio.color.SDRColor.Companion.VIOLET
import fr.xgouchet.graphikio.color.SDRColor.Companion.WHITE
import fr.xgouchet.graphikio.color.SDRColor.Companion.YELLOW
import fr.xgouchet.graphikio.test.kotest.property.boundColorArb
import fr.xgouchet.graphikio.test.kotest.property.colorChannelDoubleArb
import fr.xgouchet.graphikio.test.kotest.property.colorChannelIntArb
import fr.xgouchet.graphikio.test.kotest.property.doubleArb
import fr.xgouchet.graphikio.test.kotest.property.intArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.between
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.math.abs
import kotlin.math.roundToInt

class BoundColorSpec : DescribeSpec({

    describe("int constructor") {
        it("keep all channel in bound range") {
            checkAll(intArb(), intArb(), intArb(), intArb()) { r, g, b, a ->
                val c = SDRColor(r, g, b, a)

                c.r shouldBeInRange CHANNEL_RANGE
                c.g shouldBeInRange CHANNEL_RANGE
                c.b shouldBeInRange CHANNEL_RANGE
                c.a shouldBeInRange CHANNEL_RANGE
            }
        }

        it("use the input channel values if in range") {
            checkAll(
                colorChannelIntArb(),
                colorChannelIntArb(),
                colorChannelIntArb(),
                colorChannelIntArb(),
            ) { r, g, b, a ->
                val c = SDRColor(r, g, b, a)

                c.r shouldBe r
                c.g shouldBe g
                c.b shouldBe b
                c.a shouldBe a
            }
        }
    }

    describe("double constructor") {
        it("keep all channel in bound range") {
            checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb()) { r, g, b, a ->
                val c = SDRColor(r, g, b, a)

                c.r shouldBeInRange CHANNEL_RANGE
                c.g shouldBeInRange CHANNEL_RANGE
                c.b shouldBeInRange CHANNEL_RANGE
                c.a shouldBeInRange CHANNEL_RANGE
            }
        }

        it("uses the nearest int when in range") {
            checkAll(
                colorChannelDoubleArb(),
                colorChannelDoubleArb(),
                colorChannelDoubleArb(),
                colorChannelDoubleArb(),
            ) { r, g, b, a ->
                val c = SDRColor(r, g, b, a)

                abs(c.r - r) shouldBe between(0.0, 1.0, 0.0001)
                abs(c.g - g) shouldBe between(0.0, 1.0, 0.0001)
                abs(c.b - b) shouldBe between(0.0, 1.0, 0.0001)
                abs(c.a - a) shouldBe between(0.0, 1.0, 0.0001)
            }
        }
    }

    describe("addition") {
        it("is commutative: c + d = d + c") {
            checkAll(boundColorArb(), boundColorArb()) { c, d ->
                val `c + d` = c + d
                val `d + c` = d + c

                `c + d` shouldBe `d + c`
            }
        }

        it("is associative: c + (d + w) = (c + d) + w") {
            checkAll(boundColorArb(), boundColorArb(), boundColorArb()) { c, d, w ->
                val `c + _d + w_` = c + (d + w)
                val `_c + d_ + w` = (c + d) + w

                `c + _d + w_` shouldBe `_c + d_ + w`
            }
        }

        it("is neutral with null vector: c + 0 = c") {
            checkAll(boundColorArb()) { c ->
                val `c + 0` = c + TRANSPARENT

                `c + 0` shouldBe c
            }
        }

        it("applies to each component ") {
            checkAll(boundColorArb()) { c ->
                assume {
                    c.r shouldBeLessThan 254
                    c.g shouldBeLessThan 254
                    c.b shouldBeLessThan 254
                    c.a shouldBeLessThan 254
                }
                val `c + unit` = c + SDRColor(1, 1, 1, 1)

                `c + unit`.r shouldBe (c.r + 1)
                `c + unit`.g shouldBe (c.g + 1)
                `c + unit`.b shouldBe (c.b + 1)
                `c + unit`.a shouldBe (c.a + 1)
            }
        }
    }

    describe("scalar multiplication") {

        it("is neutral with one: c × 1.0 = c") {
            checkAll(boundColorArb()) { c ->
                val `c × one` = c * 1.0

                `c × one` shouldBe c
            }
        }

        it("has zero property: c × 0.0 = null color") {
            checkAll(boundColorArb()) { c ->
                val `c × zero` = c * 0.0

                `c × zero` shouldBe TRANSPARENT
            }
        }

        it("applies to each component") {
            checkAll(boundColorArb()) { c ->
                assume {
                    c.r shouldBeLessThan 127
                    c.g shouldBeLessThan 127
                    c.b shouldBeLessThan 127
                    c.a shouldBeLessThan 127
                }

                val `c × two` = c * 2.0

                `c × two`.r shouldBe (c.r * 2)
                `c × two`.g shouldBe (c.g * 2)
                `c × two`.b shouldBe (c.b * 2)
                `c × two`.a shouldBe (c.a * 2)
            }
        }
    }

    describe("hue") {
        it("returns known hues for pure colors") {
            RED.hue() shouldBe 0
            YELLOW.hue() shouldBe 60
            GREEN.hue() shouldBe 120
            TEAL.hue() shouldBe 180
            BLUE.hue() shouldBe 240
            VIOLET.hue() shouldBe 300

            WHITE.hue() shouldBe 0.0
            GREY.hue() shouldBe 0.0
            BLACK.hue() shouldBe 0.0
        }
    }

    describe("saturation") {
        it("returns known saturation for pure colors") {
            RED.saturation() shouldBe 255
            YELLOW.saturation() shouldBe 255
            GREEN.saturation() shouldBe 255
            TEAL.saturation() shouldBe 255
            BLUE.saturation() shouldBe 255
            VIOLET.saturation() shouldBe 255

            WHITE.saturation() shouldBe 0
            GREY.saturation() shouldBe 0
            BLACK.saturation() shouldBe 0
        }
    }

    describe("value") {
        it("returns known value for pure colors") {
            RED.value() shouldBe 255
            YELLOW.value() shouldBe 255
            GREEN.value() shouldBe 255
            TEAL.value() shouldBe 255
            BLUE.value() shouldBe 255
            VIOLET.value() shouldBe 255

            WHITE.value() shouldBe 255
            GREY.value() shouldBe 128
            BLACK.value() shouldBe 0
        }
    }

    describe("luminance") {
        it("returns known value for pure colors") {
            RED.luminance() shouldBe (LUMINANCE_RED * MAX_VALUE).roundToInt()
            YELLOW.luminance() shouldBe ((LUMINANCE_RED + LUMINANCE_GREEN) * MAX_VALUE).roundToInt()
            GREEN.luminance() shouldBe (LUMINANCE_GREEN * MAX_VALUE).roundToInt()
            TEAL.luminance() shouldBe ((LUMINANCE_GREEN + LUMINANCE_BLUE) * MAX_VALUE).roundToInt()
            BLUE.luminance() shouldBe (LUMINANCE_BLUE * MAX_VALUE).roundToInt()
            VIOLET.luminance() shouldBe ((LUMINANCE_RED + LUMINANCE_BLUE) * MAX_VALUE).roundToInt()

            WHITE.luminance() shouldBe MAX_VALUE
            GREY.luminance() shouldBe HALF_VALUE
            BLACK.luminance() shouldBe 0
        }
    }
})
