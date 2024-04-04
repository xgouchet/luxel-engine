package fr.xgouchet.graphikio.test.kotest.assertions

import fr.xgouchet.graphikio.color.HDRColor
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
    getChannel: HDRColor.() -> Double,
    other: HDRColor,
    percentage: Percentage,
) = Matcher<HDRColor> { value ->
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

fun redIsWithinPercentageOf(other: HDRColor, percentage: Percentage) =
    channelIsWithinPercentageOf("red", { r }, other, percentage)

fun greenIsWithinPercentageOf(other: HDRColor, percentage: Percentage) =
    channelIsWithinPercentageOf("green", { g }, other, percentage)

fun blueIsWithinPercentageOf(other: HDRColor, percentage: Percentage) =
    channelIsWithinPercentageOf("blue", { b }, other, percentage)

fun alphaIsWithinPercentageOf(other: HDRColor, percentage: Percentage) =
    channelIsWithinPercentageOf("alpha", { a }, other, percentage)

fun beWithinPercentageOf(other: HDRColor, percentage: Percentage) = Matcher.all(
    redIsWithinPercentageOf(other, percentage),
    greenIsWithinPercentageOf(other, percentage),
    blueIsWithinPercentageOf(other, percentage),
    alphaIsWithinPercentageOf(other, percentage),
)

fun beCloseTo(other: HDRColor) = beWithinPercentageOf(other, 1.percent)

infix fun HDRColor.shouldBeCloseTo(other: HDRColor) {
    this should beCloseTo(other)
}
