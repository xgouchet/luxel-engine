package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.components.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.components.test.kotest.assertions.shouldBeIn
import fr.xgouchet.luxels.components.test.kotest.property.vectorListArb
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.EPSILON
import fr.xgouchet.luxels.core.math.Vector
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions

fun <D : Dimension> abstractCurveSpec(dimension: D) = describeSpec {
    describe("a Curve [$dimension]") {
        it("throws when instantiated with no points") {
            shouldThrow<IllegalArgumentException> {
                Curve(emptyList<Vector<D>>())
            }
        }

        it("has bounding volume including all points") {
            checkAll(vectorListArb(dimension)) { points ->
                withAssumptions(points.isNotEmpty()) {
                    val curve = Curve(points)

                    val volume = curve.volume

                    points.forEach {
                        it shouldBeIn volume
                    }
                }
            }
        }

        it("returns first point when t=0.0") {
            checkAll(vectorListArb(dimension)) { points ->
                withAssumptions(points.isNotEmpty()) {
                    val curve = Curve(points)

                    val result = curve.getPosition(0.0)

                    result shouldBeCloseTo points.first()
                }
            }
        }

        it("returns last point when t=1.0") {
            checkAll(vectorListArb(dimension)) { points ->
                withAssumptions(points.isNotEmpty()) {
                    val curve = Curve(points)

                    val result = curve.getPosition(1.0)

                    result shouldBeCloseTo points.last()
                }
            }
        }

        it("is continuous") {
            checkAll(vectorListArb(dimension), Arb.double(0.0, 1.0)) { points, t ->
                withAssumptions(points.isNotEmpty()) {
                    val curve = Curve(points)

                    val point = curve.getPosition(t)
                    val neighbour = curve.getPosition(t + EPSILON)

                    val delta = neighbour - point
                    val threshold = curve.volume.size.length()
                    delta.length() shouldBeLessThanOrEqual (threshold * 0.001)
                }
            }
        }
    }
}
