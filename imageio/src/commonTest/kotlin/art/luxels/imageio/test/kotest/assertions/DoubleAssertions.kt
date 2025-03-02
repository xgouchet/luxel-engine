package art.luxels.imageio.test.kotest.assertions

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.doubles.percent
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import kotlin.math.abs

fun Double.isTooSmall(): Boolean {
    return abs(this) <= 0.001
}

infix fun Double.shouldBeCloseTo(other: Double): Double {
    this shouldBe other.plusOrMinus(1.percent)
    return this
}

fun Double.shouldBeInRange(min: Double, max: Double): Double {
    assertSoftly(this) {
        shouldBeGreaterThanOrEqual(min)
        shouldBeLessThanOrEqual(max)
    }
    return this
}
