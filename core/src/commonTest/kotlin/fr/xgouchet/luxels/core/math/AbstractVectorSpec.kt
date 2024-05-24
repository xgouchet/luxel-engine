package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.isTooSmall
import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleInfiniteArb
import fr.xgouchet.luxels.core.test.kotest.property.doubleNaNArb
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.abs

@Suppress("LocalVariableName", "NonAsciiCharacters", "ktlint:standard:property-naming")
fun <D : Dimension> abstractVectorSpec(d: D) = describeSpec {
    val nul = Vector.nul(d)
    val unit = Vector.unit(d)
    val axes = Vector.axes(d)
    val vectorArb = vectorArb(d)

    describe("minus ($d)") {
        it("is consistent with addition: u + (-u) = null") {
            checkAll(vectorArb) { u ->
                val `u + _-u_` = u + (-u)

                `u + _-u_` shouldBeCloseTo nul
            }
        }

        it("is consistent with subtraction: -u = null - u") {
            checkAll(vectorArb) { u ->
                val `-u` = -u
                val `null - u` = nul - u

                `-u` shouldBeCloseTo `null - u`
            }
        }
    }

    describe("addition ($d)") {
        it("is commutative: u + v = v + u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `u + v` = u + v
                val `v + u` = v + u

                `u + v` shouldBeCloseTo `v + u`
            }
        }

        it("is associative: u + (v + w) = (u + v) + w") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `u + _v + w_` = u + (v + w)
                val `_u + v_ + w` = (u + v) + w

                `u + _v + w_` shouldBeCloseTo `_u + v_ + w`
            }
        }

        it("is neutral with null vector: u + 0 = u") {
            checkAll(vectorArb) { u ->
                val `u + 0` = u + nul

                `u + 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vectorArb) { u ->
                val `u + unit` = u + unit

                assertSoftly {
                    d.range.forEach { i ->
                        `u + unit`[i] shouldBeCloseTo u[i] + 1.0
                    }
                }
            }
        }
    }

    describe("subtraction ($d)") {
        it("is consistent with addition: (u + v) - v = u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `_u + v_ - v` = (u + v) - v

                `_u + v_ - v` shouldBeCloseTo u
            }
        }
        it("is consistent with addition: (u - v) + v = u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `_u - v_ + v` = (u - v) + v

                `_u - v_ + v` shouldBeCloseTo u
            }
        }

        it("is neutral with null vector: u - 0 = u") {
            checkAll(vectorArb) { u ->
                val `u - 0` = u - nul

                `u - 0` shouldBeCloseTo u
            }
        }

        it("applies to each component") {
            checkAll(vectorArb) { u ->
                val `u - unit` = u - unit

                assertSoftly {
                    d.range.forEach { i ->
                        `u - unit`[i] shouldBeCloseTo u[i] - 1.0
                    }
                }
            }
        }
    }

    describe("scalar multiplication ($d)") {
        it("is associative: u × (x × y) = (u × x) × y") {
            checkAll(vectorArb, doubleArb(), doubleArb()) { u, x, y ->
                val `u × _x × y_` = u * (x * y)
                val `_u × x_ × y` = (u * x) * y

                `u × _x × y_` shouldBeCloseTo `_u × x_ × y`
            }
        }

        it("is neutral with one: u × 1.0 = u") {
            checkAll(vectorArb) { u ->
                val `u × one` = u * 1.0

                `u × one` shouldBeCloseTo u
            }
        }

        it("has zero property: u × 0.0 = null vector") {
            checkAll(vectorArb) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo nul
            }
        }

        it("is consistent with addition: u × 2.0 = u + u") {
            checkAll(vectorArb) { u ->
                val `u × two` = u * 2.0
                val `u + u` = u + u

                assertSoftly {
                    `u × two` shouldBeCloseTo `u + u`
                }
            }
        }
    }

    describe("vector multiplication ($d)") {
        it("is associative: u × (v × w) = (u × v) × w") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `u × _v × w_` = u * (v * w)
                val `_u × v_ × w` = (u * v) * w

                `u × _v × w_` shouldBeCloseTo `_u × v_ × w`
            }
        }

        it("is neutral with unit: u × unit = u") {
            checkAll(vectorArb) { u ->
                val `u × unit` = u * unit

                `u × unit` shouldBeCloseTo u
            }
        }

        it("has zero property: u × null vector = null vector") {
            checkAll(vectorArb) { u ->
                val `u × null` = u * nul

                `u × null` shouldBeCloseTo nul
            }
        }
    }

    describe("scalar division ($d)") {
        it("is consistent with scalar multiplication: (u × x) ÷ x = u") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u × x_ ÷ x` = (u * x) / x

                    `_u × x_ ÷ x` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with scalar multiplication: (u ÷ x) × x = u") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                withAssumptions(abs(x) > EPSILON) {
                    val `_u ÷ x_ × x` = (u / x) * x

                    `_u ÷ x_ × x` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with one: u ÷ 1.0 = u") {
            checkAll(vectorArb) { u ->
                val `u ÷ one` = u / 1.0

                `u ÷ one` shouldBeCloseTo u
            }
        }
    }

    describe("vector division ($d)") {
        it("is consistent with vector multiplication: (u × v) ÷ v = u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u × v_ ÷ v` = (u * v) / v

                    `_u × v_ ÷ v` shouldBeCloseTo u
                }
            }
        }

        it("is consistent with vector multiplication: (u ÷ v) × v = u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                withAssumptions(!v.isTooSmall()) {
                    val `_u ÷ v_ × v` = (u / v) * v

                    `_u ÷ v_ × v` shouldBeCloseTo u
                }
            }
        }

        it("is neutral with unit: u ÷ unit = u") {
            checkAll(vectorArb) { u ->
                val `u ÷ unit` = u / unit

                `u ÷ unit` shouldBeCloseTo u
            }
        }
    }

    describe("isLessThanOrEqual ($d)") {
        it("returns true when equal") {
            checkAll(vectorArb) { u ->
                u isLessThanOrEqual u shouldBe true
            }
        }

        it("returns true when compared with successor") {
            checkAll(vectorArb) { u ->
                u isLessThanOrEqual u + unit shouldBe true
            }
        }

        it("is consistent with addition") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `v lte u` = v isLessThanOrEqual u
                val `v + w lte u + w` = (v + w) isLessThanOrEqual (u + w)

                `v lte u` shouldBe `v + w lte u + w`
            }
        }

        it("is consistent with multiplication") {
            checkAll(vectorArb, vectorArb, doubleArb()) { u, v, r ->
                val k = abs(r)

                withAssumptions(k > EPSILON) {
                    val `v lte u` = v isLessThanOrEqual u
                    val `kv lte ku` = (v * k) isLessThanOrEqual (u * k)

                    `v lte u` shouldBe `kv lte ku`
                }
            }
        }

        it("is consistent with greaterThan") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `v lte u` = v isLessThanOrEqual u
                val `v gt u` = v isGreaterThan u

                if (`v lte u`) {
                    `v gt u` shouldBe false
                }
                if (`v gt u`) {
                    `v lte u` shouldBe false
                }
            }
        }

        it("is consistent with isLessThan") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `v lt u` = v isLessThan u
                val `v lte u` = v isLessThanOrEqual u

                if (`v lt u`) {
                    `v lte u` shouldBe true
                }
                if (!`v lte u`) {
                    `v lt u` shouldBe false
                }
            }
        }
    }

    describe("isLessThan ($d)") {
        it("returns true when compared with successor") {
            checkAll(vectorArb) { u ->
                u isLessThan u + unit shouldBe true
            }
        }

        it("is consistent with addition") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `v lt u` = v isLessThan u
                val `v + w lt u + w` = (v + w) isLessThan (u + w)

                `v lt u` shouldBe `v + w lt u + w`
            }
        }

        it("is consistent with multiplication") {
            checkAll(vectorArb, vectorArb, doubleArb()) { u, v, r ->
                val k = abs(r)
                withAssumptions(k > EPSILON) {
                    val `v lt u` = v isLessThan u
                    val `kv lt ku` = (v * k) isLessThan (u * k)

                    `v lt u` shouldBe `kv lt ku`
                }
            }
        }
    }

    describe("isGreaterThanOrEqual ($d)") {
        it("returns true when equal") {
            checkAll(vectorArb) { u ->
                u isGreaterThanOrEqual u shouldBe true
            }
        }

        it("returns true when compared with predecessor") {
            checkAll(vectorArb) { u ->
                u isGreaterThanOrEqual u - unit shouldBe true
            }
        }

        it("is consistent with addition") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `v gte u` = v isGreaterThanOrEqual u
                val `v + w gte u + w` = (v + w) isGreaterThanOrEqual (u + w)

                `v gte u` shouldBe `v + w gte u + w`
            }
        }

        it("is consistent with multiplication") {
            checkAll(vectorArb, vectorArb, doubleArb()) { u, v, r ->
                val k = abs(r)
                withAssumptions(k > EPSILON) {
                    val `v gte u` = v isGreaterThanOrEqual u
                    val `kv gte ku` = (v * k) isGreaterThanOrEqual (u * k)

                    `v gte u` shouldBe `kv gte ku`
                }
            }
        }

        it("is consistent with lessThan") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `v gte u` = v isGreaterThanOrEqual u
                val `v lt u` = v isLessThan u

                if (`v gte u`) {
                    `v lt u` shouldBe false
                }
                if (`v lt u`) {
                    `v gte u` shouldBe false
                }
            }
        }

        it("is consistent with isGreaterThan") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `v gt u` = v isGreaterThan u
                val `v gte u` = v isGreaterThanOrEqual u

                if (`v gt u`) {
                    `v gte u` shouldBe true
                }
                if (!`v gte u`) {
                    `v gt u` shouldBe false
                }
            }
        }
    }

    describe("isGreaterThan ($d)") {
        it("returns true when compared with predecessor") {
            checkAll(vectorArb) { u ->
                u isGreaterThan u - unit shouldBe true
            }
        }

        it("is consistent with addition") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `v lt u` = v isGreaterThan u
                val `v + w lt u + w` = (v + w) isGreaterThan (u + w)

                `v lt u` shouldBe `v + w lt u + w`
            }
        }

        it("is consistent with multiplication") {
            checkAll(vectorArb, vectorArb, doubleArb()) { u, v, r ->
                val k = abs(r)

                withAssumptions(k > EPSILON) {
                    val `v lt u` = v isGreaterThan u
                    val `kv lt ku` = (v * k) isGreaterThan (u * k)

                    `v lt u` shouldBe `kv lt ku`
                }
            }
        }
    }

    describe("length ($d)") {
        it("scales with scalar multiplication: ‖u × x‖ = ‖u‖ × ‖x‖") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                val `‖u × x‖` = (u * x).length()
                val `‖u‖ × ‖x‖` = u.length() * abs(x)

                `‖u × x‖` shouldBeCloseTo `‖u‖ × ‖x‖`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖ = ‖u‖ ÷ ‖x‖") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                withAssumptions(!x.isTooSmall()) {
                    val `‖u ÷ x‖` = (u / x).length()
                    val `‖u‖ ÷ ‖x‖` = u.length() / abs(x)

                    `‖u ÷ x‖` shouldBeCloseTo `‖u‖ ÷ ‖x‖`
                }
            }
        }

        it("returns zero for null vector: ‖null‖ = 0.0") {
            val `‖null‖` = nul.length()

            `‖null‖` shouldBeCloseTo 0.0
        }

        it("returns one for axis aligned unit vectors") {
            assertSoftly {
                axes.forEach { axis ->
                    axis.length() shouldBeCloseTo 1.0
                }
            }
        }
    }

    describe("squaredLength ($d)") {
        it("scales with scalar multiplication: ‖u × x‖² = ‖u‖² × x²") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                val `‖u × x‖²` = (u * x).squaredLength()
                val `‖u‖² × x²` = u.squaredLength() * x * x

                `‖u × x‖²` shouldBeCloseTo `‖u‖² × x²`
            }
        }

        it("scales with scalar division: ‖u ÷ x‖² = ‖u‖² ÷ x²") {
            checkAll(vectorArb, doubleArb()) { u, x ->
                withAssumptions(!x.isTooSmall()) {
                    val `‖u ÷ x‖²` = (u / x).squaredLength()
                    val `‖u‖² ÷ x²` = u.squaredLength() / (x * x)

                    `‖u ÷ x‖²` shouldBeCloseTo `‖u‖² ÷ x²`
                }
            }
        }

        it("returns zero for null vector: ‖null‖² = 0.0") {
            val `‖null‖²` = nul.squaredLength()

            `‖null‖²` shouldBeCloseTo 0.0
        }

        it("returns one for axis aligned unit vectors") {
            assertSoftly {
                axes.forEach { axis ->
                    axis.squaredLength() shouldBeCloseTo 1.0
                }
            }
        }
    }

    describe("normalized ($d)") {
        it("creates colinear vector: û = k×u") {
            checkAll(vectorArb) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val û = u.normalized()
                    val k = û.components().first() / u.components().first()

                    assertSoftly {
                        d.range.forEach { i ->
                            û[i] shouldBeCloseTo (k * u[i])
                        }
                    }
                }
            }
        }

        it("sets the length to one: ‖û‖ = 1.0") {
            checkAll(vectorArb) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val `‖û‖` = u.normalized().length()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("sets the squaredLength to one: ‖û‖² = 1.0") {
            checkAll(vectorArb) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val `‖û‖` = u.normalized().squaredLength()

                    `‖û‖` shouldBeCloseTo 1.0
                }
            }
        }

        it("leaves aligned unit vectors untouched") {
            assertSoftly {
                axes.forEach { axis ->
                    axis.normalized() shouldBeCloseTo axis
                }
            }
        }
    }

    describe("dot product ($d)") {
        it("is commutative: u · v = v · u") {
            checkAll(vectorArb, vectorArb) { u, v ->
                val `u · v` = u dot v
                val `v · u` = v dot u

                `u · v` shouldBeCloseTo `v · u`
            }
        }

        it("is distributive over addition: u · (v + w) = (u · v) + (u · w)") {
            checkAll(vectorArb, vectorArb, vectorArb) { u, v, w ->
                val `u · _v + w_` = u dot (v + w)
                val `_u · v_ + _u · w_` = (u dot v) + (u dot w)

                `u · _v + w_` shouldBeCloseTo `_u · v_ + _u · w_`
            }
        }

        it("is bilinear: u · (xv + w) = x(u · v) + (u · w)") {
            checkAll(vectorArb, vectorArb, vectorArb, doubleArb()) { u, v, w, x ->
                val `u · _xv + w_` = u dot ((v * x) + w)
                val `x_u · v_ + _u · w_` = ((u dot v) * x) + (u dot w)

                `u · _xv + w_` shouldBeCloseTo `x_u · v_ + _u · w_`
            }
        }

        it("is consistent with squaredLength: u · u = ‖u‖²") {
            checkAll(vectorArb) { u ->
                val `u · u` = u dot u
                val `‖u‖²` = u.squaredLength()

                `u · u` shouldBeCloseTo `‖u‖²`
            }
        }
    }

    describe("floor ($d)") {
        it("returns vector below original vector: ⌊u⌋ ≤ u") {
            checkAll(vectorArb) { u ->
                val `⌊u⌋` = u.floor()

                assertSoftly {
                    d.range.forEach { i ->
                        `⌊u⌋`[i] shouldBeLessThanOrEqual u[i]
                    }
                }
            }
        }

        it("returns vector at most unit from original vector: u - ⌊u⌋ ≤ unit") {
            checkAll(vectorArb) { u ->
                val `u - ⌊u⌋` = u - u.floor()

                assertSoftly {
                    d.range.forEach { i ->
                        `u - ⌊u⌋`[i] shouldBeLessThanOrEqual 1.0
                    }
                }
            }
        }
    }

    describe("ceil ($d)") {
        it("returns vector below original vector: ⌈u⌉ ≥ u") {
            checkAll(vectorArb) { u ->
                val `⌊u⌋` = u.ceil()

                assertSoftly {
                    d.range.forEach { i ->
                        `⌊u⌋`[i] shouldBeGreaterThanOrEqual u[i]
                    }
                }
            }
        }

        it("returns vector at most unit from original vector: ⌈u⌉ - u ≤ unit") {
            checkAll(vectorArb) { u ->
                val `⌈u⌉ - u` = u.ceil() - u

                assertSoftly {
                    d.range.forEach { i ->
                        `⌈u⌉ - u`[i] shouldBeLessThanOrEqual 1.0
                    }
                }
            }
        }
    }

    describe("abs ($d)") {
        it("preserves length: ‖|u|‖ = ‖u‖") {
            checkAll(vectorArb) { u ->
                val `‖❘u❘‖` = u.abs().length()
                val `‖u‖` = u.length()

                `‖❘u❘‖` shouldBeCloseTo `‖u‖`
            }
        }

        it("returns same result from opposite vectors: |u| = |-u|") {
            checkAll(vectorArb) { u ->
                val `❘u❘` = u.abs()
                val `❘-u❘` = (-u).abs()

                `❘u❘` shouldBeCloseTo `❘-u❘`
            }
        }
    }

    describe("reflect ($d)") {
        it("keeps the original vector's length: ‖r‖ = ‖u‖") {
            checkAll(vectorArb, vectorArb) { u, n ->
                withAssumptions(!(u.isTooSmall() || n.isTooSmall())) {
                    val r = u.reflect(n.normalized())

                    r.length() shouldBeCloseTo u.length()
                }
            }
        }

        it("reflects to the normal when co-linear") {
            checkAll(vectorArb) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val normal = -(u.normalized())
                    val r = u.reflect(normal)

                    r shouldBeCloseTo -u
                }
            }
        }

        it("reflects against base axes") {
            checkAll(vectorArb) { u ->
                axes.forEach { axis ->
                    val r = u.reflect(axis)

                    d.range.forEach { i ->
                        if (axis[i] == 0.0) {
                            r[i] shouldBeCloseTo u[i]
                        } else {
                            r[i] shouldBeCloseTo -u[i]
                        }
                    }
                }
            }
        }
    }

    describe("components ($d)") {
        it("is consistent with constructor") {
            checkAll(vectorArb) { u ->
                val copy = Vector<D>(u.components().toDoubleArray())

                copy shouldBeCloseTo u
            }
        }

        it("has components in order") {
            checkAll(vectorArb) { u ->
                val components = u.components()

                components shouldHaveSize d.size
                d.range.forEach { i ->
                    components[i] shouldBe u[i]
                }
            }
        }
    }

    describe("isNaN ($d)") {
        it("returns false for non-NaN values") {
            checkAll(vectorArb) { v ->
                v.isNaN() shouldBe false
            }
        }

        d.range.forEach { i ->
            it("returns true if one component is NaN") {
                checkAll(
                    arbitrary {
                        Vector<D>(
                            DoubleArray(d.size) {
                                if (it == i) {
                                    doubleNaNArb().bind()
                                } else {
                                    doubleArb().bind()
                                }
                            },
                        )
                    },
                ) { v ->
                    v.isNaN() shouldBe true
                }
            }
        }
    }

    describe("isInfinite ($d)") {
        it("returns false for non-inifinite values") {
            checkAll(vectorArb) { v ->
                v.isInfinite() shouldBe false
            }
        }

        d.range.forEach { i ->
            it("returns true if one component is infinite") {
                checkAll(
                    arbitrary {
                        Vector<D>(
                            DoubleArray(d.size) {
                                if (it == i) {
                                    doubleInfiniteArb().bind()
                                } else {
                                    doubleArb().bind()
                                }
                            },
                        )
                    },
                ) { v ->
                    v.isInfinite() shouldBe true
                }
            }
        }
    }

    describe("asMatrix ($d)") {
        it("returns a horizontal matrix with the proper size") {
            checkAll(vectorArb) { v ->
                val m = v.asHorizontalMatrix()

                m.width shouldBe d.size
                m.height shouldBe 1
            }
        }

        it("returns a horizontal matrix with the same data") {
            checkAll(vectorArb) { v ->
                val m = v.asHorizontalMatrix()

                assertSoftly {
                    d.range.forEach { i ->
                        m.get(i, 0) shouldBeCloseTo v[i]
                    }
                }
            }
        }

        it("returns a vertical matrix with the proper size") {
            checkAll(vectorArb) { v ->
                val m = v.asVerticalMatrix()

                m.height shouldBe d.size
                m.width shouldBe 1
            }
        }

        it("returns a vertical matrix with the same data") {
            checkAll(vectorArb) { v ->
                val m = v.asVerticalMatrix()

                assertSoftly {
                    d.range.forEach { i ->
                        m.get(0, i) shouldBeCloseTo v[i]
                    }
                }
            }
        }

        it("is consistent with squared length") {
            checkAll(vectorArb) { v ->
                val hor = v.asHorizontalMatrix()
                val ver = v.asVerticalMatrix()

                val res = hor * ver
                val sl = v.squaredLength()

                res.get(0, 0) shouldBeCloseTo sl
            }
        }
    }
}
