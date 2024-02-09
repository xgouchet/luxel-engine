package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.color.Color
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun beCloseTo(other: Color) = Matcher<Color> { value ->
    val passed = (value.r isCloseTo other.r) &&
        (value.g isCloseTo other.g) &&
        (value.b isCloseTo other.b) &&
        (value.a isCloseTo other.a)

    MatcherResult(
        passed,
        { "Color should be close to $other but was $value" },
        { "Color should not be close to $other but was $value" },
    )
}

infix fun Color.shouldBeCloseTo(other: Color): Color {
    this should beCloseTo(other)
    return this
}
