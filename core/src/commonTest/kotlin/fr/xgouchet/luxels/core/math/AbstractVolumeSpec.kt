package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldNotBeIn
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.describeSpec
import io.kotest.property.PropTestConfig
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.abs

@OptIn(ExperimentalKotest::class)
@Suppress("LocalVariableName", "NonAsciiCharacters")
fun <D : Dimension> abstractVolumeSpec(d: D) = describeSpec {
    val nulVector = Vector.nul(d)
    val unitVector = Vector.unit(d)
    val unitVolume = Volume.unit(d)
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
}