package fr.xgouchet.luxels.core.math.geometry

import fr.xgouchet.luxels.core.math.EPSILON
import fr.xgouchet.luxels.core.math.HALF_PI
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.NULL
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.UNIT
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.X_AXIS
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.Y_AXIS
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.Z_AXIS
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.fromComponents
import fr.xgouchet.luxels.core.math.geometry.Vector3.Companion.fromSpherical
import fr.xgouchet.luxels.core.test.kotest.assertions.isTooSmall
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleNaNArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleSmallArb
import fr.xgouchet.luxels.core.test.kotest.property.vector2Arb
import fr.xgouchet.luxels.core.test.kotest.property.vector3Arb
import fr.xgouchet.luxels.core.test.kotest.property.vector3SmallArb
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
class Vector3Test : DescribeSpec({

    describe("minus") {
        it("is consistent with addition: u + (-u) = null") {
            checkAll(vector3Arb()) { u ->
                val `u + _-u_` = u + (-u)

                `u + _-u_` shouldBeCloseTo NULL
            }
        }

        it("is consistent with subtraction: -u = null - u") {
            checkAll(vector3Arb()) { u ->
                val `-u` = -u
                val `null - u` = NULL - u

                `-u` shouldBeCloseTo `null - u`
            }
        }
    }

    describe("addition") {
        it("is commutative: u + v = v + u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `u + v` = u + v
                val `v + u` = v + u

                `u + v` shouldBeCloseTo `v + u`
            }
        }

        it("is associative: u + (v + w) = (u + v) + w") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb()) { u, v, w ->
                val `u + _v + w_` = u + (v + w)
                val `_u + v_ + w` = (u + v) + w

                `u + _v + w_` shouldBeCloseTo `_u + v_ + w`
            }
        }

        it("is neutral with null vector: u + 0 = u") {
            checkAll(vector3Arb()) { u ->
                val `u + 0` = u + NULL

                `u + 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector3Arb()) { u ->
                val `u + unit` = u + UNIT

                `u + unit`.x shouldBeCloseTo (u.x + 1.0)
                `u + unit`.y shouldBeCloseTo (u.y + 1.0)
                `u + unit`.z shouldBeCloseTo (u.z + 1.0)
            }
        }
    }

    describe("subtraction") {
        it("is consistent with addition: (u + v) - v = u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `_u + v_ - v` = (u + v) - v

                `_u + v_ - v` shouldBeCloseTo u
            }
        }
        it("is consistent with addition: (u - v) + v = u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `_u - v_ + v` = (u - v) + v

                `_u - v_ + v` shouldBeCloseTo u
            }
        }

        it("is neutral with null vector: u - 0 = u") {
            checkAll(vector3Arb()) { u ->
                val `u - 0` = u - NULL

                `u - 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vector3Arb()) { u ->
                val `u - unit` = u - UNIT

                `u - unit`.x shouldBeCloseTo (u.x - 1.0)
                `u - unit`.y shouldBeCloseTo (u.y - 1.0)
                `u - unit`.z shouldBeCloseTo (u.z - 1.0)
            }
        }
    }

    describe("scalar multiplication") {
        it("is associative: u × (x × y) = (u × x) × y") {
            checkAll(vector3Arb(), doubleArb(), doubleArb()) { u, x, y ->
                val `u × _x × y_` = u * (x * y)
                val `_u × x_ × y` = (u * x) * y

                `u × _x × y_` shouldBeCloseTo `_u × x_ × y`
            }
        }

        it("is neutral with one: u × 1.0 = u") {
            checkAll(vector3Arb()) { u ->
                val `u × one` = u * 1.0

                `u × one` shouldBeCloseTo u
            }
        }

        it("has zero property: u × 0.0 = null vector") {
            checkAll(vector3Arb()) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo NULL
            }
        }

        it("applies to each component") {
            checkAll(vector3Arb()) { u ->
                val `u × two` = u * 2.0

                `u × two`.x shouldBeCloseTo (u.x * 2.0)
                `u × two`.y shouldBeCloseTo (u.y * 2.0)
                `u × two`.z shouldBeCloseTo (u.z * 2.0)
            }
        }
    }

    describe("vector multiplication") {
        it("is associative: u × (v × w) = (u × v) × w") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb()) { u, v, w ->
                val `u × _v × w_` = u * (v * w)
                val `_u × v_ × w` = (u * v) * w

                `u × _v × w_` shouldBeCloseTo `_u × v_ × w`
            }
        }

        it("is neutral with unit: u × unit = u") {
            checkAll(vector3Arb()) { u ->
                val `u × unit` = u * UNIT

                `u × unit` shouldBeCloseTo u
            }
        }

        it("has zero property: u × null vector = null vector") {
            checkAll(vector3Arb()) { u ->
                val `u × null` = u * NULL

                `u × null` shouldBeCloseTo NULL
            }
        }
    }

    describe("scalar division") {
        it("is consistent with scalar multiplication: (u × x) ÷ x = u") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u × x_ ÷ x` = (u * x) / x

                    `_u × x_ ÷ x` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with scalar multiplication: (u ÷ x) × x = u") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u ÷ x_ × x` = (u / x) * x

                    `_u ÷ x_ × x` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with one: u ÷ 1.0 = u") {
            checkAll(vector3Arb()) { u ->
                val `u ÷ one` = u / 1.0

                `u ÷ one` shouldBeCloseTo u
            }
        }
    }

    describe("vector division") {
        it("is consistent with vector multiplication: (u × v) ÷ v = u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u × v_ ÷ v` = (u * v) / v

                    `_u × v_ ÷ v` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with vector multiplication: (u ÷ v) × v = u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u ÷ v_ × v` = (u / v) * v

                    `_u ÷ v_ × v` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with unit: u ÷ unit = u") {
            checkAll(vector3Arb()) { u ->
                val `u ÷ unit` = u / UNIT

                `u ÷ unit` shouldBeCloseTo u
            }
        }
    }

    describe("length") {
        it("scales with scalar multiplication: ‖u × x‖ = ‖u‖ × ‖x‖") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
                val `‖u × x‖` = (u * x).length()
                val `‖u‖ × ‖x‖` = u.length() * abs(x)

                `‖u × x‖` shouldBeCloseTo `‖u‖ × ‖x‖`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖ = ‖u‖ ÷ ‖x‖") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
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
        }
    }

    describe("squaredLength") {
        it("scales with scalar multiplication: ‖u × x‖² = ‖u‖² × x²") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
                val `‖u × x‖²` = (u * x).squaredLength()
                val `‖u‖² × x²` = u.squaredLength() * x * x

                `‖u × x‖²` shouldBeCloseTo `‖u‖² × x²`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖² = ‖u‖² ÷ x²") {
            checkAll(vector3Arb(), doubleArb()) { u, x ->
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
        }
    }

    describe("normalized") {
        it("creates colinear vector: û = k×u") {
            checkAll(vector3Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val û = u.normalized()
                    val k = û.x / u.x

                    û.y shouldBeCloseTo (k * u.y)
                    û.z shouldBeCloseTo (k * u.z)
                }
            }
        }

        it("sets the length to one: ‖û‖ = 1.0") {
            checkAll(vector3Arb()) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val `‖û‖` = u.normalized().length()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("sets the squaredLength to one: ‖û‖² = 1.0") {
            checkAll(vector3Arb()) { u ->
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
        }
    }

    describe("dot product") {
        it("is commutative: u · v = v · u") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `u · v` = u dot v
                val `v · u` = v dot u

                `u · v` shouldBeCloseTo `v · u`
            }
        }

        it("is distributive over addition: u · (v + w) = (u · v) + (u · w)") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb()) { u, v, w ->
                val `u · _v + w_` = u dot (v + w)
                val `_u · v_ + _u · w_` = (u dot v) + (u dot w)

                `u · _v + w_` shouldBeCloseTo `_u · v_ + _u · w_`
            }
        }

        it("is bilinear: u · (xv + w) = x(u · v) + (u · w)") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb(), doubleArb()) { u, v, w, x ->
                val `u · _xv + w_` = u dot ((v * x) + w)
                val `x_u · v_ + _u · w_` = ((u dot v) * x) + (u dot w)

                `u · _xv + w_` shouldBeCloseTo `x_u · v_ + _u · w_`
            }
        }

        it("is consistent with squaredLength: u · u = ‖u‖²") {
            checkAll(vector3Arb()) { u ->
                val `u · u` = u dot u
                val `‖u‖²` = u.squaredLength()

                `u · u` shouldBeCloseTo `‖u‖²`
            }
        }
    }

    describe("cross product") {
        it("is anti-commutative: u ⨯ v = -(v ⨯ u)") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `u ⨯ v` = u cross v
                val `-_v ⨯ u_` = -(v cross u)

                `u ⨯ v` shouldBeCloseTo `-_v ⨯ u_`
            }
        }

        it("is distributive over addition: u ⨯ (v + w) = (u ⨯ v) + (u ⨯ w)") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb()) { u, v, w ->
                val `u ⨯ _v + w_` = u cross (v + w)
                val `_u ⨯ v_ + _u ⨯ w_` = (u cross v) + (u cross w)

                `u ⨯ _v + w_` shouldBeCloseTo `_u ⨯ v_ + _u ⨯ w_`
            }
        }

        it("is consistent with scalar multiplication: ku ⨯ v = u ⨯ kv = k(u ⨯ v)") {
            checkAll(vector3Arb(), vector3Arb(), doubleArb()) { u, v, k ->
                val `ku ⨯ v` = (u * k) cross v
                val `u ⨯ kv` = u cross (v * k)
                val `k_u ⨯ v_` = (u cross v) * k

                `ku ⨯ v` shouldBeCloseTo `u ⨯ kv`
                `ku ⨯ v` shouldBeCloseTo `k_u ⨯ v_`
            }
        }

        it("satisfies the Jacobi identity: [u ⨯ (v ⨯ w)] + [v ⨯ (w ⨯ u)] + [w ⨯ (u ⨯ v)] = null") {
            checkAll(vector3SmallArb(), vector3SmallArb(), vector3SmallArb()) { u, v, w ->
                val `u ⨯ _v ⨯ w_` = u cross (v cross w)
                val `v ⨯ _w ⨯ u_` = v cross (w cross u)
                val `w ⨯ _u ⨯ v_` = w cross (u cross v)

                val sum = `u ⨯ _v ⨯ w_` + `v ⨯ _w ⨯ u_` + `w ⨯ _u ⨯ v_`

                // Margin due to double imprecision over the number of operations involved
                sum.length() shouldBeLessThanOrEqual 0.1
                // sum shouldBeCloseTo NULL
            }
        }

        it("against itself returns null: u ⨯ u = null") {
            checkAll(vector3Arb()) { u ->
                val `u ⨯ u` = u cross u

                `u ⨯ u` shouldBeCloseTo NULL
            }
        }

        it("allows triple product: u · (v ⨯ w) = v · (w ⨯ a) = w · (u ⨯ v)") {
            checkAll(vector3Arb(), vector3Arb(), vector3Arb()) { u, v, w ->
                val `u · _v ⨯ w_` = u dot (v cross w)
                val `v · _w ⨯ u_` = v dot (w cross u)
                val `w · _u ⨯ v_` = w dot (u cross v)

                `u · _v ⨯ w_` shouldBeCloseTo `v · _w ⨯ u_`
                `u · _v ⨯ w_` shouldBeCloseTo `w · _u ⨯ v_`
            }
        }

        it("is consistent with dot product and squaredLength: ‖u ⨯ v‖² = ‖u‖² ‖v‖² - (u · v)²") {
            checkAll(vector3Arb(), vector3Arb()) { u, v ->
                val `‖u ⨯ v‖²` = (u cross v).squaredLength()
                val `‖u‖² ‖v‖² - _u · v_²` = (u.squaredLength() * v.squaredLength()) - ((u dot v) * (u dot v))

                `‖u ⨯ v‖²` shouldBeCloseTo `‖u‖² ‖v‖² - _u · v_²`
            }
        }

        it("constructs an orthogonal base") {
            (X_AXIS cross Y_AXIS) shouldBeCloseTo Z_AXIS
            (Y_AXIS cross Z_AXIS) shouldBeCloseTo X_AXIS
            (Z_AXIS cross X_AXIS) shouldBeCloseTo Y_AXIS
        }
    }

    describe("floor") {
        it("returns vector below original vector: ⌊u⌋ ≤ u") {
            checkAll(vector3Arb()) { u ->
                val `⌊u⌋` = u.floor()

                `⌊u⌋`.x shouldBeLessThanOrEqual u.x
                `⌊u⌋`.y shouldBeLessThanOrEqual u.y
                `⌊u⌋`.z shouldBeLessThanOrEqual u.z
            }
        }

        it("returns vector at most unit from original vector: u - ⌊u⌋ ≤ unit") {
            checkAll(vector3Arb()) { u ->
                val `u - ⌊u⌋` = u - u.floor()

                `u - ⌊u⌋`.x shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.y shouldBeLessThanOrEqual 1.0
                `u - ⌊u⌋`.z shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("ceil") {
        it("returns vector below original vector: ⌈u⌉ ≥ u") {
            checkAll(vector3Arb()) { u ->
                val `⌊u⌋` = u.ceil()

                `⌊u⌋`.x shouldBeGreaterThanOrEqual u.x
                `⌊u⌋`.y shouldBeGreaterThanOrEqual u.y
                `⌊u⌋`.z shouldBeGreaterThanOrEqual u.z
            }
        }

        it("returns vector at most unit from original vector: ⌈u⌉ - u ≤ unit") {
            checkAll(vector3Arb()) { u ->
                val `⌈u⌉ - u` = u.ceil() - u

                `⌈u⌉ - u`.x shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.y shouldBeLessThanOrEqual 1.0
                `⌈u⌉ - u`.z shouldBeLessThanOrEqual 1.0
            }
        }
    }

    describe("abs") {
        it("preserves length: ‖|u|‖ = ‖u‖") {
            checkAll(vector3Arb()) { u ->
                val `‖_u_‖` = u.abs().length()
                val `‖u‖` = u.length()

                `‖_u_‖` shouldBeCloseTo `‖u‖`
            }
        }

        it("returns same result from oposite vactors: |u| = |-u|") {
            checkAll(vector3Arb()) { u ->
                val `_u_` = u.abs()
                val `_-u_` = (-u).abs()

                `_u_` shouldBeCloseTo `_-u_`
            }
        }
    }

    describe("reflect") {
        it("keeps the original vector's length: ‖r‖ = ‖u‖") {
            checkAll(vector3Arb(), vector3Arb()) { u, n ->
                val r = u.reflect(n.normalized())

                r.length() shouldBeCloseTo u.length()
            }
        }

        it("reflects to the normal when co-linear") {
            checkAll(vector3Arb()) { u ->
                val r = u.reflect(-u.normalized())

                r shouldBeCloseTo -u
            }
        }

        it("reflects against X axis") {
            checkAll(vector3Arb()) { u ->
                val r = u.reflect(X_AXIS)

                r.x shouldBeCloseTo -u.x
                r.y shouldBeCloseTo u.y
                r.z shouldBeCloseTo u.z
            }
        }

        it("reflects against Y axis") {
            checkAll(vector3Arb()) { u ->
                val r = u.reflect(Y_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo -u.y
                r.z shouldBeCloseTo u.z
            }
        }

        it("reflects against Z axis") {
            checkAll(vector3Arb()) { u ->
                val r = u.reflect(Z_AXIS)

                r.x shouldBeCloseTo u.x
                r.y shouldBeCloseTo u.y
                r.z shouldBeCloseTo -u.z
            }
        }
    }

    // TODO refract

    describe("fromComponents") {
        it("constructs a vector from a list") {
            checkAll(doubleArb(), doubleArb(), doubleArb()) { x, y, z ->
                fromComponents(listOf(x, y, z)) shouldBe Vector3(x, y, z)
            }
        }

        it("throws when not enough components provided") {
            checkAll(doubleArb()) { x ->
                shouldThrowAny {
                    fromComponents(listOf(x))
                }
            }

            checkAll(doubleArb(), doubleArb()) { x, y ->
                shouldThrowAny {
                    fromComponents(listOf(x, y))
                }
            }

            shouldThrowAny {
                fromComponents(emptyList())
            }
        }

        it("throws when too many components provided") {
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

            checkAll(
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
                doubleArb(),
            ) { a1, a2, a3, a4, a5, a6 ->
                shouldThrowAny {
                    fromComponents(listOf(a1, a2, a3, a4, a5, a6))
                }
            }
        }
    }

    describe("components") {
        it("is consistent with fromComponent") {
            checkAll(vector3Arb()) { u ->
                val copy = fromComponents(u.components())

                copy shouldBeCloseTo u
            }
        }

        it("has components in order") {
            checkAll(vector3Arb()) { u ->
                val components = u.components()

                components shouldHaveSize 3
                components[0] shouldBeCloseTo u.x
                components[1] shouldBeCloseTo u.y
                components[2] shouldBeCloseTo u.z
            }
        }
    }

    describe("xy") {
        it("returns a Vector2 with X and Y components") {
            checkAll(vector2Arb(), doubleArb()) { u, z ->
                val v = Vector3(u.x, u.y, z)

                v.xy shouldBeCloseTo u
            }
        }
    }

    describe("yz") {
        it("returns a Vector2 with Y and Z components") {
            checkAll(vector2Arb(), doubleArb()) { u, x ->
                val v = Vector3(x, u.x, u.y)

                v.yz shouldBeCloseTo u
            }
        }
    }

    describe("xz") {
        it("returns a Vector2 with X and Z components") {
            checkAll(vector2Arb(), doubleArb()) { u, y ->
                val v = Vector3(u.x, y, u.y)

                v.xz shouldBeCloseTo u
            }
        }
    }

    describe("fromSpherical") {
        it("returns a vector with length equal to radius") {
            checkAll(doubleSmallArb(), doubleSmallArb(), doubleSmallArb()) { p, a, r ->
                val radius = abs(r)
                val v = fromSpherical(p, a, radius)

                v.length() shouldBeCloseTo radius
            }
        }

        it("loops around the polar circle") {
            checkAll(doubleSmallArb(), doubleSmallArb()) { a, r ->
                val radius = abs(r)
                val v1 = fromSpherical(0.0, a, radius)
                val v2 = fromSpherical(TAU, a, radius)

                v1 shouldBeCloseTo v2
            }
        }

        it("is symmetrical across azimuth=0 (Y plane)") {
            val symmetrical = X_AXIS - Y_AXIS + Z_AXIS
            checkAll(doubleSmallArb(), doubleSmallArb(), doubleSmallArb()) { p, a, r ->
                val radius = abs(r)
                val v1 = fromSpherical(p, a, radius) * symmetrical
                val v2 = fromSpherical(p, -a, radius)

                v1 shouldBeCloseTo v2
            }
        }

        it("returns a vector with y=0 when azimuth=0") {
            checkAll(doubleSmallArb(), doubleSmallArb()) { p, r ->
                val radius = abs(r)
                val v = fromSpherical(p, 0.0, radius)

                v.y shouldBeCloseTo 0.0
            }
        }

        it("creates axis vectors with special inputs") {
            fromSpherical(0.0, 0.0, 1.0) shouldBeCloseTo X_AXIS
            fromSpherical(PI, 0.0, 1.0) shouldBeCloseTo -X_AXIS

            fromSpherical(HALF_PI, 0.0, 1.0) shouldBeCloseTo Z_AXIS
            fromSpherical(-HALF_PI, 0.0, 1.0) shouldBeCloseTo -Z_AXIS

            checkAll(doubleSmallArb()) { p ->
                fromSpherical(p, HALF_PI, 1.0) shouldBeCloseTo Y_AXIS
                fromSpherical(p, -HALF_PI, 1.0) shouldBeCloseTo -Y_AXIS
            }
        }

        it("uses sensible defaults ") {
            checkAll(doubleSmallArb()) { p ->
                val v = fromSpherical(p)

                v.y shouldBeCloseTo 0.0
                v.length() shouldBeCloseTo 1.0
            }
        }
    }

    describe("isNaN") {
        it("returns false for non-NaN values") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z ->
                val v = Vector3(x, y, z)

                v.isNaN() shouldBe false
            }
        }

        it("returns true if x is NaN") {
            checkAll(
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z ->
                val v = Vector3(x, y, z)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if y is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
                Arb.double(includeNonFiniteEdgeCases = false),
            ) { x, y, z ->
                val v = Vector3(x, y, z)

                v.isNaN() shouldBe true
            }
        }

        it("returns true if z is NaN") {
            checkAll(
                Arb.double(includeNonFiniteEdgeCases = false),
                Arb.double(includeNonFiniteEdgeCases = false),
                doubleNaNArb(),
            ) { x, y, z ->
                val v = Vector3(x, y, z)

                v.isNaN() shouldBe true
            }
        }
    }
})
