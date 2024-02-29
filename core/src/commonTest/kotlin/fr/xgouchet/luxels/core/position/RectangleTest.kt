package fr.xgouchet.luxels.core.position

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldNotBeIn
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.rectangleArb
import fr.xgouchet.luxels.core.test.kotest.property.vector2Arb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.checkAll
import kotlin.math.abs

@Suppress("LocalVariableName", "NonAsciiCharacters")
class RectangleTest : DescribeSpec({

    describe("size") {
        it("returns the rectangle's size") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)

                space2.size shouldBeCloseTo size
            }
        }

        it("returns null for empty rectangle ") {
            checkAll(vector2Arb()) { u ->
                val space2 = Space2(u, u)

                space2.size shouldBeCloseTo Vector2.NULL
            }
        }

        it("returns unit for unit rectangle ") {
            val space2 = Space2.UNIT

            space2.size shouldBeCloseTo Vector2.UNIT
        }
    }

    describe("center") {
        it("returns the rectangle's center") {
            checkAll(vector2Arb(), vector2Arb()) { center, s ->
                val size = s.abs()
                val min = center - size
                val max = center + size

                val space2 = Space2(min, max)

                space2.center shouldBeCloseTo center
            }
        }

        it("returns center for empty rectangle ") {
            checkAll(vector2Arb()) { u ->
                val space2 = Space2(u, u)

                space2.center shouldBeCloseTo u
            }
        }
    }

    describe("contains") {
        it("min corner") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)

                min shouldBeIn space2
            }
        }

        it("max corner") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)

                max shouldBeIn space2
            }
        }

        it("center") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)

                space2.center shouldBeIn space2
            }
        }

        it("below min") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)
                val belowMin = min - Vector2.UNIT

                belowMin shouldNotBeIn space2
            }
        }

        it("above max") {
            checkAll(vector2Arb(), vector2Arb()) { min, s ->
                val size = s.abs()
                val max = min + size

                val space2 = Space2(min, max)
                val aboveMax = max + Vector2.UNIT

                aboveMax shouldNotBeIn space2
            }
        }
    }

    describe("expanded") {
        it("keeps the center point") {
            checkAll(rectangleArb(), doubleArb()) { b, s ->
                val scale = abs(s)

                val expanded = b.expanded(scale)

                expanded.center shouldBeCloseTo b.center
            }
        }

        it("increases the size by the given factor") {
            checkAll(rectangleArb(), doubleArb()) { b, s ->
                val scale = abs(s)

                val expanded = b.expanded(scale)

                expanded.size shouldBeCloseTo b.size * (1 + scale)
            }
        }
    }
})
