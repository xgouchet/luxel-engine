package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Matrix
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun <R : Dimension, C : Dimension> Matrix<R, C>.mismatchedValuesWith(other: Matrix<R, C>): List<Pair<Int, Int>> {
    check(width == other.width)
    check(height == other.height)

    val result = mutableListOf<Pair<Int, Int>>()

    for (i in 0..<width) {
        for (j in 0..<height) {
            if (!(get(i, j) isCloseTo other.get(i, j))) {
                result.add(i to j)
            }
        }
    }

    return result
}

infix fun <R : Dimension, C : Dimension> Matrix<R, C>.isCloseTo(other: Matrix<R, C>): Boolean {
    val mismatchedIndices = this.mismatchedValuesWith(other)

    return mismatchedIndices.isEmpty()
}

fun <R : Dimension, C : Dimension> beCloseTo(other: Matrix<R, C>) = Matcher<Matrix<R, C>> { value ->
    val mismatchedIndices = value.mismatchedValuesWith(other)

    MatcherResult(
        mismatchedIndices.isEmpty(),
        {
//            val displayMismatched = mismatchedIndices.joinToString { "[${it.first}, ${it.second}]" }
            "Matrix $value should be close to $other, but indices mismatched at $mismatchedIndices"
        },
        { "Matrix $value should not be close to $other" },
    )
}

infix fun <R : Dimension, C : Dimension> Matrix<R, C>.shouldBeCloseTo(other: Matrix<R, C>): Matrix<R, C> {
    this should beCloseTo(other)
    return this
}
