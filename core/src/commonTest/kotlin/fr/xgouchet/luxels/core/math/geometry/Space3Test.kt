package fr.xgouchet.luxels.core.math.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.NULL
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldNotBeIn
import fr.xgouchet.luxels.core.test.kotest.property.boxArb
import fr.xgouchet.luxels.core.test.kotest.property.doublePositiveArb
import fr.xgouchet.luxels.core.test.kotest.property.vector3Arb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.checkAll
import kotlin.math.abs

@Suppress("ktlint:standard:property-naming")
class Space3Test : DescribeSpec({

    describe("size") {
        it("returns the box's size") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)

                space3.size shouldBeCloseTo size
            }
        }

        it("returns null for empty box ") {
            checkAll(vector3Arb()) { u ->
                val space3 = Space3(u, u)

                space3.size shouldBeCloseTo Vector3.NULL
            }
        }

        it("returns unit for unit box ") {
            val space3 = Space3.UNIT

            space3.size shouldBeCloseTo Vector3.UNIT
        }
    }

    describe("center") {
        it("returns the box's center") {
            checkAll(vector3Arb(), vector3Arb()) { center, s ->
                val size = s.abs()
                val min = center - size
                val max = center + size

                val space3 = Space3(min, max)

                space3.center shouldBeCloseTo center
            }
        }

        it("returns center for empty box ") {
            checkAll(vector3Arb()) { u ->
                val space3 = Space3(u, u)

                space3.center shouldBeCloseTo u
            }
        }
    }

    describe("contains") {
        it("min corner") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)

                min shouldBeIn space3
            }
        }

        it("max corner") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)

                max shouldBeIn space3
            }
        }

        it("center") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)

                space3.center shouldBeIn space3
            }
        }

        it("below min") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)
                val belowMin = min - Vector3.UNIT

                belowMin shouldNotBeIn space3
            }
        }

        it("above max") {
            checkAll(vector3Arb(), vector3Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space3 = Space3(min, max)
                val aboveMax = max + Vector3.UNIT

                aboveMax shouldNotBeIn space3
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

                expanded.size shouldBeCloseTo b.size * scale
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: b × (x × y) = (b × x) × y") {
            checkAll(boxArb(), doublePositiveArb(), doublePositiveArb()) { b, x, y ->
                val `b × _x × y_` = b * (x * y)
                val `_b × x_ × y` = (b * x) * y

                `b × _x × y_` shouldBeCloseTo `_b × x_ × y`
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

                `b × zero` shouldBeCloseTo Space3(NULL, NULL)
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
