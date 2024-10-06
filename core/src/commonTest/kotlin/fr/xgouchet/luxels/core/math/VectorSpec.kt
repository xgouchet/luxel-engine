package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.math.PI

@Suppress("LocalVariableName", "NonAsciiCharacters", "ktlint:standard:property-naming")
class VectorSpec : DescribeSpec(
    {
        describe("Named constructors") {
            it("creates empty 1 dimensional vector") {
                checkAll(doubleArb()) { x ->
                    val v = Vector1(x)

                    v.x shouldBe x
                }
            }

            it("creates empty 2 dimensional vector") {
                checkAll(doubleArb(), doubleArb()) { x, y ->
                    val v = Vector2(x, y)

                    v.x shouldBe x
                    v.y shouldBe y
                }
            }

            it("creates empty 3 dimensional vector") {
                checkAll(doubleArb(), doubleArb(), doubleArb()) { x, y, z ->
                    val v = Vector3(x, y, z)

                    v.x shouldBe x
                    v.y shouldBe y
                    v.z shouldBe z
                    v.xy shouldBe Vector2(x, y)
                    v.xz shouldBe Vector2(x, z)
                    v.yz shouldBe Vector2(y, z)
                }
            }

            it("creates empty 4 dimensional vector") {
                checkAll(doubleArb(), doubleArb(), doubleArb(), doubleArb()) { x, y, z, w ->
                    val v = Vector4(x, y, z, w)

                    v.x shouldBe x
                    v.y shouldBe y
                    v.z shouldBe z
                    v.w shouldBe w
                    v.xy shouldBe Vector2(x, y)
                    v.xz shouldBe Vector2(x, z)
                    v.xw shouldBe Vector2(x, w)
                    v.yz shouldBe Vector2(y, z)
                    v.yw shouldBe Vector2(y, w)
                    v.zw shouldBe Vector2(z, w)
                    v.xyz shouldBe Vector3(x, y, z)
                    v.xyw shouldBe Vector3(x, y, w)
                    v.xzw shouldBe Vector3(x, z, w)
                    v.yzw shouldBe Vector3(y, z, w)
                }
            }
        }

        describe("fromCircular") {
            it("construct known directions") {
                checkAll(Arb.double(0.001, 100.0)) { radius ->

                    fromCircular(0.0, radius) shouldBeCloseTo Vector2(radius, 0.0)
                    fromCircular(HALF_PI, radius) shouldBeCloseTo Vector2(0.0, radius)
                    fromCircular(PI, radius) shouldBeCloseTo Vector2(-radius, 0.0)
                    fromCircular(3 * HALF_PI, radius) shouldBeCloseTo Vector2(0.0, -radius)
                }
            }

            it("has 𝜏 frequency") {
                checkAll(doubleArb(), Arb.double(0.001, 100.0)) { angle, radius ->

                    fromCircular(angle, radius) shouldBeCloseTo fromCircular(angle + TAU, radius)
                }
            }

            it("has length consistent with radius") {
                checkAll(doubleArb(), Arb.double(0.001, 100.0)) { angle, radius ->

                    fromCircular(angle, radius).length() shouldBeCloseTo radius
                }
            }

            it("has default radius 1.0") {
                checkAll(doubleArb()) { angle ->

                    fromCircular(angle).length() shouldBeCloseTo 1.0
                }
            }
        }

        describe("cross product") {
            it("is consistent with 3D axes") {
                val x = Vector3(1.0, 0.0, 0.0)
                val y = Vector3(0.0, 1.0, 0.0)
                val z = Vector3(0.0, 0.0, 1.0)

                (x cross y) shouldBeCloseTo z
                (y cross z) shouldBeCloseTo x
                (z cross x) shouldBeCloseTo y
            }

            it("gives null against self: (u × u) = 0") {
                checkAll(vectorArb(Dimension.D3)) { u ->
                    val `u × u` = u cross u

                    `u × u` shouldBeCloseTo Vector.nul(Dimension.D3)
                }
            }

            it("gives null for colinear vectors: u × (k × u) = 0") {
                checkAll(vectorArb(Dimension.D3), Arb.double(-100.0, 100.0)) { u, k ->
                    val `u × _k × u_` = u cross (u * k)

                    // Due to floating point we need to be lenient here
                    `u × _k × u_`.length() shouldBeLessThanOrEqual 2.0E-3
                }
            }

            it("is anti commutative: (u × v) = -(v × u)") {
                checkAll(vectorArb(Dimension.D3), vectorArb(Dimension.D3)) { u, v ->
                    val `u × v` = u cross v
                    val `v × u` = v cross u

                    `u × v` shouldBeCloseTo -`v × u`
                }
            }

            it("is distributive over addition: u × (v + w) = (u × v) + (u × w)") {
                checkAll(vectorArb(Dimension.D3), vectorArb(Dimension.D3), vectorArb(Dimension.D3)) { u, v, w ->
                    val `u × _v + w_` = u cross (v + w)
                    val `_u × v_ + _u × w_` = (u cross v) + (u cross w)

                    `u × _v + w_` shouldBeCloseTo `_u × v_ + _u × w_`
                }
            }

            it("is compatible with scalar multiplication: k(u × v) = (ku) × v = u × (kv)") {
                checkAll(vectorArb(Dimension.D3), vectorArb(Dimension.D3), doubleArb()) { u, v, k ->
                    val `k × _u × v_` = (u cross v) * k
                    val `_k × u_ × v` = (u * k) cross v
                    val `u × _k × v_` = u cross (v * k)

                    `k × _u × v_` shouldBeCloseTo `_k × u_ × v`
                    `k × _u × v_` shouldBeCloseTo `u × _k × v_`
                }
            }
        }

        include(abstractVectorSpec(Dimension.D1))
        include(abstractVectorSpec(Dimension.D2))
        include(abstractVectorSpec(Dimension.D3))
        include(abstractVectorSpec(Dimension.D4))
    },
)
