package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.geometry.Vector4
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun Vector4.isTooSmall(): Boolean {
    return kotlin.math.abs(
        x,
    ) <= 0.001 || kotlin.math.abs(y) <= 0.001 || kotlin.math.abs(z) <= 0.001 || kotlin.math.abs(w) <= 0.001
}

fun beCloseTo(other: Vector4) = Matcher<Vector4> { value ->
    val passed = (value.x isCloseTo other.x) &&
        (value.y isCloseTo other.y) &&
        (value.z isCloseTo other.z) &&
        (value.w isCloseTo other.w)

    MatcherResult(
        passed,
        { "Vector $value should be close to $other" },
        { "Vector $value should not be close to $other" },
    )
}

infix fun Vector4.shouldBeCloseTo(other: Vector4): Vector4 {
    this should beCloseTo(other)

    return this
}
