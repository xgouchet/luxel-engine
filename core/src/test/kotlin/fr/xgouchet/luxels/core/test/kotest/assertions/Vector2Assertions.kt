package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.position.Rectangle
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

fun beIn(rectangle: Rectangle) = Matcher<Vector2> { value ->
    MatcherResult(
        (value in rectangle),
        { "Vector $value should be in rectangle $rectangle" },
        { "Vector $value should not be in rectangle $rectangle" },
    )
}

infix fun Vector2.shouldBeCloseTo(other: Vector2): Vector2 {
    this should beCloseTo(other)

    return this
}

infix fun Vector2.shouldBeIn(rectangle: Rectangle): Vector2 {
    this should beIn(rectangle)
    return this
}

infix fun Vector2.shouldNotBeIn(rectangle: Rectangle): Vector2 {
    this shouldNot beIn(rectangle)
    return this
}
