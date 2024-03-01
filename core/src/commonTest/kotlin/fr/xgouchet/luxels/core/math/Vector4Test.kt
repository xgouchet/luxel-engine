package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.math.Vector4.Companion.NULL
import fr.xgouchet.luxels.core.math.Vector4.Companion.UNIT
import fr.xgouchet.luxels.core.math.Vector4.Companion.W_AXIS
import fr.xgouchet.luxels.core.math.Vector4.Companion.X_AXIS
import fr.xgouchet.luxels.core.math.Vector4.Companion.Y_AXIS
import fr.xgouchet.luxels.core.math.Vector4.Companion.Z_AXIS
import fr.xgouchet.luxels.core.math.Vector4.Companion.fromComponents
import fr.xgouchet.luxels.core.test.kotest.assertions.isTooSmall
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleNaNArb
import fr.xgouchet.luxels.core.test.kotest.property.vector2Arb
import fr.xgouchet.luxels.core.test.kotest.property.vector3Arb
import fr.xgouchet.luxels.core.test.kotest.property.vector4Arb
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
import kotlin.math.abs

@Suppress("RemoveRedundantBackticks")
class Vector4Test : DescribeSpec({

    describe("minus") {
        it("is consistent with addition: u + (-u) = null") {
            checkAll(vector4Arb()) { u ->
                val `u + _-u_` = u + (-u)

                `u + _-u_` shouldBeCloseTo NULL
            }
        }

        it("is consistent with subtraction: -u = null - u") {
            checkAll(vector4Arb()) { u ->
                val `-u` = -u
                val `null - u` = NULL - u

                `-u` shouldBeCloseTo `null - u`
            }
        }
    }

    describe("addition") {
        it("is commutative: u + v = v + u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                val `u + v` = u + v
                val `v + u` = v + u

                `u + v` shouldBeCloseTo `v + u`
            }
        }

        it("is associative: u + (v + w) = (u + v) + w") {
            checkAll(vector4Arb(), vector4Arb(), vector4Arb()) { u, v, w ->
                val `u + _v + w_` = u + (v + w)
                val `_u + v_ + w` = (u + v) + w

                `u + _v + w_` shouldBeCloseTo `_u + v_ + w`
            }
        }

        it("is neutral with null vector: u + 0 = u") {
            checkAll(vector4Arb()) { u ->
                val `u + 0` = u + NULL

                `u + 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector4Arb()) { u ->
                val `u + unit` = u + UNIT

                `u + unit`.x shouldBeCloseTo (u.x + 1.0)
                `u + unit`.y shouldBeCloseTo (u.y + 1.0)
                `u + unit`.z shouldBeCloseTo (u.z + 1.0)
                `u + unit`.w shouldBeCloseTo (u.w + 1.0)
            }
        }
    }

    describe("subtraction") {
        it("is consistent with addition: (u + v) - v = u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                val `_u + v_ - v` = (u + v) - v

                `_u + v_ - v` shouldBeCloseTo u
            }
        }
        it("is consistent with addition: (u - v) + v = u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                val `_u - v_ + v` = (u - v) + v

                `_u - v_ + v` shouldBeCloseTo u
            }
        }

        it("is neutral with null vector: u - 0 = u") {
            checkAll(vector4Arb()) { u ->
                val `u - 0` = u - NULL

                `u - 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector4Arb()) { u ->
                val `u - unit` = u - UNIT

                `u - unit`.x shouldBeCloseTo (u.x - 1.0)
                `u - unit`.y shouldBeCloseTo (u.y - 1.0)
                `u - unit`.z shouldBeCloseTo (u.z - 1.0)
                `u - unit`.w shouldBeCloseTo (u.w - 1.0)
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: u × (x × y) = (u × x) × y") {
            checkAll(vector4Arb(), doubleArb(), doubleArb()) { u, x, y ->
                val `u × _x × y_` = u * (x * y)
                val `_u × x_ × y` = (u * x) * y

                `u × _x × y_` shouldBeCloseTo `_u × x_ × y`
            }
        }

        it("is neutral with one: u × 1.0 = u") {
            checkAll(vector4Arb()) { u ->
                val `u × one` = u * 1.0

                `u × one` shouldBeCloseTo u
            }
        }

        it("has zero property: u × 0.0 = null vector") {
            checkAll(vector4Arb()) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo NULL
            }
        }

        it("applies to each component") {
            checkAll(vector4Arb()) { u ->
                val `u × two` = u * 2.0

                `u × two`.x shouldBeCloseTo (u.x * 2.0)
                `u × two`.y shouldBeCloseTo (u.y * 2.0)
                `u × two`.z shouldBeCloseTo (u.z * 2.0)
                `u × two`.w shouldBeCloseTo (u.w * 2.0)
            }
        }
    }

    describe("vector multiplication") {
        it("is associative: u × (v × w) = (u × v) × w") {
            checkAll(vector4Arb(), vector4Arb(), vector4Arb()) { u, v, w ->
                val `u × _v × w_` = u * (v * w)
                val `_u × v_ × w` = (u * v) * w

                `u × _v × w_` shouldBeCloseTo `_u × v_ × w`
            }
        }

        it("is neutral with unit: u × unit = u") {
            checkAll(vector4Arb()) { u ->
                val `u × unit` = u * UNIT

                `u × unit` shouldBeCloseTo u
            }
        }

        it("has zero property: u × null vector = null vector") {
            checkAll(vector4Arb()) { u ->
                val `u × null` = u * NULL

                `u × null` shouldBeCloseTo NULL
            }
        }
    }

    describe("scalar division") {
        it("is consistent with scalar multiplication: (u × x) ÷ x = u") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u × x_ ÷ x` = (u * x) / x

                    `_u × x_ ÷ x` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with scalar multiplication: (u ÷ x) × x = u") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u ÷ x_ × x` = (u / x) * x

                    `_u ÷ x_ × x` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with one: u ÷ 1.0 = u") {
            checkAll(vector4Arb()) { u ->
                val `u ÷ one` = u / 1.0

                `u ÷ one` shouldBeCloseTo u
            }
        }
    }

    describe("vector division") {
        it("is consistent with vector multiplication: (u × v) ÷ v = u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u × v_ ÷ v` = (u * v) / v

                    `_u × v_ ÷ v` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with vector multiplication: (u ÷ v) × v = u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u ÷ v_ × v` = (u / v) * v

                    `_u ÷ v_ × v` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with unit: u ÷ unit = u") {
            checkAll(vector4Arb()) { u ->
                val `u ÷ unit` = u / UNIT

                `u ÷ unit` shouldBeCloseTo u
            }
        }
    }

    describe("length") {
        it("scales with scalar multiplication: ‖u × x‖ = ‖u‖ × ‖x‖") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
                val `‖u × x‖` = (u * x).length()
                val `‖u‖ × ‖x‖` = u.length() * abs(x)

                `‖u × x‖` shouldBeCloseTo `‖u‖ × ‖x‖`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖ = ‖u‖ ÷ ‖x‖") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
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
            Z_AXIS.length() shouldBeCloseTo 1.0
            W_AXIS.length() shouldBeCloseTo 1.0
        }
    }

    describe("squaredLength") {
        it("scales with scalar multiplication: ‖u × x‖² = ‖u‖² × x²") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
                val `‖u × x‖²` = (u * x).squaredLength()
                val `‖u‖² × x²` = u.squaredLength() * x * x

                `‖u × x‖²` shouldBeCloseTo `‖u‖² × x²`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖² = ‖u‖² ÷ x²") {
            checkAll(vector4Arb(), doubleArb()) { u, x ->
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
            Z_AXIS.squaredLength() shouldBeCloseTo 1.0
            W_AXIS.squaredLength() shouldBeCloseTo 1.0
        }
    }

    describe("normalized") {
        it("creates colinear vector: û = k×u") {
            checkAll(vector4Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val û = u.normalized()
                    val k = û.x / u.x

                    û.y shouldBeCloseTo (k * u.y)
                    û.z shouldBeCloseTo (k * u.z)
                    û.w shouldBeCloseTo (k * u.w)
                }
            }
        }

        it("sets the length to one: ‖û‖ = 1.0") {
            checkAll(vector4Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val `‖û‖` = u.normalized().length()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("sets the squaredLength to one: ‖û‖² = 1.0") {
            checkAll(vector4Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val `‖û‖` = u.normalized().squaredLength()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("leaves aligned unit vectors untouched") {
            X_AXIS.normalized() shouldBeCloseTo X_AXIS
            Y_AXIS.normalized() shouldBeCloseTo Y_AXIS
            Z_AXIS.normalized() shouldBeCloseTo Z_AXIS
            W_AXIS.normalized() shouldBeCloseTo W_AXIS
        }
    }

    describe("dot product") {
        it("is commutative: u · v = v · u") {
            checkAll(vector4Arb(), vector4Arb()) { u, v ->
                val `u · v` = u dot v
                val `v · u` = v dot u

                `u · v` shouldBeCloseTo `v · u`
            }
        }

        it("is distributive over addition: u · (v + w) = (u · v) + (u · w)") {
            checkAll(vector4Arb(), vector4Arb(), vector4Arb()) { u, v, w ->
                val `u · _v + w_` = u dot (v + w)
                val `_u · v_ + _u · w_` = (u dot v) + (u dot w)

                `u · _v + w_` shouldBeCloseTo `_u · v_ + _u · w_`
            }
        }

        it("is bilinear: u · (xv + w) = x(u · v) + (u · w)") {
            checkAll(vector4Arb(), vector4Arb(), vector4Arb(), doubleArb()) { u, v, w, x ->
                val `u · _xv + w_` = u dot ((v * x) + w)
                val `x_u · v_ + _u · w_` = ((u dot v) * x) + (u dot w)

                `u · _xv + w_` shouldBeCloseTo `x_u · v_ + _u · w_`
            }
        }

        it("is consistent with squaredLength: u · u = ‖u‖²") {
            checkAll(vector4Arb()) { u ->
                val `u · u` = u dot u
                val `‖u‖²` = u.squaredLength()

                `u · u` shouldBeCloseTo `‖u‖²`
            }
        }
    }

    describe("floor") {
        it("returns vector below original vector: ⌊u⌋ ≤ u") {
            checkAll(vector4Arb()) { u ->
                val `⌊u⌋` = u.floor()

                `⌊u⌋`.x shouldBeLessThanOrEqual u.x
                `⌊u⌋`.y shouldBeLessThanOrEqual u.y
                `⌊u⌋`.z shouldBeLessThanOrEqual u.z
                `⌊u⌋`.w shouldBeLessThanOrEqual u.w
            }
        }

        it("returns vector at most unit from original vector: u - ⌊u⌋ ≤ unit") {
            checkAll(vector4Arb()) { u ->
                val `u - ⌊u⌋` = u - u.floor()

                `u - ⌊u⌋`.x shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.y shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.z shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.w shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("ceil") {
        it("returns vector below original vector: ⌈u⌉ ≥ u") {
            checkAll(vector4Arb()) { u ->
                val `⌊u⌋` = u.ceil()

                `⌊u⌋`.x shouldBeGreaterThanOrEqual u.x
                `⌊u⌋`.y shouldBeGreaterThanOrEqual u.y
                `⌊u⌋`.z shouldBeGreaterThanOrEqual u.z
                `⌊u⌋`.w shouldBeGreaterThanOrEqual u.w
            }
        }

        it("returns vector at most unit from original vector: ⌈u⌉ - u ≤ unit") {
            checkAll(vector4Arb()) { u ->
                val `⌈u⌉ - u` = u.ceil() - u

                `⌈u⌉ - u`.x shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.y shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.z shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.w shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("abs") {
        it("preserves length: ‖|u|‖ = ‖u‖") {
            checkAll(vector4Arb()) { u ->
                val `‖_u_‖` = u.abs().length()
                val `‖u‖` = u.length()

                `‖_u_‖` shouldBeCloseTo `‖u‖`
            }
        }

        it("returns same result from oposite vactors: |u| = |-u|") {
            checkAll(vector4Arb()) { u ->
                val `_u_` = u.abs()
                val `_-u_` = (-u).abs()

                `_u_` shouldBeCloseTo `_-u_`
            }
        }
    }

    describe("reflect") {
        it("keeps the original vector's length: ‖r‖ = ‖u‖") {
            checkAll(vector4Arb(), vector4Arb()) { u, n ->
                val r = u.reflect(n.normalized())

                r.length() shouldBeCloseTo u.length()
            }
        }

        it("reflects to the normal when co-linear") {
            checkAll(vector4Arb()) { u ->
                val r = u.reflect(-u.normalized())

                r shouldBeCloseTo -u
            }
        }

        it("reflects against X axis") {
            checkAll(vector4Arb()) { u ->
                val r = u.reflect(Vector4.X_AXIS)

                r.x shouldBeCloseTo -u.x
                r.y shouldBeCloseTo u.y
                r.z shouldBeCloseTo u.z
                r.w shouldBeCloseTo u.w
            }
        }

        it("reflects against Y axis") {
            checkAll(vector4Arb()) { u ->
                val r = u.reflect(Vector4.Y_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo -u.y
                r.z shouldBeCloseTo u.z
                r.w shouldBeCloseTo u.w
            }
        }

        it("reflects against Z axis") {
            checkAll(vector4Arb()) { u ->
                val r = u.reflect(Vector4.Z_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo u.y
                r.z shouldBeCloseTo -u.z
                r.w shouldBeCloseTo u.w
            }
        }

        it("reflects against W axis") {
            checkAll(vector4Arb()) { u ->
                val r = u.reflect(Vector4.W_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo u.y
                r.z shouldBeCloseTo u.z
                r.w shouldBeCloseTo -u.w
            }
        }
    }

    describe("fromComponents") {
        it("constructs a vector from a list") {
            checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb()) { x, y, z, w ->
                Vector4.fromComponents(listOf(x, y, z, w)) shouldBe Vector4(x, y, z, w)
            }
        }

        it("throws when not enough components provided") {
            checkAll(doubleArb()) { x ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(x))
                }
            }

            checkAll(doubleArb(), doubleArb()) { x, y ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(x, y))
                }
            }

            checkAll(doubleArb(), doubleArb(), doubleArb()) { x, y, z ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(x, y, z))
                }
            }

            shouldThrowAny {
                Vector4.fromComponents(emptyList())
            }
        }

        it("throws when too many components provided") {
            checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb(), doubleArb()) { a1, a2, a3, a4, a5 ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(a1, a2, a3, a4, a5))
                }
            }

            checkAll(
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
            ) { a1, a2, a3, a4, a5, a6 ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(a1, a2, a3, a4, a5, a6))
                }
            }

            checkAll(
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
            ) { a1, a2, a3, a4, a5, a6, a7 ->
                shouldThrowAny {
                    Vector4.fromComponents(listOf(a1, a2, a3, a4, a5, a6, a7))
                }
            }
        }
    }

    describe("components") {
        it("is consistent with fromComponent") {
            checkAll(vector4Arb()) { u ->
                val copy = fromComponents(u.components())

                copy shouldBeCloseTo u
            }
        }

        it("has components in order") {
            checkAll(vector4Arb()) { u ->
                val components = u.components()

                components shouldHaveSize 4
                components[0] shouldBeCloseTo u.x
                components[1] shouldBeCloseTo u.y
                components[2] shouldBeCloseTo u.z
                components[3] shouldBeCloseTo u.w
            }
        }
    }

    describe("xy") {
        it("returns a Vector2 with X and Y components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, z, w ->
                val v = Vector4(u.x, u.y, z, w)

                v.xy shouldBeCloseTo u
            }
        }
    }

    describe("xz") {
        it("returns a Vector2 with X and Z components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, y, w ->
                val v = Vector4(u.x, y, u.y, w)

                v.xz shouldBeCloseTo u
            }
        }
    }

    describe("xw") {
        it("returns a Vector2 with X and W components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, y, z ->
                val v = Vector4(u.x, y, z, u.y)

                v.xw shouldBeCloseTo u
            }
        }
    }

    describe("yz") {
        it("returns a Vector2 with Y and Z components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, x, w ->
                val v = Vector4(x, u.x, u.y, w)

                v.yz shouldBeCloseTo u
            }
        }
    }

    describe("yw") {
        it("returns a Vector2 with Y and W components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, x, z ->
                val v = Vector4(x, u.x, z, u.y)

                v.yw shouldBeCloseTo u
            }
        }
    }

    describe("zw") {
        it("returns a Vector2 with Z and W components") {
            checkAll(vector2Arb(), doubleArb(), doubleArb()) { u, x, y ->
                val v = Vector4(x, y, u.x, u.y)

                v.zw shouldBeCloseTo u
            }
        }
    }

    describe("xyz") {
        it("returns a Vector3 with X, Y and Z components") {
            checkAll(vector3Arb(), doubleArb()) { u, w ->
                val v = Vector4(u.x, u.y, u.z, w)

                v.xyz shouldBeCloseTo u
            }
        }
    }

    describe("xyw") {
        it("returns a Vector3 with X, Y and W components") {
            checkAll(vector3Arb(), doubleArb()) { u, z ->
                val v = Vector4(u.x, u.y, z, u.z)

                v.xyw shouldBeCloseTo u
            }
        }
    }

    describe("xzw") {
        it("returns a Vector3 with X, Z and W components") {
            checkAll(vector3Arb(), doubleArb()) { u, y ->
                val v = Vector4(u.x, y, u.y, u.z)

                v.xzw shouldBeCloseTo u
            }
        }
    }

    describe("yzw") {
        it("returns a Vector3 with Y, Z and W components") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
                val v = Vector4(x, u.x, u.y, u.z)

                v.yzw shouldBeCloseTo u
            }
        }
    }

    describe("isNaN") {
        it("returns false for non-NaN values") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z, w ->
                val v = Vector4(x, y, z, w)

                v.isNaN() shouldBe false
            }
        }

        it("returns true if x is NaN") {
            checkAll(
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z, w ->
                val v = Vector4(x, y, z, w)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if y is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z, w ->
                val v = Vector4(x, y, z, w)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if z is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z, w ->
                val v = Vector4(x, y, z, w)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if w is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
            ) { x, y, z, w ->
                val v = Vector4(x, y, z, w)

                v.isNaN() shouldBe true
            }
        }
    }
})
