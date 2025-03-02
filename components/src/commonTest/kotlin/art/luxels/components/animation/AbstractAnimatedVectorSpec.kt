package art.luxels.components.animation

import art.luxels.components.geometry.Curve
import art.luxels.components.test.kotest.assertions.shouldBeCloseTo
import art.luxels.components.test.kotest.property.durationArb
import art.luxels.components.test.kotest.property.vectorListArb
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.time.Duration.Companion.nanoseconds

fun <D : Dimension> abstractAnimatedVectorSpec(dimension: D) = describeSpec {
    describe("an animated Vector [$dimension]") {
        it("throws when instantiated with no points") {
            checkAll(durationArb()) { duration ->
                withAssumptions(duration.inWholeNanoseconds > 0) {
                    shouldThrow<IllegalArgumentException> {
                        AnimatedVector(duration, emptyList<Vector<D>>())
                    }
                }
            }
        }

        it("throws when instantiated with duration 0") {
            checkAll(vectorListArb(dimension)) { points ->
                withAssumptions(points.isNotEmpty()) {
                    shouldThrow<IllegalArgumentException> {
                        AnimatedVector(0.nanoseconds, points)
                    }
                }
            }
        }

        it("returns first point when time=0") {
            checkAll(vectorListArb(dimension), durationArb()) { points, duration ->
                withAssumptions(points.isNotEmpty() && duration.inWholeNanoseconds > 0) {
                    val animated = AnimatedVector(duration, points)

                    val result = animated.getValue(0.nanoseconds)

                    result shouldBeCloseTo points.first()
                }
            }
        }

        it("returns last point when time=duration") {
            checkAll(vectorListArb(dimension), durationArb()) { points, duration ->
                withAssumptions(points.isNotEmpty() && duration.inWholeNanoseconds > 0) {
                    val animated = AnimatedVector(duration, points)

                    val result = animated.getValue(duration)

                    result shouldBeCloseTo points.last()
                }
            }
        }

        it("is continuous") {
            checkAll(vectorListArb(dimension), durationArb(), Arb.double(0.0, 1.0)) { points, duration, nanos ->
                withAssumptions(points.size > 1 && duration.inWholeNanoseconds > 1000) {
                    val animated = AnimatedVector(duration, points)
                    val time = (duration.inWholeNanoseconds * nanos).nanoseconds

                    val point = animated.getValue(time)
                    val neighbour = animated.getValue(time + 1.nanoseconds)

                    val delta = neighbour - point
                    val threshold = Curve(points).volume.size.length()
                    delta.length() shouldBeLessThanOrEqual (threshold * 0.001)
                }
            }
        }
    }
}
