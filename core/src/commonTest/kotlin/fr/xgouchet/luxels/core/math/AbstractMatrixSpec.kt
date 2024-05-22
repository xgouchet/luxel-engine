package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import fr.xgouchet.luxels.core.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.test.kotest.property.matrixArb
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.describeSpec
import io.kotest.property.checkAll
import io.kotest.property.withAssumptions
import kotlin.math.abs
import kotlin.math.pow

@Suppress("LocalVariableName", "NonAsciiCharacters", "ktlint:standard:property-naming")
fun <C : Dimension, R : Dimension> abstractMatrixSpec(cols: C, rows: R) = describeSpec {
    val zero = Matrix.zero(cols, rows)
    val one = Matrix.one(cols, rows)
    val identity = Matrix.identity(cols, rows)
    val matrixArb = matrixArb(cols, rows)

    val compatMatrixArb = matrixArb(rows, cols)
    val compatIdentity = Matrix.identity(cols, cols)

    describe("minus ($cols×$rows)") {
        it("is consistent with addition: m + (-m) = zero") {
            checkAll(matrixArb) { m ->
                val `m + _-m_` = m + (-m)

                `m + _-m_` shouldBeCloseTo zero
            }
        }

        it("is consistent with subtraction: -m = zero - m") {
            checkAll(matrixArb) { m ->
                val `-m` = -m
                val `null - m` = zero - m

                `-m` shouldBeCloseTo `null - m`
            }
        }
    }

    describe("addition ($cols×$rows)") {
        it("is commutative: u + v = v + u") {
            checkAll(matrixArb, matrixArb) { a, b ->
                val `a + b` = a + b
                val `b + a` = b + a

                `a + b` shouldBeCloseTo `b + a`
            }
        }

        it("is associative: a + (b + c) = (a + b) + c") {
            checkAll(matrixArb, matrixArb, matrixArb) { a, b, c ->
                val `a + _v + w_` = a + (b + c)
                val `_u + v_ + c` = (a + b) + c

                `a + _v + w_` shouldBeCloseTo `_u + v_ + c`
            }
        }

        it("is neutral with matrix zero: a + 0 = a") {
            checkAll(matrixArb) { a ->
                val `a + 0` = a + zero

                `a + 0` shouldBeCloseTo a
            }
        }

        it("applies to each component") {
            checkAll(matrixArb) { a ->
                val `a + one` = a + one

                assertSoftly {
                    cols.range.forEach { i ->
                        rows.range.forEach { j ->
                            `a + one`.get(i, j) shouldBeCloseTo a.get(i, j) + 1.0
                        }
                    }
                }
            }
        }
    }

    describe("subtraction ($cols×$rows)") {
        it("is consistent with addition: (a + b) - b = a") {
            checkAll(matrixArb, matrixArb) { a, b ->
                val `_a + b_ - b` = (a + b) - b

                `_a + b_ - b` shouldBeCloseTo a
            }
        }
        it("is consistent with addition: (a - b) + b = a") {
            checkAll(matrixArb, matrixArb) { a, b ->
                val `_a - b_ + b` = (a - b) + b

                `_a - b_ + b` shouldBeCloseTo a
            }
        }

        it("is neutral with null vector: a - 0 = a") {
            checkAll(matrixArb) { a ->
                val `a - 0` = a - zero

                `a - 0` shouldBeCloseTo a
            }
        }

        it("applies to each component") {
            checkAll(matrixArb) { a ->
                val `a - one` = a - one

                assertSoftly {
                    cols.range.forEach { i ->
                        rows.range.forEach { j ->
                            `a - one`.get(i, j) shouldBeCloseTo a.get(i, j) - 1.0
                        }
                    }
                }
            }
        }
    }

    describe("scalar multiplication ($cols×$rows)") {
        it("is associative: a × (x × y) = (a × x) × y") {
            checkAll(matrixArb, doubleArb(), doubleArb()) { a, x, y ->
                val `a × _x × y_` = a * (x * y)
                val `_a × x_ × y` = (a * x) * y

                `a × _x × y_` shouldBeCloseTo `_a × x_ × y`
            }
        }

        it("is neutral with one: a × 1.0 = u") {
            checkAll(matrixArb) { a ->
                val `a × id` = a * 1.0

                `a × id` shouldBeCloseTo a
            }
        }

        it("has zero property: u × 0.0 = zero matrix") {
            checkAll(matrixArb) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo zero
            }
        }

        it("is consistent with addition: a × 2.0 = a + a") {
            checkAll(matrixArb) { a ->
                val `a × two` = a * 2.0
                val `a + a` = a + a

                assertSoftly {
                    `a × two` shouldBeCloseTo `a + a`
                }
            }
        }
    }

    describe("matrix multiplication ($cols×$rows)") {
        it("is distributive with scalar multiplication: a × (b × n) = (a × b) × n") {
            checkAll(matrixArb, compatMatrixArb, doubleArb()) { a, b, y ->
                val `a × _b × y_` = a * (b * y)
                val `_a × b_ × y` = (a * b) * y

                `a × _b × y_` shouldBeCloseTo `_a × b_ × y`
            }
        }

        it("is distributive with matrix multiplication: a × (b × c) = (a × b) × c") {
            checkAll(matrixArb, compatMatrixArb, matrixArb) { a, b, c ->
                val `a × _b × c_` = a * (b * c)
                val `_a × b_ × c` = (a * b) * c

                `a × _b × c_` shouldBeCloseTo `_a × b_ × c`
            }
        }

        it("is distributive with postfix addition: a × (b + c) = (a × b) + (a × c)") {
            checkAll(matrixArb, compatMatrixArb, compatMatrixArb) { a, b, c ->
                val `a × _b + c_` = a * (b + c)
                val `_a × b_ + _a × c_` = (a * b) + (a * c)

                `a × _b + c_` shouldBeCloseTo `_a × b_ + _a × c_`
            }
        }

        it("is distributive with prefix addition: (a + b) × c = (a × c) + (b × c)") {
            checkAll(matrixArb, matrixArb, compatMatrixArb) { a, b, c ->
                val `_a + b_ × c` = (a + b) * c
                val `_a × c_ + _b × c_` = (a * c) + (b * c)

                `_a + b_ × c` shouldBeCloseTo `_a × c_ + _b × c_`
            }
        }

        it("is neutral with identity: a × id = a") {
            checkAll(matrixArb) { a ->
                val `a × id` = a * compatIdentity

                `a × id` shouldBeCloseTo a
            }
        }

        it("has zero property: u × 0.0 = zero matrix") {
            checkAll(matrixArb) { u ->
                val `u × zero` = u * 0.0

                `u × zero` shouldBeCloseTo zero
            }
        }

        it("is consistent with transposition: (a × b)ᵀ = aᵀ × bᵀ") {
            checkAll(matrixArb, compatMatrixArb) { a, b ->
                val `_a × b_T` = (a * b).transpose()
                val `bT × aT` = b.transpose() * a.transpose()

                `_a × b_T` shouldBeCloseTo `bT × aT`
            }
        }
    }

    describe("transpose ($cols×$rows)") {
        it("is idempotent when applied twice: (aᵀ)ᵀ = a") {
            checkAll(matrixArb) { a ->
                val aTT = a.transpose().transpose()

                aTT shouldBeCloseTo a
            }
        }

        it("is distributive with addition: (a + b)ᵀ = aᵀ + bᵀ") {
            checkAll(matrixArb, matrixArb) { a, b ->
                val `_a + b_T` = (a + b).transpose()
                val `aT + bT` = a.transpose() + b.transpose()

                `_a + b_T` shouldBeCloseTo `aT + bT`
            }
        }

        it("is distributive with scalar multiplication: (a × k)ᵀ = aᵀ × k") {
            checkAll(matrixArb, doubleArb()) { a, k ->
                val `_a × k_T` = (a * k).transpose()
                val `aT × k` = a.transpose() * k

                `_a × k_T` shouldBeCloseTo `aT × k`
            }
        }

        if (rows == cols) {
            it("is idempotent regarding determinant: det(aᵀ) = det(a)") {
                checkAll(matrixArb) { a ->
                    val `∣aT∣` = a.transpose().determinant()
                    val `∣a∣` = a.determinant()

                    `∣aT∣` shouldBeCloseTo `∣a∣`
                }
            }

            it("is commutative with inversion: (aᵀ)⁻¹ = (a⁻¹)ᵀ") {
                checkAll(matrixArb) { a ->
                    withAssumptions(a.isInvertible()) {
                        val `_aᵀ_⁻¹` = a.transpose().inverse()
                        val `_a⁻¹_ᵀ` = a.inverse().transpose()

                        `_aᵀ_⁻¹` shouldBeCloseTo `_a⁻¹_ᵀ`
                    }
                }
            }
        }
    }

    describe("determinant ($cols×$rows)") {
        if (rows != cols) {
            it("is not defined on a non square matrix") {
                checkAll(matrixArb) { a ->
                    shouldThrow<UnsupportedOperationException> {
                        a.determinant()
                    }
                }
            }
        }

        if (rows == cols) {
            it("is 1 for identity") {
                identity.determinant() shouldBeCloseTo 1.0
            }

            it("is homogenous: |ka| = k^${rows.size} |ka|") {
                checkAll(matrixArb, doubleArb()) { a, k ->
                    val `∣ka∣` = (a * k).determinant()
                    val `kⁿ × ∣a∣` = a.determinant() * (k.pow(rows.size))

                    `∣ka∣` shouldBeCloseTo `kⁿ × ∣a∣`
                }
            }
        }
    }

    describe("inverse ($cols×$rows)") {
        if (rows != cols) {
            it("is not defined on a non square matrix") {
                checkAll(matrixArb) { a ->
                    shouldThrow<UnsupportedOperationException> {
                        a.inverse()
                    }
                }
            }
        }

        if (rows == cols) {
            it("is idempotent when applied twice: (a⁻¹)⁻¹ = a") {
                checkAll(matrixArb) { a ->
                    withAssumptions(a.isInvertible()) {
                        val `_a⁻¹_⁻¹` = a.inverse().inverse()

                        `_a⁻¹_⁻¹` shouldBeCloseTo a
                    }
                }
            }
            it("is distributive with scalar multiplication: (ka)⁻¹ = k⁻¹ × a⁻¹") {
                checkAll(matrixArb, doubleArb()) { a, k ->
                    withAssumptions((abs(k) > EPSILON) && a.isInvertible()) {
                        val `_ka_⁻¹` = (a * k).inverse()
                        val `k⁻¹ × a⁻¹` = a.inverse() * (1.0 / k)

                        `_ka_⁻¹` shouldBeCloseTo `k⁻¹ × a⁻¹`
                    }
                }
            }
            it("is consistent with determinant: det(a⁻¹) = det(a)⁻¹") {
                checkAll(matrixArb) { a ->
                    withAssumptions(a.isInvertible()) {
                        val `∣aT∣` = a.transpose().determinant()
                        val `∣a∣` = a.determinant()

                        `∣aT∣` shouldBeCloseTo `∣a∣`
                    }
                }
            }
        }
    }

    /*
    describe("vector multiplication ($rows)") {
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

    describe("scalar division ($rows)") {
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

    describe("vector division ($rows)") {
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

    describe("isLessThanOrEqual ($rows)") {
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

    describe("isLessThan ($rows)") {
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

    describe("isGreaterThanOrEqual ($rows)") {
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

    describe("isGreaterThan ($rows)") {
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

    describe("length ($rows)") {
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

    describe("squaredLength ($rows)") {
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

    describe("normalized ($rows)") {
        it("creates colinear vector: û = k×u") {
            checkAll(vectorArb) { u ->
                withAssumptions(!u.isTooSmall()) {
                    val û = u.normalized()
                    val k = û.components().first() / u.components().first()

                    assertSoftly {
                        rows.range.forEach { i ->
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

    describe("dot product ($rows)") {
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

    describe("floor ($rows)") {
        it("returns vector below original vector: ⌊u⌋ ≤ u") {
            checkAll(vectorArb) { u ->
                val `⌊u⌋` = u.floor()

                assertSoftly {
                    rows.range.forEach { i ->
                        `⌊u⌋`[i] shouldBeLessThanOrEqual u[i]
                    }
                }
            }
        }

        it("returns vector at most unit from original vector: u - ⌊u⌋ ≤ unit") {
            checkAll(vectorArb) { u ->
                val `u - ⌊u⌋` = u - u.floor()

                assertSoftly {
                    rows.range.forEach { i ->
                        `u - ⌊u⌋`[i] shouldBeLessThanOrEqual 1.0
                    }
                }
            }
        }
    }

    describe("ceil ($rows)") {
        it("returns vector below original vector: ⌈u⌉ ≥ u") {
            checkAll(vectorArb) { u ->
                val `⌊u⌋` = u.ceil()

                assertSoftly {
                    rows.range.forEach { i ->
                        `⌊u⌋`[i] shouldBeGreaterThanOrEqual u[i]
                    }
                }
            }
        }

        it("returns vector at most unit from original vector: ⌈u⌉ - u ≤ unit") {
            checkAll(vectorArb) { u ->
                val `⌈u⌉ - u` = u.ceil() - u

                assertSoftly {
                    rows.range.forEach { i ->
                        `⌈u⌉ - u`[i] shouldBeLessThanOrEqual 1.0
                    }
                }
            }
        }
    }

    describe("abs ($rows)") {
        it("preserves length: ‖|u|‖ = ‖u‖") {
            checkAll(vectorArb) { u ->
                val `‖_u_‖` = u.abs().length()
                val `‖u‖` = u.length()

                `‖_u_‖` shouldBeCloseTo `‖u‖`
            }
        }

        it("returns same result from opposite vectors: |u| = |-u|") {
            checkAll(vectorArb) { u ->
                val _u_ = u.abs()
                val `_-u_` = (-u).abs()

                _u_ shouldBeCloseTo `_-u_`
            }
        }
    }

    describe("reflect ($rows)") {
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

                    rows.range.forEach { i ->
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

    describe("components ($rows)") {
        it("is consistent with constructor") {
            checkAll(vectorArb) { u ->
                val copy = Vector<R>(u.components().toDoubleArray())

                copy shouldBeCloseTo u
            }
        }

        it("has components in order") {
            checkAll(vectorArb) { u ->
                val components = u.components()

                components shouldHaveSize rows.size
                rows.range.forEach { i ->
                    components[i] shouldBe u[i]
                }
            }
        }
    }

    describe("isNaN ($rows)") {
        it("returns false for non-NaN values") {
            checkAll(
                vectorArb,
            ) { v ->
                v.isNaN() shouldBe false
            }
        }

        rows.range.forEach { i ->
            it("returns true if one component is NaN") {
                checkAll(
                    arbitrary {
                        Vector<R>(
                            DoubleArray(rows.size) {
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

    describe("isInifinite ($rows)") {
        it("returns false for non-inifinite values") {
            checkAll(
                vectorArb,
            ) { v ->
                v.isInfinite() shouldBe false
            }
        }

        rows.range.forEach { i ->
            it("returns true if one component is infinite") {
                checkAll(
                    arbitrary {
                        Vector<R>(
                            DoubleArray(rows.size) {
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

     */
}
