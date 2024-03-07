package fr.xgouchet.graphikio.test.kotest.assertions

import fr.xgouchet.graphikio.color.UnboundColor
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.compose.all
import io.kotest.matchers.doubles.Percentage
import io.kotest.matchers.doubles.percent
import io.kotest.matchers.should
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.nextDown
import kotlin.math.nextUp

private fun channelIsWithinPercentageOf(
    channelName: String,
    getChannel: UnboundColor.() -> Double,
    other: UnboundColor,
    percentage: Percentage,
) = Matcher<UnboundColor> { value ->
    val otherChannel = other.getChannel()
    val tolerance = max(otherChannel.times(percentage.value / 100.0).absoluteValue, 0.00001)
    val minBound = (otherChannel - tolerance).nextDown()
    val maxBound = (otherChannel + tolerance).nextUp()
    val range = minBound..maxBound
    MatcherResult(
        value.getChannel() in range,
        { "${value.getChannel()} $channelName channel is not within ${percentage.value}% of expected ${other.r}" },
        { "${value.getChannel()} $channelName channel is within ${percentage.value}% of expected ${other.r}" },
    )
}

fun redIsWithinPercentageOf(other: UnboundColor, percentage: Percentage) =
    channelIsWithinPercentageOf("red", { r }, other, percentage)

fun greenIsWithinPercentageOf(other: UnboundColor, percentage: Percentage) =
    channelIsWithinPercentageOf("green", { g }, other, percentage)

fun blueIsWithinPercentageOf(other: UnboundColor, percentage: Percentage) =
    channelIsWithinPercentageOf("blue", { b }, other, percentage)

fun alphaIsWithinPercentageOf(other: UnboundColor, percentage: Percentage) =
    channelIsWithinPercentageOf("alpha", { a }, other, percentage)

fun beWithinPercentageOf(other: UnboundColor, percentage: Percentage) = Matcher.all(
    redIsWithinPercentageOf(other, percentage),
    greenIsWithinPercentageOf(other, percentage),
    blueIsWithinPercentageOf(other, percentage),
    alphaIsWithinPercentageOf(other, percentage),
)

fun beCloseTo(other: UnboundColor) = beWithinPercentageOf(other, 1.percent)

infix fun UnboundColor.shouldBeCloseTo(other: UnboundColor) {
    this should beCloseTo(other)
}
