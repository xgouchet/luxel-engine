package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import kotlin.math.abs

fun <D : Dimension> Vector<D>.isTooSmall(): Boolean {
    return components().any { abs(it) <= 0.001 }
}

fun <D : Dimension> beCloseTo(other: Vector<D>) = Matcher<Vector<D>> { value ->

    val components = value.components()
    val otherComponents = other.components()
    check(components.size == otherComponents.size)

    val passed = components.zip(otherComponents) { t, o -> t isCloseTo o }.all { it }

    MatcherResult(
        passed,
        { "Vector $value should be close to $other" },
        { "Vector $value should not be close to $other" },
    )
}

fun <D : Dimension> beNul() = Matcher<Vector<D>> { value ->

    val components = value.components()

    val passed = components.all { it isCloseTo 0.0 }

    MatcherResult(
        passed,
        { "Vector $value should be nul" },
        { "Vector $value should not be nul" },
    )
}

infix fun <D : Dimension> Vector<D>.shouldBeCloseTo(other: Vector<D>): Vector<D> {
    this should beCloseTo(other)

    return this
}

fun <D : Dimension> Vector<D>.shouldBeNul(): Vector<D> {
    this should beNul()

    return this
}
