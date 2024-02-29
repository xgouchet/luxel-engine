package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.MAX_IR_LIGHT
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.MAX_VISIBLE_LIGHT
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.MIN_UV_LIGHT
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.MIN_VISIBLE_LIGHT
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_BLUE
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_GREEN
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_RED
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_TEAL
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_VIOLET
import fr.xgouchet.luxels.core.color.EMSColorSource.Companion.PURE_YELLOW
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeInRange
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class EMSColorSourceTest : DescribeSpec({

    describe("color") {
        it("is transparent beyond ultraviolet") {
            checkAll(Arb.double(max = MIN_UV_LIGHT)) { w ->
                val color = EMSColorSource(w).color()

                color shouldBeCloseTo Color.TRANSPARENT
            }
        }

        it("is transparent beyond infrared") {
            checkAll(Arb.double(min = MAX_IR_LIGHT)) { w ->
                val color = EMSColorSource(w).color()

                color shouldBeCloseTo Color.TRANSPARENT
            }
        }

        it("is opaque within visible light range") {
            checkAll(Arb.double(min = MIN_VISIBLE_LIGHT, max = MAX_VISIBLE_LIGHT)) { w ->
                val color = EMSColorSource(w).color()

                color.a shouldBe 1.0
            }
        }

        it("is has expected components in the UV-violet range") {
            checkAll(Arb.double(min = MIN_UV_LIGHT, max = PURE_VIOLET)) { w ->
                val color = EMSColorSource(w).color()

                color.r.shouldBeInRange(0.0, 1.0)
                color.g.shouldBeInRange(0.0, 1.0)
                color.b shouldBe 0.0
                color.a.shouldBeInRange(0.0, 1.0)
            }
        }

        it("is has expected components in the violet-blue range") {
            checkAll(Arb.double(min = PURE_VIOLET, max = PURE_BLUE)) { w ->
                val color = EMSColorSource(w).color()

                color.r.shouldBeInRange(0.0, 1.0)
                color.g shouldBe 0.0
                color.b.shouldBeInRange(0.0, 1.0)
                color.a.shouldBeInRange(0.0, 1.0)
            }
        }

        it("is has expected components in the blue-teal range") {
            checkAll(Arb.double(min = PURE_BLUE, max = PURE_TEAL)) { w ->
                val color = EMSColorSource(w).color()

                color.r shouldBe 0.0
                color.g.shouldBeInRange(0.0, 1.0)
                color.b shouldBe 1.0
                color.a shouldBe 1.0
            }
        }

        it("is has expected components in the teal-green range") {
            checkAll(Arb.double(min = PURE_TEAL, max = PURE_GREEN)) { w ->
                val color = EMSColorSource(w).color()

                color.r shouldBe 0.0
                color.g shouldBe 1.0
                color.b.shouldBeInRange(0.0, 1.0)
                color.a shouldBe 1.0
            }
        }

        it("is has expected components in the green-yellow range") {
            checkAll(Arb.double(min = PURE_GREEN, max = PURE_YELLOW)) { w ->
                val color = EMSColorSource(w).color()

                color.r.shouldBeInRange(0.0, 1.0)
                color.g shouldBe 1.0
                color.b shouldBe 0.0
                color.a shouldBe 1.0
            }
        }

        it("is has expected components in the yellow-red range") {
            checkAll(Arb.double(min = PURE_YELLOW, max = PURE_RED)) { w ->
                val color = EMSColorSource(w).color()

                color.r shouldBe 1.0
                color.g.shouldBeInRange(0.0, 1.0)
                color.b shouldBe 0.0
                color.a shouldBe 1.0
            }
        }

        it("is has expected components in the red-IR range") {
            checkAll(Arb.double(min = PURE_RED, max = MAX_IR_LIGHT)) { w ->
                val color = EMSColorSource(w).color()

                color.r.shouldBeInRange(0.0, 1.0)
                color.g shouldBe 0.0
                color.b shouldBe 0.0
                color.a.shouldBeInRange(0.0, 1.0)
            }
        }

        it("is has expected components for pure colors") {
//            EMSColorSource(PURE_VIOLET).color() shouldBeCloseTo Color.VIOLET
            EMSColorSource(PURE_BLUE).color() shouldBeCloseTo Color.BLUE
            EMSColorSource(PURE_TEAL).color() shouldBeCloseTo Color.TEAL
            EMSColorSource(PURE_GREEN).color() shouldBeCloseTo Color.GREEN
            EMSColorSource(PURE_YELLOW).color() shouldBeCloseTo Color.YELLOW
            EMSColorSource(PURE_RED).color() shouldBeCloseTo Color.RED
        }
    }
})
