package fr.xgouchet.luxels.core.math

import fr.xgouchet.luxels.core.test.kotest.assertions.shouldBeCloseTo
import io.kotest.core.spec.style.DescribeSpec

class MatrixSpec : DescribeSpec({

    describe("Hardcoded examples") {
        it("Computes inverse") {
            val a = Matrix3x3().apply {
                set(0, 0, -3.0)
                set(1, 0, 2.0)
                set(2, 0, -1.0)
                set(0, 1, 6.0)
                set(1, 1, -6.0)
                set(2, 1, 7.0)
                set(0, 2, 3.0)
                set(1, 2, -4.0)
                set(2, 2, 4.0)
            }

            val inv = Matrix3x3().apply {
                set(0, 0, -(1.0 / 3.0))
                set(1, 0, 1.0 / 3.0)
                set(2, 0, -(2.0 / 3.0))
                set(0, 1, 0.25)
                set(1, 1, 0.75)
                set(2, 1, -1.25)
                set(0, 2, 0.5)
                set(1, 2, 0.5)
                set(2, 2, -0.5)
            }

            a.inverse() shouldBeCloseTo inv
        }

        it("Computes inverse") {
            val a = Matrix3x3().apply {
                set(0, 0, 0.0)
                set(1, 0, 1.0)
                set(2, 0, 0.0)
                set(0, 1, -1.0)
                set(1, 1, 0.0)
                set(2, 1, 0.0)
                set(0, 2, 0.0)
                set(1, 2, -1.0)
                set(2, 2, 1.0)
            }

            val inv = Matrix3x3().apply {
                set(0, 0, 0.0)
                set(1, 0, -1.0)
                set(2, 0, 0.0)
                set(0, 1, 1.0)
                set(1, 1, 0.0)
                set(2, 1, 0.0)
                set(0, 2, 1.0)
                set(1, 2, 0.0)
                set(2, 2, 1.0)
            }

            a.inverse() shouldBeCloseTo inv
        }
        it("Computes determinant 3x3") {
            val a = Matrix3x3().apply {
                set(0, 0, 4.0)
                set(1, 0, 2.0)
                set(2, 0, 3.0)
                set(0, 1, 3.0)
                set(1, 1, -1.0)
                set(2, 1, 1.0)
                set(0, 2, 0.0)
                set(1, 2, 2.0)
                set(2, 2, 4.0)
            }

            a.determinant() shouldBeCloseTo (-30.0)
        }
        it("Computes determinant 4x4") {
            val a = Matrix4x4().apply {
                set(0, 0, 1.0)
                set(1, 0, 2.0)
                set(2, 0, 1.0)
                set(3, 0, 0.0)
                set(0, 1, 0.0)
                set(1, 1, 3.0)
                set(2, 1, 1.0)
                set(3, 1, 1.0)
                set(0, 2, -1.0)
                set(1, 2, 0.0)
                set(2, 2, 3.0)
                set(3, 2, 1.0)
                set(0, 3, 3.0)
                set(1, 3, 1.0)
                set(2, 3, 2.0)
                set(3, 3, 0.0)
            }

            a.determinant() shouldBeCloseTo 16.0
        }
    }

    include(abstractMatrixSpec(Dimension.D1, Dimension.D1))
    include(abstractMatrixSpec(Dimension.D1, Dimension.D2))
    include(abstractMatrixSpec(Dimension.D1, Dimension.D3))
    include(abstractMatrixSpec(Dimension.D1, Dimension.D4))

    include(abstractMatrixSpec(Dimension.D2, Dimension.D1))
    include(abstractMatrixSpec(Dimension.D2, Dimension.D2))
    include(abstractMatrixSpec(Dimension.D2, Dimension.D3))
    include(abstractMatrixSpec(Dimension.D2, Dimension.D4))

    include(abstractMatrixSpec(Dimension.D3, Dimension.D1))
    include(abstractMatrixSpec(Dimension.D3, Dimension.D2))
    include(abstractMatrixSpec(Dimension.D3, Dimension.D3))
    include(abstractMatrixSpec(Dimension.D3, Dimension.D4))

    include(abstractMatrixSpec(Dimension.D4, Dimension.D1))
    include(abstractMatrixSpec(Dimension.D4, Dimension.D2))
    include(abstractMatrixSpec(Dimension.D4, Dimension.D3))
    include(abstractMatrixSpec(Dimension.D4, Dimension.D4))
})
