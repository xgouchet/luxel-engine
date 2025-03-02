package art.luxels.core.math.random

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlinx.datetime.Clock
import kotlin.math.abs

class IntRandomGeneratorSpec : DescribeSpec(
    {
        RndGen.resetSeed(Clock.System.now().epochSeconds)

        describe("uniform") {
            it("generates a uniform value between 0 and 100") {
                val iterations = 4096
                val buckets = IntArray(10) { 0 }
                repeat(iterations) {
                    val value = RndGen.int.uniform()
                    buckets[value / 10]++

                    value shouldBeInRange 0..100
                }

                val bucketSize = iterations / 10
                val offset = bucketSize / 8 // 12.5%
                for (i in 0 until 10) {
                    buckets[i] shouldBeInRange (bucketSize - offset)..(bucketSize + offset)
                }
            }
        }

        describe("inRange") {
            it("returns min if max <= min") {
                checkAll(Arb.int(1024..4096), Arb.int(0..1024)) { min, max ->
                    val value = RndGen.int.inRange(min, max)

                    value shouldBe min
                }
            }

            it("generates a uniform value in the given range") {
                checkAll(Arb.int(0..128), Arb.int(1024..4096)) { min, max ->
                    val value = RndGen.int.inRange(min, max)

                    value shouldBeInRange min..max
                }
            }
        }

        describe("gaussian") {
            it("generates a gaussian value around 50") {
                val iterations = 4096
                var sum = 0
                var deltaSum = 0

                repeat(iterations) {
                    val value = RndGen.int.gaussian()

                    sum += value
                    deltaSum += abs(value - 50)
                }

                val resultMean = sum / iterations
                val resultStDev = deltaSum / iterations
                resultMean shouldBeInRange 45..55
                resultStDev shouldBeInRange 15..25
            }

            it("generates a gaussian value ") {
                checkAll(Arb.int(256..1024), Arb.int(32..64)) { mean, stDev ->
                    val iterations = 4096
                    var sum = 0
                    var deltaSum = 0

                    repeat(iterations) {
                        val value = RndGen.int.gaussian(mean, stDev)
                        sum += value
                        deltaSum += abs(value - mean)
                    }

                    val resultMean = sum / iterations
                    val resultStDev = deltaSum / iterations
                    val offset = stDev / 3
                    resultMean shouldBeInRange (mean - offset)..(mean + offset)
                    resultStDev shouldBeInRange (stDev - offset)..(stDev + offset)
                }
            }
        }
    },
)
