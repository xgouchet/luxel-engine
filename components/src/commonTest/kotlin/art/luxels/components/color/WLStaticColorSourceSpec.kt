package art.luxels.components.color

import art.luxels.components.color.WLColorSource.Companion.MAX_IR_LIGHT
import art.luxels.components.color.WLColorSource.Companion.MAX_VISIBLE_LIGHT
import art.luxels.components.color.WLColorSource.Companion.MIN_UV_LIGHT
import art.luxels.components.color.WLColorSource.Companion.MIN_VISIBLE_LIGHT
import art.luxels.components.color.WLColorSource.Companion.PURE_BLUE
import art.luxels.components.color.WLColorSource.Companion.PURE_GREEN
import art.luxels.components.color.WLColorSource.Companion.PURE_RED
import art.luxels.components.color.WLColorSource.Companion.PURE_TEAL
import art.luxels.components.color.WLColorSource.Companion.PURE_VIOLET
import art.luxels.components.color.WLColorSource.Companion.PURE_YELLOW
import art.luxels.components.test.kotest.assertions.shouldBeCloseTo
import art.luxels.components.test.kotest.assertions.shouldBeInRange
import art.luxels.imageio.color.HDRColor
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class WLStaticColorSourceSpec : DescribeSpec(
    {

        describe("color") {
            it("is transparent beyond ultraviolet") {
                checkAll(Arb.double(max = MIN_UV_LIGHT)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("is transparent beyond infrared") {
                checkAll(Arb.double(min = MAX_IR_LIGHT)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color shouldBeCloseTo HDRColor.TRANSPARENT
                }
            }

            it("is opaque within visible light range") {
                checkAll(Arb.double(min = MIN_VISIBLE_LIGHT, max = MAX_VISIBLE_LIGHT)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.a shouldBe 1.0
                }
            }

            it("is has expected components in the UV-violet range") {
                checkAll(Arb.double(min = MIN_UV_LIGHT, max = PURE_VIOLET)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r.shouldBeInRange(0.0, 1.0)
                    color.g.shouldBeInRange(0.0, 1.0)
                    color.b shouldBe 0.0
                    color.a.shouldBeInRange(0.0, 1.0)
                }
            }

            it("is has expected components in the violet-blue range") {
                checkAll(Arb.double(min = PURE_VIOLET, max = PURE_BLUE)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r.shouldBeInRange(0.0, 1.0)
                    color.g shouldBe 0.0
                    color.b.shouldBeInRange(0.0, 1.0)
                    color.a.shouldBeInRange(0.0, 1.0)
                }
            }

            it("is has expected components in the blue-teal range") {
                checkAll(Arb.double(min = PURE_BLUE, max = PURE_TEAL)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r shouldBe 0.0
                    color.g.shouldBeInRange(0.0, 1.0)
                    color.b shouldBe 1.0
                    color.a shouldBe 1.0
                }
            }

            it("is has expected components in the teal-green range") {
                checkAll(Arb.double(min = PURE_TEAL, max = PURE_GREEN)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r shouldBe 0.0
                    color.g shouldBe 1.0
                    color.b.shouldBeInRange(0.0, 1.0)
                    color.a shouldBe 1.0
                }
            }

            it("is has expected components in the green-yellow range") {
                checkAll(Arb.double(min = PURE_GREEN, max = PURE_YELLOW)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r.shouldBeInRange(0.0, 1.0)
                    color.g shouldBe 1.0
                    color.b shouldBe 0.0
                    color.a shouldBe 1.0
                }
            }

            it("is has expected components in the yellow-red range") {
                checkAll(Arb.double(min = PURE_YELLOW, max = PURE_RED)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r shouldBe 1.0
                    color.g.shouldBeInRange(0.0, 1.0)
                    color.b shouldBe 0.0
                    color.a shouldBe 1.0
                }
            }

            it("is has expected components in the red-IR range") {
                checkAll(Arb.double(min = PURE_RED, max = MAX_IR_LIGHT)) { w ->
                    val color = WLStaticColorSource(w).color()

                    color.r.shouldBeInRange(0.0, 1.0)
                    color.g shouldBe 0.0
                    color.b shouldBe 0.0
                    color.a.shouldBeInRange(0.0, 1.0)
                }
            }

            it("is has expected components for pure colors") {
//            EMSColorSource(PURE_VIOLET).color() shouldBeCloseTo Color.VIOLET
                WLStaticColorSource(PURE_BLUE).color() shouldBeCloseTo HDRColor.BLUE
                WLStaticColorSource(PURE_TEAL).color() shouldBeCloseTo HDRColor.TEAL
                WLStaticColorSource(PURE_GREEN).color() shouldBeCloseTo HDRColor.GREEN
                WLStaticColorSource(PURE_YELLOW).color() shouldBeCloseTo HDRColor.YELLOW
                WLStaticColorSource(PURE_RED).color() shouldBeCloseTo HDRColor.RED
            }
        }
    },
)
