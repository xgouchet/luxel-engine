package fr.xgouchet.luxels.core.test.kotest.assertions

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

infix fun <D : Dimension> Volume<D>.isCloseTo(other: Volume<D>): Boolean {
    return (min isCloseTo other.min) && (max isCloseTo other.max)
}

fun <D : Dimension> beCloseTo(other: Volume<D>) = Matcher<Volume<D>> { value ->
    MatcherResult(
        value isCloseTo other,
        { "Volume $value should be close to $other" },
        { "Volume $value should not be close to $other" },
    )
}

infix fun <D : Dimension> Volume<D>.shouldBeCloseTo(other: Volume<D>): Volume<D> {
    this should beCloseTo(other)
    return this
}
