package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.geometry.Space2
import fr.xgouchet.luxels.core.math.geometry.Vector2
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.math.abs

fun Vector2.isTooSmall(): Boolean {
    return abs(x) <= 0.001 || abs(y) <= 0.001
}

fun beCloseTo(other: Vector2) = Matcher<Vector2> { value ->
    val passed = (value.x isCloseTo other.x) && (value.y isCloseTo other.y)

    MatcherResult(
        passed,
        { "Vector $value should be close to $other" },
        { "Vector $value should not be close to $other" },
    )
}

fun beIn(space2: Space2) = Matcher<Vector2> { value ->
    MatcherResult(
        (value in space2),
        { "Vector $value should be in rectangle $space2" },
        { "Vector $value should not be in rectangle $space2" },
    )
}

infix fun Vector2.shouldBeCloseTo(other: Vector2): Vector2 {
    this should beCloseTo(other)

    return this
}

infix fun Vector2.shouldBeIn(space2: Space2): Vector2 {
    this should beIn(space2)
    return this
}

infix fun Vector2.shouldNotBeIn(space2: Space2): Vector2 {
    this shouldNot beIn(space2)
    return this
}
