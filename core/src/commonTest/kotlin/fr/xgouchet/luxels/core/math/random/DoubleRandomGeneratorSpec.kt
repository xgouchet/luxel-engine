package fr.xgouchet.luxels.core.math.random

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlinx.datetime.Clock
import kotlin.math.abs

class DoubleRandomGeneratorSpec : DescribeSpec(
    {
        RndGen.resetSeed(Clock.System.now().epochSeconds)

        describe("uniform") {
            it("generates a uniform value between 0 and 100") {
                val iterations = 4096
                val buckets = IntArray(10) { 0 }
                repeat(iterations) {
                    val value = RndGen.double.uniform()
                    buckets[(value * 10).toInt()]++

                    value shouldBeGreaterThanOrEqual 0.0
                    value shouldBeLessThanOrEqual 1.0
                }

                val bucketSize = iterations / 10
                val offset = bucketSize / 8 // 12.5%
                println(buckets.joinToString())
                println("expected $bucketSize ± $offset")
                for (i in 0 until 10) {
                    buckets[i] shouldBeInRange (bucketSize - offset)..(bucketSize + offset)
                }
            }
        }

        describe("inRange") {
            it("returns min if max <= min") {
                checkAll(Arb.double(1024.0..4096.0), Arb.double(0.0..1024.0)) { min, max ->
                    val value = RndGen.double.inRange(min, max)

                    value shouldBe min
                }
            }

            it("generates a uniform value in the given range") {
                checkAll(Arb.double(0.0..128.0), Arb.double(1024.0..4096.0)) { min, max ->
                    val value = RndGen.double.inRange(min, max)

                    value shouldBeGreaterThanOrEqual min
                    value shouldBeLessThanOrEqual max
                }
            }
        }

        describe("gaussian") {
            it("generates a gaussian value around 50") {
                val iterations = 4096
                var sum = 0.0
                var deltaSum = 0.0

                repeat(iterations) {
                    val value = RndGen.double.gaussian()

                    sum += value
                    deltaSum += abs(value)
                }

                val resultMean = sum / iterations
                val resultStDev = deltaSum / iterations
                abs(resultMean) shouldBeLessThanOrEqual 0.05
                resultStDev shouldBeGreaterThanOrEqual 0.75
                resultStDev shouldBeLessThanOrEqual 1.25
            }

            it("generates a gaussian value ") {
                checkAll(Arb.double(256.0..1024.0), Arb.double(16.0..64.0)) { mean, stDev ->
                    val iterations = 4096
                    var sum = 0.0
                    var deltaSum = 0.0

                    repeat(iterations) {
                        val value = RndGen.double.gaussian(mean, stDev)
                        sum += value
                        deltaSum += abs(value - mean)
                    }

                    val resultMean = sum / iterations
                    val resultStDev = deltaSum / iterations
                    val offset = stDev / 3
                    resultMean shouldBeGreaterThanOrEqual (mean - offset)
                    resultMean shouldBeLessThanOrEqual (mean + offset)
                    resultStDev shouldBeGreaterThanOrEqual (stDev - offset)
                    resultStDev shouldBeLessThanOrEqual (stDev + offset)
                }
            }
        }
    },
)
