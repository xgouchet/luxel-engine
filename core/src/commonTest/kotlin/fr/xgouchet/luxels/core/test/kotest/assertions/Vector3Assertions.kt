package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space3
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.math.abs

fun Vector3.isTooSmall(): Boolean {
    return abs(x) <= 0.001 || abs(y) <= 0.001 || abs(z) <= 0.001
}

infix fun Vector3.isCloseTo(other: Vector3): Boolean {
    return (x isCloseTo other.x) &&
        (y isCloseTo other.y) &&
        (z isCloseTo other.z)
}

fun beCloseTo(other: Vector3) = Matcher<Vector3> { value ->
    val passed = value isCloseTo other

    MatcherResult(
        passed,
        { "Vector $value should be close to $other" },
        { "Vector $value should not be close to $other" },
    )
}

fun beIn(space3: Space3) = Matcher<Vector3> { value ->
    MatcherResult(
        (value in space3),
        { "Vector $value should be in box $space3" },
        { "Vector $value should not be in box $space3" },
    )
}

infix fun Vector3.shouldBeCloseTo(other: Vector3): Vector3 {
    this should beCloseTo(other)

    return this
}

infix fun Vector3.shouldBeIn(space3: Space3): Vector3 {
    this should beIn(space3)
    return this
}

infix fun Vector3.shouldNotBeIn(space3: Space3): Vector3 {
    this shouldNot beIn(space3)
    return this
}
