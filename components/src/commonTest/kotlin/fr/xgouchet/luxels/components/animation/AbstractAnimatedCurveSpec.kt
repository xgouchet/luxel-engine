package fr.xgouchet.luxels.components.animation

import fr.xgouchet.luxels.components.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.components.test.kotest.property.animatedVectorListArb
import fr.xgouchet.luxels.components.test.kotest.property.durationArb
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.EPSILON
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.time.Duration.Companion.nanoseconds

fun <D : Dimension> abstractAnimatedCurveSpec(dimension: D) = describeSpec {
    describe("an animated Curve [$dimension]") {
        it("throws when instantiated with no points") {
            checkAll(durationArb()) { duration ->
                withAssumptions(duration.inWholeNanoseconds > 0) {
                    shouldThrow<IllegalArgumentException> {
                        AnimatedCurve(emptyList<AnimatedVector<D>>())
                    }
                }
            }
        }

        it("returns first point's initial position when t=0.0, time=0") {
            checkAll(animatedVectorListArb(dimension)) { animatedPoints ->
                withAssumptions(animatedPoints.isNotEmpty()) {
                    val animated = AnimatedCurve(animatedPoints)

                    val result = animated.getValue(0.nanoseconds).getPosition(0.0)

                    result shouldBeCloseTo animatedPoints.first().getValue(0.nanoseconds)
                }
            }
        }

        it("returns last point's initial position when t=1.0, time=0") {
            checkAll(animatedVectorListArb(dimension)) { animatedPoints ->
                withAssumptions(animatedPoints.isNotEmpty()) {
                    val animated = AnimatedCurve(animatedPoints)

                    val result = animated.getValue(0.nanoseconds).getPosition(1.0)

                    result shouldBeCloseTo animatedPoints.last().getValue(0.nanoseconds)
                }
            }
        }

        it("returns first point's final position when t=0.0, time=maxDuration") {
            checkAll(animatedVectorListArb(dimension)) { animatedPoints ->
                withAssumptions(animatedPoints.isNotEmpty()) {
                    val animated = AnimatedCurve(animatedPoints)
                    val maxDuration = animatedPoints.fold(0.nanoseconds) { prev, it ->
                        max(prev, it.duration)
                    }

                    val result = animated.getValue(maxDuration).getPosition(0.0)

                    result shouldBeCloseTo animatedPoints.first().getValue(maxDuration)
                }
            }
        }

        it("returns last point's final position when t=1.0, time=maxDuration") {
            checkAll(animatedVectorListArb(dimension)) { animatedPoints ->
                withAssumptions(animatedPoints.isNotEmpty()) {
                    val animated = AnimatedCurve(animatedPoints)
                    val maxDuration = animatedPoints.fold(0.nanoseconds) { prev, it ->
                        max(prev, it.duration)
                    }

                    val result = animated.getValue(maxDuration).getPosition(1.0)

                    result shouldBeCloseTo animatedPoints.last().getValue(maxDuration)
                }
            }
        }

        it("is continuous in time") {
            checkAll(
                animatedVectorListArb(dimension),
                durationArb(),
                Arb.double(0.0, 1.0),
            ) { animatedPoints, duration, t ->
                withAssumptions(animatedPoints.isNotEmpty() && duration.inWholeNanoseconds > 1000) {
                    val animated = AnimatedCurve(animatedPoints)

                    val point = animated.getValue(duration).getPosition(t)
                    val neighbour = animated.getValue(duration + 1.nanoseconds).getPosition(t)

                    val delta = neighbour - point
                    val threshold = animated.getValue(duration).volume.size.length()
                    delta.length() shouldBeLessThanOrEqual (threshold * 0.001)
                }
            }
        }

        it("is continuous in space") {
            checkAll(
                animatedVectorListArb(dimension),
                durationArb(),
                Arb.double(0.0, 1.0),
            ) { animatedPoints, duration, t ->
                withAssumptions(animatedPoints.isNotEmpty() && duration.inWholeNanoseconds > 0) {
                    val animated = AnimatedCurve(animatedPoints)

                    val point = animated.getValue(duration).getPosition(t)
                    val neighbour = animated.getValue(duration).getPosition(t + EPSILON)

                    val delta = neighbour - point
                    val threshold = animated.getValue(duration).volume.size.length()
                    delta.length() shouldBeLessThanOrEqual (threshold * 0.01)
                }
            }
        }
    }
}
