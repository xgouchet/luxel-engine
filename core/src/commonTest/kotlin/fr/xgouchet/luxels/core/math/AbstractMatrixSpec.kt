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
                        val `k⁻¹ × a⁻¹` = try {
                            a.inverse() * (1.0 / k)
                        } catch (e: Exception) {
                            null
                        }

                        if (`k⁻¹ × a⁻¹` != null) {
                            `_ka_⁻¹` shouldBeCloseTo `k⁻¹ × a⁻¹`
                        }
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
}
