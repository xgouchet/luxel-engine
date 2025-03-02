package art.luxels.components.test.kotest.assertions

import art.luxels.core.math.EPSILON
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.should
import kotlin.math.abs
import kotlin.math.max

fun Double.isTooSmall(): Boolean {
    return abs(this) <= 0.001
}

infix fun Double.isCloseTo(other: Double): Boolean {
    val threshold = max(max(abs(other * 0.001), abs(this * 0.001)), EPSILON)
    return abs(this - other) <= threshold
}

fun beCloseTo(other: Double) = Matcher<Double> { value ->
    MatcherResult(
        value isCloseTo other,
        { "Double $value should be close to $other" },
        { "Double $value should not be close to $other" },
    )
}

infix fun Double.shouldBeCloseTo(other: Double): Double {
    this should beCloseTo(other)
    return this
}

fun Double.shouldBeInRange(min: Double, max: Double): Double {
    assertSoftly(this) {
        shouldBeGreaterThanOrEqual(min)
        shouldBeLessThanOrEqual(max)
    }
    return this
}
