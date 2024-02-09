package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector3.Companion.NULL
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldNotBeIn
import fr.xgouchet.luxels.core.test.kotest.property.boxArb
import fr.xgouchet.luxels.core.test.kotest.property.doublePositiveArb
import fr.xgouchet.luxels.core.test.kotest.property.vector3Arb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.checkAll
import kotlin.math.abs

@Suppress("LocalVariableName", "NonAsciiCharacters")
class BoxTest : DescribeSpec({

    describe("size") {
        it("returns the box's size") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)

                box.size shouldBeCloseTo size
            }
        }

        it("returns null for empty box ") {
            checkAll(vector3Arb()) { u ->
                val box = Box(u, u)

                box.size shouldBeCloseTo Vector3.NULL
            }
        }

        it("returns unit for unit box ") {
            val box = Box.UNIT

            box.size shouldBeCloseTo Vector3.UNIT
        }
    }

    describe("center") {
        it("returns the box's center") {
            checkAll(vector3Arb(), vector3Arb()) { center, s ->
                val size = s.abs()
                val min = center - size
                val max = center + size

                val box = Box(min, max)

                box.center shouldBeCloseTo center
            }
        }

        it("returns center for empty box ") {
            checkAll(vector3Arb()) { u ->
                val box = Box(u, u)

                box.center shouldBeCloseTo u
            }
        }
    }

    describe("contains") {
        it("min corner") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)

                min shouldBeIn box
            }
        }

        it("max corner") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)

                max shouldBeIn box
            }
        }

        it("center") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)

                box.center shouldBeIn box
            }
        }

        it("below min") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)
                val belowMin = min - Vector3.UNIT

                belowMin shouldNotBeIn box
            }
        }

        it("above max") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val box = Box(min, max)
                val aboveMax = max + Vector3.UNIT

                aboveMax shouldNotBeIn box
            }
        }
    }

    describe("expanded") {
        it("keeps the center point") {
            checkAll(boxArb(), doublePositiveArb()) { b, s ->
                val scale = abs(s)

                val expanded = b.expanded(scale)

                expanded.center shouldBeCloseTo b.center
            }
        }

        it("increases the size by the given factor") {
            checkAll(boxArb(), doublePositiveArb()) { b, s ->
                val scale = abs(s)

                val expanded = b.expanded(scale)

                expanded.size shouldBeCloseTo b.size * (1 + scale)
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: b × (x × y) = (b × x) × y") {
            checkAll(boxArb(), doublePositiveArb(), doublePositiveArb()) { b, x, y ->
                val `b × (x × y)` = b * (x * y)
                val `(b × x) × y` = (b * x) * y

                `b × (x × y)` shouldBeCloseTo `(b × x) × y`
            }
        }

        it("is neutral with one: b × 1.0 = b") {
            checkAll(boxArb()) { b ->
                val `b × one` = b * 1.0

                `b × one` shouldBeCloseTo b
            }
        }

        it("has zero property: b × 0.0 = null vector") {
            checkAll(boxArb()) { b ->
                val `b × zero` = b * 0.0

                `b × zero` shouldBeCloseTo Box(NULL, NULL)
            }
        }

        it("scales the size") {
            checkAll(boxArb(), doublePositiveArb()) { b, x ->
                val `b × x` = b * x

                `b × x`.size shouldBeCloseTo b.size * abs(x)
            }
        }

        it("scales the center") {
            checkAll(boxArb(), doublePositiveArb()) { b, x ->
                val `b × x` = b * x

                `b × x`.center shouldBeCloseTo b.center * x
            }
        }

        it("applies to each component") {
            checkAll(boxArb()) { b ->
                val `b × two` = b * 2.0

                `b × two`.max shouldBeCloseTo (b.max * 2.0)
                `b × two`.min shouldBeCloseTo (b.min * 2.0)
            }
        }
    }
})
