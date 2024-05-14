package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
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

fun <D : Dimension> beIn(volume: Volume<D>) = Matcher<Vector<D>> { value ->
    MatcherResult(
        (value in volume),
        { "Vector $value should be in volume $volume" },
        { "Vector $value should not be in volume $volume" },
    )
}
