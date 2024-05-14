package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.beCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.beIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.rectangleArb
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import io.kotest.property.PropTestConfig
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.abs

@OptIn(ExperimentalKotest::class)
@Suppress("LocalVariableName", "NonAsciiCharacters")
fun <D : Dimension> abstractVolumeSpec(d: D) = describeSpec {
    val nulVector = Vector.nul(d)
    val unitVector = Vector.unit(d)
    val unitVolume = Volume(nulVector, unitVector)
    val axes = Vector.axes(d)
    val vectorArb = vectorArb(d)

    describe("init ($d)") {
        it("fails when max < min") {
            checkAll(PropTestConfig(seed = 5746776119396637590L), vectorArb, vectorArb) { min, s ->
                val size = s.abs()

                withAssumptions(size.length() > EPSILON) {
                    val max = min - size

                    shouldThrow<IllegalStateException> {
                        Volume(min, max)
                    }
                }
            }
        }
    }

    describe("size ($d)") {
        it("returns the rectangle's size") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                volume.size should beCloseTo(size)
            }
        }

        it("returns null for empty rectangle ") {
            checkAll(vectorArb) { u ->
                val volume = Volume(u, u)

                volume.size should beCloseTo(nulVector)
            }
        }

        it("returns unit for unit rectangle ") {
            val volume = unitVolume

            volume.size should beCloseTo(unitVector)
        }
    }

    describe("center ($d)") {
        it("returns the rectangle's center") {
            checkAll(vectorArb, vectorArb) { center, s ->
                val size = s.abs()
                val min = center - size
                val max = center + size

                val volume = Volume(min, max)

                volume.center should beCloseTo(center)
            }
        }

        it("returns center for empty rectangle ") {
            checkAll(vectorArb) { u ->
                val volume = Volume(u, u)

                volume.center should beCloseTo(u)
            }
        }
    }

    describe("contains ($d)") {
        it("min corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                min should beIn(volume)
            }
        }

        it("max corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                max should beIn(volume)
            }
        }

        it("center") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                volume.center should beIn(volume)
            }
        }

        it("below min") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val belowMin = min - unitVector

                belowMin shouldNot beIn(volume)
            }
        }

        it("above max") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val aboveMax = max + unitVector

                aboveMax shouldNot beIn(volume)
            }
        }
    }

    describe("expanded ($d)") {
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

                expanded.size shouldBeCloseTo b.size * scale
            }
        }
    }
}
