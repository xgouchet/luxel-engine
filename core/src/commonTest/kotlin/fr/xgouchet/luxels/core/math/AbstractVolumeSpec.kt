package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldNotBeIn
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.describeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalKotest::class)
@Suppress("LocalVariableName", "NonAsciiCharacters")
fun <D : Dimension> abstractVolumeSpec(d: D) = describeSpec {
    val nulVector = Vector.nul(d)
    val unitVector = Vector.unit(d)
    val unitVolume = Volume.unit(d)
    val vectorArb = vectorArb(d)

    describe("init ($d)") {
        it("fails when max < min") {
            checkAll(vectorArb, vectorArb) { min, s ->
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

                volume.size shouldBeCloseTo size
            }
        }

        it("returns null for empty rectangle ") {
            checkAll(vectorArb) { u ->
                val volume = Volume(u, u)

                volume.size shouldBeCloseTo nulVector
            }
        }

        it("returns unit for unit rectangle ") {
            val volume = unitVolume

            volume.size shouldBeCloseTo unitVector
        }
    }

    describe("center ($d)") {
        it("returns the rectangle's center") {
            checkAll(vectorArb, vectorArb) { center, s ->
                val size = s.abs()
                val min = center - size
                val max = center + size

                val volume = Volume(min, max)

                volume.center shouldBeCloseTo center
            }
        }

        it("returns center for empty rectangle ") {
            checkAll(vectorArb) { u ->
                val volume = Volume(u, u)

                volume.center shouldBeCloseTo u
            }
        }
    }

    describe("corner ($d)") {
        it("returns min corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                volume.corner(0) shouldBeCloseTo min
            }
        }

        it("returns max corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size
                val maxCornerIdx = 2.0.pow(d.size).roundToInt()

                val volume = Volume(min, max)

                volume.corner(maxCornerIdx - 1) shouldBeCloseTo max
            }
        }
    }

    describe("contains ($d)") {
        it("min corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                min shouldBeIn volume
            }
        }

        it("max corner") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                max shouldBeIn volume
            }
        }

        it("center") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)

                volume.center shouldBeIn volume
            }
        }

        it("below min") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val belowMin = min - unitVector

                belowMin shouldNotBeIn volume
            }
        }

        it("above max") {
            checkAll(vectorArb, vectorArb) { min, s ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val aboveMax = max + unitVector

                aboveMax shouldNotBeIn volume
            }
        }
    }

    describe("expanded") {
        it("contains initial volume boundaries (when size > 1)") {
            checkAll(vectorArb, vectorArb, Arb.double(1.001, 100.0)) { min, s, factor ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val expanded = volume.expanded(factor)

                volume.min shouldBeIn expanded
                volume.max shouldBeIn expanded
                volume.center shouldBeIn expanded
            }
        }
        it("be contained in initial volume boundaries (when size < 1)") {
            checkAll(vectorArb, vectorArb, Arb.double(0.01, 0.99)) { min, s, factor ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val expanded = volume.expanded(factor)

                expanded.min shouldBeIn volume
                expanded.max shouldBeIn volume
                expanded.center shouldBeIn volume
            }
        }

        it("has same center") {
            checkAll(vectorArb, vectorArb, Arb.double(0.01, 100.0)) { min, s, factor ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val expanded = volume.expanded(factor)

                volume.center shouldBeCloseTo expanded.center
            }
        }

        it("has scaled size") {
            checkAll(vectorArb, vectorArb, Arb.double(0.01, 100.0)) { min, s, factor ->
                val size = s.abs()
                val max = min + size

                val volume = Volume(min, max)
                val expanded = volume.expanded(factor)

                expanded.size shouldBeCloseTo (volume.size * factor)
            }
        }
    }
}
