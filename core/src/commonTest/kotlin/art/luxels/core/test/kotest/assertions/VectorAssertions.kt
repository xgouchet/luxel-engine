package art.luxels.core.test.kotest.assertions

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.math.abs

fun <D : Dimension> Vector<D>.isTooSmall(): Boolean {
    return components().any { abs(it) <= 0.001 }
}

infix fun <D : Dimension> Vector<D>.isCloseTo(other: Vector<D>): Boolean {
    val components = components()
    val otherComponents = other.components()
    check(components.size == otherComponents.size)

    return components.zip(otherComponents) { t, o -> t isCloseTo o }.all { it }
}

fun <D : Dimension> beCloseTo(other: Vector<D>) = Matcher<Vector<D>> { value ->
    MatcherResult(
        value isCloseTo other,
        { "Vector $value should be close to $other" },
        { "Vector $value should not be close to $other" },
    )
}

infix fun <D : Dimension> Vector<D>.shouldBeCloseTo(other: Vector<D>): Vector<D> {
    this should beCloseTo(other)
    return this
}

fun <D : Dimension> beIn(volume: Volume<D>) = Matcher<Vector<D>> { value ->
    MatcherResult(
        (value in volume),
        { "Vector $value should be in volume $volume" },
        { "Vector $value should not be in volume $volume" },
    )
}

infix fun <D : Dimension> Vector<D>.shouldBeIn(other: Volume<D>): Vector<D> {
    this should beIn(other)
    return this
}

infix fun <D : Dimension> Vector<D>.shouldNotBeIn(other: Volume<D>): Vector<D> {
    this shouldNot beIn(other)
    return this
}
