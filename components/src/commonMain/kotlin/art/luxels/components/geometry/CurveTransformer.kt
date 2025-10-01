package art.luxels.components.geometry

import art.luxels.core.math.EPSILON
import art.luxels.core.math.Matrix.Companion.unsafeMatrix
import art.luxels.core.utils.Transformer
import kotlin.math.abs
import kotlin.math.pow

/**
 * A [Transformer] converting a numeric input based on a curve.
 * @param curvePoints the control points for the curve
 * @property isLinear whether the interpolation should be linear or cubic
 */
class CurveTransformer(curvePoints: List<Pair<Double, Double>>, val isLinear: Boolean = true) :
    Transformer<Double, Double> {

    /**
     * Represents a Cubic Function of the form: f(x) = ax³ + bx² + cx + d.
     * It's only valid over the range min to max
     */
    internal data class CubicFunction(
        val a: Double,
        val b: Double,
        val c: Double,
        val d: Double,
        val minX: Double,
        val maxX: Double,
    ) {
        fun isInputValid(input: Double): Boolean {
            return (input >= minX) && (input <= maxX)
        }

        fun compute(input: Double): Double {
            var sum = a
            sum *= input
            sum += b
            sum *= input
            sum += c
            sum *= input
            sum += d

            return sum
        }
    }

    /**
     * Represents a Cubic Function of the form: f(x) = ax³ + bx² + cx + d.
     * It's only valid over the range min to max.
     */
    internal data class LinearFunction(val minX: Double, val minY: Double, val maxX: Double, val maxY: Double) {
        val rangeX = maxX - minX
        val rangeY = maxY - minY
        fun isInputValid(input: Double): Boolean {
            return (input >= minX) && (input <= maxX)
        }

        fun compute(input: Double): Double {
            return if (abs(rangeX) > EPSILON) {
                val t = (input - minX) / rangeX

                (t * rangeY) + minY
            } else {
                maxY
            }
        }
    }

    private val sortedPoints = curvePoints.sortedBy { it.first }

    private val cubicFunctions: Array<CubicFunction>
    private val linearFunctions: Array<LinearFunction>

    init {
        val pointCount = sortedPoints.size
        val polyCount = pointCount - 1
        val coefCount = polyCount * 4

        val globalMatrix = unsafeMatrix(coefCount + 1, coefCount)

        // TODO check input is sorted
        var row = 0
        var x = 0.0
        var y = 0.0

        // Main poly function
        for (p in 0 until polyCount) {
            x = sortedPoints[p].first
            y = sortedPoints[p].second
            globalMatrix.set(p * 4, row, x.pow(3))
            globalMatrix.set((p * 4) + 1, row, x.pow(2))
            globalMatrix.set((p * 4) + 2, row, x)
            globalMatrix.set((p * 4) + 3, row, 1.0)
            globalMatrix.set(coefCount, row, y)
            row++

            x = sortedPoints[p + 1].first
            y = sortedPoints[p + 1].second
            globalMatrix.set(p * 4, row, x.pow(3))
            globalMatrix.set((p * 4) + 1, row, x.pow(2))
            globalMatrix.set((p * 4) + 2, row, x)
            globalMatrix.set((p * 4) + 3, row, 1.0)
            globalMatrix.set(coefCount, row, y)
            row++
        }

        // first order derivatives
        for (p in 0 until (polyCount - 1)) {
            x = sortedPoints[p + 1].first

            globalMatrix.set(p * 4, row, 3.0 * x.pow(2))
            globalMatrix.set((p * 4) + 1, row, 2.0 * x)
            globalMatrix.set((p * 4) + 2, row, 1.0)
            globalMatrix.set((p * 4) + 4, row, -3.0 * x.pow(2))
            globalMatrix.set((p * 4) + 5, row, -2.0 * x)
            globalMatrix.set((p * 4) + 6, row, -1.0)
            row++
        }

        // second order derivatives
        for (p in 0 until (polyCount - 1)) {
            x = sortedPoints[p + 1].first

            globalMatrix.set(p * 4, row, 6.0 * x)
            globalMatrix.set((p * 4) + 1, row, 2.0)
            globalMatrix.set((p * 4) + 4, row, -6.0 * x)
            globalMatrix.set((p * 4) + 5, row, -2.0)
            row++
        }

        // Boundaries of the curve must have 0 second order derivatives
        x = sortedPoints.first().first
        globalMatrix.set(0, row, 6.0 * x)
        globalMatrix.set(1, row, 2.0)
        row++
        x = sortedPoints.last().first
        globalMatrix.set((polyCount * 4) - 4, row, -6.0 * x)
        globalMatrix.set((polyCount * 4) - 3, row, -2.0)
        row++

        val rref = globalMatrix.reducedRowEchelonForm()
        val polynomialCoefs = rref.column(coefCount)

        cubicFunctions = Array(polyCount) { n ->
            CubicFunction(
                a = polynomialCoefs.get(0, (n * 4)),
                b = polynomialCoefs.get(0, (n * 4) + 1),
                c = polynomialCoefs.get(0, (n * 4) + 2),
                d = polynomialCoefs.get(0, (n * 4) + 3),
                minX = sortedPoints[n].first,
                maxX = sortedPoints[n + 1].first,
            )
        }

        linearFunctions = Array(polyCount) { n ->
            LinearFunction(
                minX = sortedPoints[n].first,
                minY = sortedPoints[n].second,
                maxX = sortedPoints[n + 1].first,
                maxY = sortedPoints[n + 1].second,
            )
        }
    }

    // region Transformer

    override fun transform(input: Double): Double {
        if (isLinear) {
            val function = linearFunctions.firstOrNull { it.isInputValid(input) }
            return function?.compute(input) ?: 0.0
        } else {
            val function = cubicFunctions.firstOrNull { it.isInputValid(input) }
            return function?.compute(input) ?: 0.0
        }
    }

    // endregion
}
