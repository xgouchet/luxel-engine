package fr.xgouchet.luxels.core.math.geometry

import fr.xgouchet.luxels.core.math.EPSILON
import fr.xgouchet.luxels.core.math.HALF_PI
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.NULL
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.UNIT
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.X_AXIS
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.Y_AXIS
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.fromCircular
import fr.xgouchet.luxels.core.math.geometry.Vector2.Companion.fromComponents
import fr.xgouchet.luxels.core.test.kotest.assertions.isTooSmall
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleNaNArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleSmallArb
import fr.xgouchet.luxels.core.test.kotest.property.vector2Arb
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.PI
import kotlin.math.abs

@Suppress("RemoveRedundantBackticks", "ktlint:standard:property-naming")
class Vector2Test : DescribeSpec({

    describe("minus") {
        it("is consistent with addition: u + (-u) = null") {
            checkAll(vector2Arb()) { u ->
                val `u + _-u_` = u + (-u)

                `u + _-u_` shouldBeCloseTo NULL
            }
        }

        it("is consistent with subtraction: -u = null - u") {
            checkAll(vector2Arb()) { u ->
                val `-u` = -u
                val `null - u` = NULL - u

                `-u` shouldBeCloseTo `null - u`
            }
        }
    }

    describe("addition") {
        it("is commutative: u + v = v + u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                val `u + v` = u + v
                val `v + u` = v + u

                `u + v` shouldBeCloseTo `v + u`
            }
        }

        it("is associative: u + (v + w) = (u + v) + w") {
            checkAll(vector2Arb(), vector2Arb(), vector2Arb()) { u, v, w ->
                val `u + _v + w_` = u + (v + w)
                val `_u + v_ + w` = (u + v) + w

                `u + _v + w_` shouldBeCloseTo `_u + v_ + w`
            }
        }

        it("is neutral with null vector: u + 0 = u") {
            checkAll(vector2Arb()) { u ->
                val `u + 0` = u + NULL

                `u + 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector2Arb()) { u ->
                val `u + unit` = u + UNIT

                `u + unit`.x shouldBeCloseTo (u.x + 1.0)
                `u + unit`.y shouldBeCloseTo (u.y + 1.0)
            }
        }
    }

    describe("subtraction") {
        it("is consistent with addition: (u + v) - v = u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                val `_u + v_ - v` = (u + v) - v

                `_u + v_ - v` shouldBeCloseTo u
            }
        }
        it("is consistent with addition: (u - v) + v = u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                val `_u - v_ + v` = (u - v) + v

                `_u - v_ + v` shouldBeCloseTo u
            }
        }

        it("is neutral with null vector: u - 0 = u") {
            checkAll(vector2Arb()) { u ->
                val `u - 0` = u - NULL

                `u - 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector2Arb()) { u ->
                val `u - unit` = u - UNIT

                `u - unit`.x shouldBeCloseTo (u.x - 1.0)
                `u - unit`.y shouldBeCloseTo (u.y - 1.0)
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: u × (x × y) = (u × x) × y") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, x, y ->
                val `u × _x × y_` = u * (x * y)
                val `_u × x_ × y` = (u * x) * y

                `u × _x × y_` shouldBeCloseTo `_u × x_ × y`
            }
        }

        it("is neutral with one: u × 1.0 = u") {
            checkAll(vector2Arb()) { u ->
                val `u × one` = u * 1.0

                `u × one` shouldBeCloseTo u
            }
        }

        it("has zero property: u × 0.0 = null vector") {
            checkAll(vector2Arb()) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo NULL
            }
        }

        it("applies to each component") {
            checkAll(vector2Arb()) { u ->
                val `u × two` = u * 2.0

                `u × two`.x shouldBeCloseTo (u.x * 2.0)
                `u × two`.y shouldBeCloseTo (u.y * 2.0)
            }
        }
    }

    describe("vector multiplication") {
        it("is associative: u × (v × w) = (u × v) × w") {
            checkAll(vector2Arb(), vector2Arb(), vector2Arb()) { u, v, w ->
                val `u × _v × w_` = u * (v * w)
                val `_u × v_ × w` = (u * v) * w

                `u × _v × w_` shouldBeCloseTo `_u × v_ × w`
            }
        }

        it("is neutral with unit: u × unit = u") {
            checkAll(vector2Arb()) { u ->
                val `u × unit` = u * UNIT

                `u × unit` shouldBeCloseTo u
            }
        }

        it("has zero property: u × null vector = null vector") {
            checkAll(vector2Arb()) { u ->
                val `u × null` = u * NULL

                `u × null` shouldBeCloseTo NULL
            }
        }
    }

    describe("scalar division") {
        it("is consistent with scalar multiplication: (u × x) ÷ x = u") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u × x_ ÷ x` = (u * x) / x

                    `_u × x_ ÷ x` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with scalar multiplication: (u ÷ x) × x = u") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u ÷ x_ × x` = (u / x) * x

                    `_u ÷ x_ × x` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with one: u ÷ 1.0 = u") {
            checkAll(vector2Arb()) { u ->
                val `u ÷ one` = u / 1.0

                `u ÷ one` shouldBeCloseTo u
            }
        }
    }

    describe("vector division") {
        it("is consistent with vector multiplication: (u × v) ÷ v = u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u × v_ ÷ v` = (u * v) / v

                    `_u × v_ ÷ v` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with vector multiplication: (u ÷ v) × v = u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u ÷ v_ × v` = (u / v) * v

                    `_u ÷ v_ × v` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with unit: u ÷ unit = u") {
            checkAll(vector2Arb()) { u ->
                val `u ÷ unit` = u / UNIT

                `u ÷ unit` shouldBeCloseTo u
            }
        }
    }

    describe("length") {
        it("scales with scalar multiplication: ‖u × x‖ = ‖u‖ × ‖x‖") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                val `‖u × x‖` = (u * x).length()
                val `‖u‖ × ‖x‖` = u.length() * abs(x)

                `‖u × x‖` shouldBeCloseTo `‖u‖ × ‖x‖`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖ = ‖u‖ ÷ ‖x‖") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                withAssumptions(!x.isTooSmall()) {
                    val `‖u ÷ x‖` = (u / x).length()
                    val `‖u‖ ÷ ‖x‖` = u.length() / abs(x)

                    `‖u ÷ x‖` shouldBeCloseTo `‖u‖ ÷ ‖x‖`
                }
            }
        }

        it("returns zero for null vector: ‖null‖ = 0.0") {
            val `‖null‖` = NULL.length()

            `‖null‖` shouldBeCloseTo 0.0
        }

        it("returns one for axis aligned unit vectors") {
            X_AXIS.length() shouldBeCloseTo 1.0
            Y_AXIS.length() shouldBeCloseTo 1.0
        }
    }

    describe("squaredLength") {
        it("scales with scalar multiplication: ‖u × x‖² = ‖u‖² × x²") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                val `‖u × x‖²` = (u * x).squaredLength()
                val `‖u‖² × x²` = u.squaredLength() * x * x

                `‖u × x‖²` shouldBeCloseTo `‖u‖² × x²`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖² = ‖u‖² ÷ x²") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                withAssumptions(!x.isTooSmall()) {
                    val `‖u ÷ x‖²` = (u / x).squaredLength()
                    val `‖u‖² ÷ x²` = u.squaredLength() / (x * x)

                    `‖u ÷ x‖²` shouldBeCloseTo `‖u‖² ÷ x²`
                }
            }
        }

        it("returns zero for null vector: ‖null‖² = 0.0") {
            val `‖null‖²` = NULL.squaredLength()

            `‖null‖²` shouldBeCloseTo 0.0
        }

        it("returns one for axis aligned unit vectors") {
            X_AXIS.squaredLength() shouldBeCloseTo 1.0
            Y_AXIS.squaredLength() shouldBeCloseTo 1.0
        }
    }

    describe("normalized") {
        it("creates colinear vector: û = k×u") {
            checkAll(vector2Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val û = u.normalized()
                    val k = û.x / u.x

                    û.y shouldBeCloseTo (k * u.y)
                }
            }
        }

        it("sets the length to one: ‖û‖ = 1.0") {
            checkAll(vector2Arb()) { u ->
                withAssumptions(u.length() >= 0.001) {
                    val `‖û‖` = u.normalized().length()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("sets the squaredLength to one: ‖û‖² = 1.0") {
            checkAll(vector2Arb()) { u ->
                withAssumptions(u.length() >= 0.001) {
                    val `‖û‖` = u.normalized().squaredLength()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("leaves aligned unit vectors untouched") {
            X_AXIS.normalized() shouldBeCloseTo X_AXIS
            Y_AXIS.normalized() shouldBeCloseTo Y_AXIS
        }
    }

    describe("dot product") {
        it("is commutative: u · v = v · u") {
            checkAll(vector2Arb(), vector2Arb()) { u, v ->
                val `u · v` = u dot v
                val `v · u` = v dot u

                `u · v` shouldBeCloseTo `v · u`
            }
        }

        it("is distributive over addition: u · (v + w) = (u · v) + (u · w)") {
            checkAll(vector2Arb(), vector2Arb(), vector2Arb()) { u, v, w ->
                val `u · _v + w_` = u dot (v + w)
                val `_u · v_ + _u · w_` = (u dot v) + (u dot w)

                `u · _v + w_` shouldBeCloseTo `_u · v_ + _u · w_`
            }
        }

        it("is bilinear: u · (xv + w) = x(u · v) + (u · w)") {
            checkAll(vector2Arb(), vector2Arb(), vector2Arb(), doubleArb()) { u, v, w, x ->
                val `u · _xv + w_` = u dot ((v * x) + w)
                val `x_u · v_ + _u · w_` = ((u dot v) * x) + (u dot w)

                `u · _xv + w_` shouldBeCloseTo `x_u · v_ + _u · w_`
            }
        }

        it("is consistent with squaredLength: u · u = ‖u‖²") {
            checkAll(vector2Arb()) { u ->
                val `u · u` = u dot u
                val `‖u‖²` = u.squaredLength()

                `u · u` shouldBeCloseTo `‖u‖²`
            }
        }
    }

    describe("floor") {
        it("returns vector below original vector: ⌊u⌋ ≤ u") {
            checkAll(vector2Arb()) { u ->
                val `⌊u⌋` = u.floor()

                `⌊u⌋`.x shouldBeLessThanOrEqual u.x
                `⌊u⌋`.y shouldBeLessThanOrEqual u.y
            }
        }

        it("returns vector at most unit from original vector: u - ⌊u⌋ ≤ unit") {
            checkAll(vector2Arb()) { u ->
                val `u - ⌊u⌋` = u - u.floor()

                `u - ⌊u⌋`.x shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.y shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("ceil") {
        it("returns vector below original vector: ⌈u⌉ ≥ u") {
            checkAll(vector2Arb()) { u ->
                val `⌊u⌋` = u.ceil()

                `⌊u⌋`.x shouldBeGreaterThanOrEqual u.x
                `⌊u⌋`.y shouldBeGreaterThanOrEqual u.y
            }
        }

        it("returns vector at most unit from original vector: ⌈u⌉ - u ≤ unit") {
            checkAll(vector2Arb()) { u ->
                val `⌈u⌉ - u` = u.ceil() - u

                `⌈u⌉ - u`.x shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.y shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("abs") {
        it("preserves length: ‖|u|‖ = ‖u‖") {
            checkAll(vector2Arb()) { u ->
                val `‖_u_‖` = u.abs().length()
                val `‖u‖` = u.length()

                `‖_u_‖` shouldBeCloseTo `‖u‖`
            }
        }

        it("returns same result from oposite vactors: |u| = |-u|") {
            checkAll(vector2Arb()) { u ->
                val `_u_` = u.abs()
                val `_-u_` = (-u).abs()

                `_u_` shouldBeCloseTo `_-u_`
            }
        }
    }

    describe("reflect") {
        it("keeps the original vector's length: ‖r‖ = ‖u‖") {
            checkAll(vector2Arb(), vector2Arb()) { u, n ->
                val r = u.reflect(n.normalized())

                r.length() shouldBeCloseTo u.length()
            }
        }

        it("reflects to the normal when co-linear") {
            checkAll(vector2Arb()) { u ->
                val r = u.reflect(-u.normalized())

                r shouldBeCloseTo -u
            }
        }

        it("reflects against X axis") {
            checkAll(vector2Arb()) { u ->
                val r = u.reflect(X_AXIS)

                r.x shouldBeCloseTo -u.x
                r.y shouldBeCloseTo u.y
            }
        }

        it("reflects against Y axis") {
            checkAll(vector2Arb()) { u ->
                val r = u.reflect(Y_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo -u.y
            }
        }
    }

    // TODO refract

    describe("fromComponents") {
        it("constructs a vector from a list") {
            checkAll(doubleArb(), doubleArb()) { x, y ->
                fromComponents(listOf(x, y)) shouldBe Vector2(x, y)
            }
        }

        it("throws when not enough components provided") {
            checkAll(doubleArb()) { x ->
                shouldThrowAny {
                    fromComponents(listOf(x))
                }
            }

            shouldThrowAny {
                fromComponents(emptyList())
            }
        }

        it("throws when too many components provided") {
            checkAll(doubleArb(), doubleArb(), doubleArb()) { a1, a2, a3 ->
                shouldThrowAny {
                    fromComponents(listOf(a1, a2, a3))
                }
            }

            checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb()) { a1, a2, a3, a4 ->
                shouldThrowAny {
                    fromComponents(listOf(a1, a2, a3, a4))
                }
            }

            checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb(), doubleArb()) { a1, a2, a3, a4, a5 ->
                shouldThrowAny {
                    fromComponents(listOf(a1, a2, a3, a4, a5))
                }
            }
        }
    }

    describe("components") {
        it("is consistent with fromComponent") {
            checkAll(vector2Arb()) { u ->
                val copy = fromComponents(u.components())

                copy shouldBeCloseTo u
            }
        }

        it("has components in order") {
            checkAll(vector2Arb()) { u ->
                val components = u.components()

                components shouldHaveSize 2
                components[0] shouldBeCloseTo u.x
                components[1] shouldBeCloseTo u.y
            }
        }
    }

    describe("fromCircular") {
        it("returns a vector with length equal to radius") {
            checkAll(doubleSmallArb(), doubleSmallArb()) { p, r ->
                val radius = abs(r)
                val v = fromCircular(p, radius)

                v.length() shouldBeCloseTo radius
            }
        }

        it("loops around the polar circle") {
            checkAll(doubleSmallArb()) { r ->
                val radius = abs(r)
                val v1 = fromCircular(0.0, radius)
                val v2 = fromCircular(TAU, radius)

                v1 shouldBeCloseTo v2
            }
        }

        it("creates axis vectors with special inputs") {
            fromCircular(0.0, 1.0) shouldBeCloseTo X_AXIS
            fromCircular(PI, 1.0) shouldBeCloseTo -X_AXIS

            fromCircular(HALF_PI, 1.0) shouldBeCloseTo Y_AXIS
            fromCircular(-HALF_PI, 1.0) shouldBeCloseTo -Y_AXIS
        }

        it("uses sensible defaults ") {
            checkAll(doubleSmallArb()) { p ->
                val v = fromCircular(p)

                v.length() shouldBeCloseTo 1.0
            }
        }
    }

    describe("isNaN") {
        it("returns false for non-NaN values") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y ->
                val v = Vector2(x, y)

                v.isNaN() shouldBe false
            }
        }

        it("returns true if x is NaN") {
            checkAll(
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y ->
                val v = Vector2(x, y)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if y is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
            ) { x, y ->
                val v = Vector2(x, y)

                v.isNaN() shouldBe true
            }
        }
    }
})
