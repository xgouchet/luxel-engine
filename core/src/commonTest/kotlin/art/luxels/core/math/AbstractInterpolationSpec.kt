package art.luxels.core.math

import art.luxels.core.test.kotest.assertions.shouldBeCloseTo
import art.luxels.core.test.kotest.property.doubleArb
import art.luxels.core.test.kotest.property.doubleSmallArb
import art.luxels.core.test.kotest.property.inputDoubleArb
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.doubles.shouldBeZero
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun <T : Interpolation> abstractInterpolationSpec(interpolation: T) = describeSpec {
    describe("generic factor") {
        it("always returns a value between 0.0 and 1.0") {
            checkAll(doubleArb()) { t ->
                val result = interpolation.factor(t)

                assertSoftly {
                    result shouldBeGreaterThanOrEqual 0.0
                    result shouldBeLessThanOrEqual 1.0
                }
            }
        }

        it("returns 0 if the input is below 0") {
            checkAll(Arb.double(-Double.MAX_VALUE, -0.0, false)) { t ->
                withAssumptions(t != -0.0) {
                    interpolation.factor(t).shouldBeZero()
                }
            }
        }

        it("returns 1 if the input is above 1") {
            checkAll(Arb.double(1.0, Double.MAX_VALUE, false)) { t ->
                interpolation.factor(t) shouldBe 1.0
            }
        }

        it("is continuous") {
            checkAll(inputDoubleArb()) { t ->
                withAssumptions(t < 1.0 - EPSILON) {
                    val result1 = interpolation.factor(t)
                    val result2 = interpolation.factor(t + (EPSILON / 10.0))

                    val diff = result2 - result1
                    diff shouldBeLessThanOrEqual (4 * EPSILON)
                }
            }
        }

        it("is strictly increasing") {
            checkAll(inputDoubleArb(), inputDoubleArb()) { t1, t2 ->
                val minT = min(t1, t2)
                val maxT = max(t1, t2)

                val resultMin = interpolation.factor(minT)
                val resultMax = interpolation.factor(maxT)

                resultMin shouldBeLessThanOrEqual resultMax
            }
        }

        it("is invariant for 0, 0.5 and 1") {
            assertSoftly {
                interpolation.factor(0.0) shouldBe 0.0
                interpolation.factor(0.5) shouldBeCloseTo 0.5 // allow some margin
                interpolation.factor(1.0) shouldBe 1.0
            }
        }
    }

    describe("generic lerp") {
        it("always returns a value between a and b for input between 0 and 1") {
            checkAll(inputDoubleArb(), doubleArb(), doubleArb()) { t, a, size ->
                val b = a + abs(size)
                val result = interpolation.interpolate(a, b, t)

                assertSoftly {
                    result shouldBeGreaterThanOrEqual a
                    result shouldBeLessThanOrEqual b
                }
            }
        }

        it("returns a value below a if the input is below 0") {
            checkAll(Arb.double(-Double.MAX_VALUE, 0.0, false), doubleArb(), doubleArb()) { t, a, size ->
                val b = a + abs(size)

                interpolation.interpolate(a, b, t) shouldBeLessThanOrEqual a
            }
        }

        it("returns a value above b if the input is above 1") {
            checkAll(Arb.double(1.0, Double.MAX_VALUE, false), doubleArb(), doubleArb()) { t, a, size ->
                val b = a + abs(size)

                interpolation.interpolate(a, b, t) shouldBeGreaterThanOrEqual b
            }
        }

        it("is continuous") {
            // Only check on the [0..1] range
            checkAll(inputDoubleArb(), doubleSmallArb(), doubleSmallArb()) { t, a, size ->
                val b = a + abs(size)
                val result1 = interpolation.interpolate(a, b, t)
                val result2 = interpolation.interpolate(a, b, t + (EPSILON / 10.0))

                val diff = result2 - result1
                diff shouldBeLessThanOrEqual (4 * EPSILON * abs(size))
            }
        }

        it("is strictly increasing") {
            checkAll(doubleSmallArb(), doubleSmallArb(), doubleArb(), doubleArb()) { t1, t2, a, size ->
                val b = a + abs(size)
                val minT = min(t1, t2)
                val maxT = max(t1, t2)

                val resultMin = interpolation.interpolate(a, b, minT)
                val resultMax = interpolation.interpolate(a, b, maxT)

                resultMin shouldBeLessThanOrEqual resultMax
            }
        }

        it("is invariant for 0, 0.5 and 1") {
            checkAll(doubleArb(), doubleArb()) { a, size ->
                val b = a + abs(size)
                assertSoftly {
                    interpolation.interpolate(a, b, 0.0) shouldBeCloseTo a
                    interpolation.interpolate(a, b, 0.5) shouldBeCloseTo ((a + b) / 2)
                    interpolation.interpolate(a, b, 1.0) shouldBeCloseTo b
                }
            }
        }
    }
}
