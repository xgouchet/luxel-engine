package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.position.Space3
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

infix fun Space3.isCloseTo(other: Space3): Boolean {
    return (min isCloseTo other.min) &&
        (max isCloseTo other.max)
}

fun beCloseTo(other: Space3) = Matcher<Space3> { value ->
    val passed = value isCloseTo other

    MatcherResult(
        passed,
        { "Box $value should be close to $other" },
        { "Box $value should not be close to $other" },
    )
}

infix fun Space3.shouldBeCloseTo(other: Space3): Space3 {
    this should beCloseTo(other)

    return this
}
